package com.guojian.weekcook.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.guojian.weekcook.R;
import com.guojian.weekcook.statusbar.StatusBarCompat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 *
 * @author jack.guo
 */
public class RegisterActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordAgainView;
    private EditText mVerificationCodeView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mGetVerificationCodeButton;
    private String password;
    private String phoneNumber;
    private MyHandler mHandler = new MyHandler(this);

    private class MyHandler extends Handler {
        WeakReference<RegisterActivity> cookListActivityWeakReference;

        MyHandler(RegisterActivity cookListActivity) {
            cookListActivityWeakReference = new WeakReference<>(cookListActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Toast.makeText(RegisterActivity.this, "验证码已发送，请注意查收~", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2) {
                Toast.makeText(RegisterActivity.this, "验证码发送失败，请检查网络~", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 3) {
                showProgress(false);
                Toast.makeText(RegisterActivity.this, "验证成功~~~~~", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("password", password);
                intent.putExtra("phoneNumber", phoneNumber);
                //通过intent对象返回结果，必须要调用一个setResult方法，
                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                setResult(LoginActivity.RESULT_CODE, intent);
                Toast.makeText(RegisterActivity.this, "注册成功请用手机号和密码登录", Toast.LENGTH_SHORT).show();
                finish();

                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();

            } else if (msg.what == 4) {
                Toast.makeText(RegisterActivity.this, "验证码验证失败，请重新获取验证码~", Toast.LENGTH_SHORT).show();
                showProgress(false);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        StatusBarCompat.setStatusBarColor(this, ResourcesCompat.getColor(getResources(),R.color.white,null), true);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.tv_register_in_password);
        mPasswordAgainView = (EditText) findViewById(R.id.tv_register_in_password_again);
        mVerificationCodeView = (EditText) findViewById(R.id.tv_verification_code);
        mGetVerificationCodeButton = (Button) findViewById(R.id.tv_get_verification_code);

        mGetVerificationCodeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode("86", mEmailView.getText().toString().trim());
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        /*if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mPasswordAgainView.setError(null);
        mVerificationCodeView.setError(null);

        // Store values at the time of the login attempt.
        phoneNumber = mEmailView.getText().toString().trim();
        password = mPasswordView.getText().toString().trim();
        String passwordAgain = mPasswordAgainView.getText().toString().trim();
        String verificationCode = mVerificationCodeView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(phoneNumber)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isMobileValid(phoneNumber)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else if (TextUtils.isEmpty(verificationCode)) {
            mVerificationCodeView.setError("验证码为空");
            focusView = mVerificationCodeView;
            cancel = true;
        } else if (!isVerificationCodeValid(verificationCode)) {
            mVerificationCodeView.setError("验证码不规范");
            focusView = mVerificationCodeView;
            cancel = true;
        }

        // Check for a valid email address.
        else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("请设置您的密码");
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError("密码不符合规范");
            focusView = mPasswordView;
            cancel = true;
        } else if (TextUtils.isEmpty(passwordAgain)) {
            mPasswordAgainView.setError("请重新填写一次您的密码");
            focusView = mPasswordAgainView;
            cancel = true;
        } else if (!isPasswordValid(passwordAgain)) {
            mPasswordAgainView.setError("密码不符合规范");
            focusView = mPasswordAgainView;
            cancel = true;
        } else if (!password.equals(passwordAgain)) {
            mPasswordAgainView.setError("两次填写的密码不一样");
            focusView = mPasswordAgainView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            hintKeyboard();
            showProgress(true);
            //mAuthTask = new UserLoginTask(phoneNumber, password,verificationCode);
            //mAuthTask.execute((Void) null);
            submitCode("86", phoneNumber, verificationCode);
        }
    }

    /**
     * 验证手机号码格式
     */
    public static boolean isMobileValid(String number) {
        /*
        移动：134、135、136、137、138、139、150、151、152、157(TD)、158、159、178(新)、182、184、187、188
        联通：130、131、132、152、155、156、185、186、166
        电信：133、153、170、173、177、180、181、189、（1349卫通）
        总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
        */
        //"[1]"代表第1位为数字1，"[345678]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String num = "[1][345678]\\d{9}";
        //matches():字符串是否在给定的正则表达式匹配
        return !TextUtils.isEmpty(number) && number.matches(num);
    }

    /**
     * 验证码格式是否正确
     */
    private boolean isVerificationCodeValid(String code) {
        return code.length() >= 4 && code.length() <= 6;
    }


    /**
     * 密码格式是否正确
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= 4 && password.length() < 16;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(RegisterActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    /*public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mPhoneNumber;
        private final String mPassword;
        private final String mVerificationCode;

        UserLoginTask(String phoneNumber, String password, String verificationCode) {
            mPhoneNumber = phoneNumber;
            mPassword = password;
            mVerificationCode = verificationCode;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
                submitCode("86",mPhoneNumber,mVerificationCode);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent();
                intent.putExtra("password", mPassword);
                intent.putExtra("phoneNumber", mPhoneNumber);
                //通过intent对象返回结果，必须要调用一个setResult方法，
                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                setResult(LoginActivity.RESULT_CODE, intent);
                Toast.makeText(RegisterActivity.this, "注册成功请用手机号和密码登录", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }*/

    /**
     * 以非可视化界面完成操作
     *
     * @param country
     * @param phone
     */
    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public void sendCode(String country, String phone) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    //Log.i("jack_guo","发送短信成功");
                    //Toast.makeText(RegisterActivity.this,"验证码已发送",Toast.LENGTH_SHORT).show();
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    mHandler.sendEmptyMessage(1);
                } else {
                    // TODO 处理错误的结果
                    //Log.i("jack_guo","发送短信失败");
                    //Toast.makeText(RegisterActivity.this,"验证码发送出错" ,Toast.LENGTH_SHORT).show();
                    mHandler.sendEmptyMessage(2);
                }
            }
        });
        // 触发操作
        SMSSDK.getVerificationCode(country, phone);
    }

    // 提交验证码，其中的code表示验证码，如“1357”
    public boolean submitCode(String country, String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证成功的结果
                    //Toast.makeText(RegisterActivity.this,"验证成功",Toast.LENGTH_LONG).show();
                    mHandler.sendEmptyMessage(3);

                } else {
                    // TODO 处理错误的结果
                    //Toast.makeText(RegisterActivity.this,"验证验证失败",Toast.LENGTH_LONG).show();
                    mHandler.sendEmptyMessage(4);
                }

            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    }

    ;


    private void hintKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


}

