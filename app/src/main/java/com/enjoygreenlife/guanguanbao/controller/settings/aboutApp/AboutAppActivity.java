package com.enjoygreenlife.guanguanbao.controller.settings.aboutApp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import com.enjoygreenlife.guanguanbao.R;

public class AboutAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        processView();
        TextView tv_version =(TextView) findViewById(R.id.tv_version);

        tv_version.setText(getVersion(this));
    //tv_version.setText(CustomUtils.getVersionCode(this)+"");
    //之所以加“”是因为获取的versionCode是int类型的数据，加""直接转化为String,否则会报错

    }


    public static String getVersion(Context context)//获取版本号
    {
        try {
            PackageInfo packageInfo=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return context.getString(R.string.version_unknown);
        }
    }


    public static int getVersionCode(Context context)//获取版本号(内部识别号)
    {
        try {
            PackageInfo packageInfo=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

    private void processView() {
        // Set up on Top Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(getString(R.string.about));
    }
}
