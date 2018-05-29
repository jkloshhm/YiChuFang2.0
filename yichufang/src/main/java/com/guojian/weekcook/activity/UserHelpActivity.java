package com.guojian.weekcook.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.guojian.weekcook.R;

public class UserHelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_help);

        findViewById(R.id.jump_to_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinQQGroup("zGr8I6wE3SwVPd-r2vN8rarOiX3SzYuC");
            }
        });
    }

    /****************
     *
     * 发起添加群流程。群号：&lt;易厨房&gt;问题反馈群(771632164) 的 key 为： zGr8I6wE3SwVPd-r2vN8rarOiX3SzYuC
     * 调用 joinQQGroup(zGr8I6wE3SwVPd-r2vN8rarOiX3SzYuC) 即可发起手Q客户端申请加群 &lt;易厨房&gt;问题反馈群(771632164)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            Toast.makeText(this,"未安装手Q或安装的版本不支持~",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
