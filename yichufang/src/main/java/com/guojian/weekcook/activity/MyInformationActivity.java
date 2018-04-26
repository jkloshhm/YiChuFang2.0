package com.guojian.weekcook.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guojian.weekcook.R;
import com.guojian.weekcook.statusbar.StatusBarCompat;
import com.guojian.weekcook.utils.GetBitmapFromSdCardUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MyInformationActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "MyInformationActivity_loglog";
    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    /* 头像文件 */
    private final String IMAGE_FILE_NAME = "head.jpg";
    private final String IMAGE_FILE_NAME_CAMERA = "head_camera.jpg";
    private String path = Environment.getExternalStorageDirectory() + "/YiChuFang/myHeadImg/";// sd路径
    private String colors[] = {"保密", "女", "男"};
    private String setItems[] = {"直接拍照", "从手机相册选择"};
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private TextView name, gender, email, personalSignature;

    private EditText editText;
    private ImageView headImage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_infomation);
        StatusBarCompat.setStatusBarColor(this, ResourcesCompat.getColor(getResources(),R.color.red_theme,null), false);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bitmap bt = GetBitmapFromSdCardUtil.getBitmap(path + "head.jpg");
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);
            headImage.setImageDrawable(drawable);
        }
    }


    private void setHeadImgAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyInformationActivity.this);
        builder.setItems(setItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        choseHeadImageFromCameraCapture();
                        break;
                    case 1:
                        choseHeadImageFromGallery();
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create();
        builder.show();
    }

    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可用，存储照片文件
        //File tempFile = new File(path, IMAGE_FILE_NAME_CAMERA);
        //tempFile.mkdirs();// 创建文件夹
        if(GetBitmapFromSdCardUtil.hasSdcard()) {
            File file = new File(path);
            file.mkdirs();// 创建文件夹
            //String fileName = path + IMAGE_FILE_NAME;// 图片名字
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path, IMAGE_FILE_NAME_CAMERA)));
        }
        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            return;
        }
        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());
                break;
            case CODE_CAMERA_REQUEST:
                if (GetBitmapFromSdCardUtil.hasSdcard()) {
                    File tempFile = new File(path, IMAGE_FILE_NAME_CAMERA);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG).show();
                }
                break;
            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {
        // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
        int output_X = 480;
        int output_Y = 480;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            if (photo != null) {
                /**
                 * 上传服务器代码
                 */
                headImage.setImageBitmap(photo);
                // head = toRoundBitmap1(head);//调用圆角处理方法
                setPicToView(photo);// 保存在SD卡中
                //image_qq1.setImageBitmap(head);// 用ImageView显示出来
                if (photo != null && photo.isRecycled()) {
                    photo.recycle();
                }
            }
        }
    }

    //保存图片到SD卡
    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        if (GetBitmapFromSdCardUtil.hasSdcard()) {
            File file = new File(path);
            file.mkdirs();// 创建文件夹
            String fileName = path + IMAGE_FILE_NAME;// 图片名字
            try {
                b = new FileOutputStream(fileName);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    // 关闭流
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(MyInformationActivity.this, "头像保存成功～", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MyInformationActivity.this, "没有SDCard, 保存失败～", Toast.LENGTH_LONG).show();
        }
    }

    private void initViews() {
        LinearLayout mBack = (LinearLayout) findViewById(R.id.ll_back_home_info);
        TextView mBackToFirst = (TextView) findViewById(R.id.tv_back_first_home_info);
        name = (TextView) findViewById(R.id.tv_my_information_my_name);
        gender = (TextView) findViewById(R.id.tv_my_information_my_gender);
        email = (TextView) findViewById(R.id.ll_my_information_my_email);
        personalSignature = (TextView) findViewById(R.id.tv_my_information_personal_signature);
        TextView mChangeMyHeadImg = (TextView) findViewById(R.id.tv_my_information_my_img_change);
        headImage = (ImageView) findViewById(R.id.iv_my_information_my_img);
        //headImage = (ImageView) findViewById(R.id.iv_my_information_my_img);

        LinearLayout mMyGenderChange = (LinearLayout) findViewById(R.id.ll_my_information_my_gender_change);
        LinearLayout mMyEmailChange = (LinearLayout) findViewById(R.id.ll_my_information_my_email_change);
        LinearLayout mMyPersonalSignatureChange = (LinearLayout) findViewById(R.id.ll_my_information_change_personal_signature);
        ImageView mMyImgChange = (ImageView) findViewById(R.id.iv_my_information_my_name_change);

        mSharedPreferences = getSharedPreferences("cooking", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        name.setText(mSharedPreferences.getString("name", "Mary"));
        gender.setText(mSharedPreferences.getString("gender", "保密"));
        email.setText(mSharedPreferences.getString("email", "未填写"));
        personalSignature.setText(mSharedPreferences.getString("personalSignature", "未填写"));
        mEditor.apply();
        if (mChangeMyHeadImg != null) mChangeMyHeadImg.setOnClickListener(this);
        if (headImage != null) headImage.setOnClickListener(this);
        if (mBack != null) mBack.setOnClickListener(this);
        if (mBackToFirst != null) mBackToFirst.setOnClickListener(this);
        if (mMyImgChange != null) mMyImgChange.setOnClickListener(this);
        if (mMyGenderChange != null) mMyGenderChange.setOnClickListener(this);
        if (mMyEmailChange != null) mMyEmailChange.setOnClickListener(this);
        if (mMyPersonalSignatureChange != null) mMyPersonalSignatureChange.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent mIntent;
        switch (v.getId()) {
            case R.id.ll_back_home_info:
                finish();
                break;
            case R.id.tv_back_first_home_info:
                mIntent = new Intent(MyInformationActivity.this, MainActivity.class);
                startActivity(mIntent);
                finish();
                break;
            case R.id.iv_my_information_my_img: //放大照片
                mIntent = new Intent(MyInformationActivity.this, HeadImageShowerActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tv_my_information_my_img_change://修改图片
                setHeadImgAlertDialog();
                break;
            case R.id.iv_my_information_my_name_change://修改名字
                ChangeNameEmailPersonalSignatureAlertDialog("name", name);
                editText.setHint("请输入昵称");
                editText.setText(mSharedPreferences.getString("name", "Mary"));
                break;
            case R.id.ll_my_information_my_gender_change://修改性别
                ChangeGenderAlertDialog();
                break;
            case R.id.ll_my_information_change_personal_signature://修改签名
                ChangeNameEmailPersonalSignatureAlertDialog("personalSignature", personalSignature);
                editText.setHint("请输入你的签名");
                editText.setText(mSharedPreferences.getString("personalSignature", "我是大厨～"));
                break;
            case R.id.ll_my_information_my_email_change://修改邮箱
                ChangeNameEmailPersonalSignatureAlertDialog("email", email);
                editText.setHint("请输入邮箱,如: jkloshhm@126.com");
                editText.setText(mSharedPreferences.getString("email", null));
                break;
            default:
                break;
        }
        mEditor.apply();
    }

    private void ChangeNameEmailPersonalSignatureAlertDialog(final String key, final TextView name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyInformationActivity.this);
        View  mAlertDialogView = getLayoutInflater().inflate(R.layout.alert_dialog_view_edit_text, null);
        editText = (EditText) mAlertDialogView.findViewById(R.id.edit_text_name_gender);
        builder.setView(mAlertDialogView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mEditor.putString(key, editText.getText().toString());
                mEditor.apply();
                name.setText(mSharedPreferences.getString(key, "未填写"));
                dialog.cancel();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    private void ChangeGenderAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyInformationActivity.this);
        int index = mSharedPreferences.getInt("gender_index", 0);
        builder.setSingleChoiceItems(colors, index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MyInformationActivity.this, colors[which], Toast.LENGTH_SHORT).show();
                mEditor.putInt("gender_index", which);
                mEditor.putString("gender", colors[which]);
                mEditor.apply();
                gender.setText(mSharedPreferences.getString("gender", null));
                dialog.cancel();
            }
        });
        builder.create();
        builder.show();
    }

}
