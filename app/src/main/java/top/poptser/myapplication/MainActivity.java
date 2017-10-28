package top.poptser.myapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.constraint.solver.SolverVariable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = "Main";

    /*
    当Activity第一次被实例化的时候系统会调用,
    整个生命周期只调用1次这个方法
    通常用于初始化设置: 1、为Activity设置所要使用的布局文件2、为按钮绑定监听器等静态的设置操作
    */

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent intent = getIntent();
//        String token = intent.getStringExtra("token");
//        if(token == null){
//            //未登录，跳转去登录
//            Intent intent_l  =  new Intent(this,LoginActivity.class);
//            startActivity(intent_l);
//        }else{
//            String user_id = intent.getStringExtra("user_id");
//            String role_type = intent.getStringExtra("role_type");
//            String contact = intent.getStringExtra("contact");
//            String phone = intent.getStringExtra("phone");
//            String logistics_name = intent.getStringExtra("logistics_name");
//            String logistics_status = intent.getStringExtra("logistics_status");
//            //已经登录,跳转到
//            if(Integer.parseInt(role_type) == 2){
//                //跳转到司机端
//                Intent intent_s  =  new Intent(this,DriverActivity.class);
//                intent_s.putExtra("token", token);
//                intent_s.putExtra("user_id", user_id);
//                intent_s.putExtra("role_type", role_type);
//                intent_s.putExtra("contact", contact);
//                intent_s.putExtra("phone", phone);
//                intent_s.putExtra("logistics_name", logistics_name);
//                intent_s.putExtra("logistics_status", logistics_status);
//                startActivity(intent_s);
//            }else{
//                show_toast("目前仅支持司机端登录");
//            }
//        }
        setContentView(R.layout.activity_main);
        Log.d(TAG, "start onCreate~~~");
    }






    //当Activity可见未获得用户焦点不能交互时系统会调用
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "start onStart~~~");
    }
    //当Activity已经停止然后重新被启动时系统会调用
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "start onRestart--");
    }
    //当Activity可见且获得用户焦点能交互时系统会调用
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "start onResume");
    }

    protected void onPause() {
        super.onPause();
        Log.d(TAG, "start onPause");
    }
    //当Activity被新的Activity完全覆盖不可见时被系统调用
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "start onStop");
    }
    //当Activity(用户调用finish()或系统由于内存不足)被系统销毁杀掉时系统调用,（
    // 整个生命周期只调用1次）用来释放onCreate ()方法中创建的资源,如结束线程等
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "已关闭程序", Toast.LENGTH_SHORT).show();
    }

    protected void show_list_view_activity(View view){
        Intent intent  =  new Intent(this,ListviewActivity.class);
        startActivity(intent);
    }


    protected void login_button(View view){
        Intent intent_l  =  new Intent(this,LoginActivity.class);
        startActivity(intent_l);
    }

}
