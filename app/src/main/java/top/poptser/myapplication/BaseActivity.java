package top.poptser.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yf on 2017/10/20.
 */

public class BaseActivity  extends Activity{


    ProgressDialog waitingDialog;   //定义loading 框

    protected int now_action = 0; //1 所有司机端数据橄榄  2按物流点分类概览 3订单列表



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //状态栏透明化
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isTranslucentStatusBar()) {
                Window window = getWindow();
                // Translucent status bar
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }

    }
    //是否statusBar 状态栏为透明 的方法 默认为真
    protected boolean isTranslucentStatusBar() {
        return true;
    }

    //显示loading
    protected void showWaitingDialog(){
        waitingDialog= new ProgressDialog(this);
        waitingDialog.setTitle("不要拙计，加载中");
        waitingDialog.setMessage("加载中...");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false); //无法通过物理返回键和点击取消
        waitingDialog.show();
        //waitingDialog.hide();
    }
    //关闭loading...
    protected void hideWaitingDialog(){
        waitingDialog.hide();
    }

    //Toast 封装
    protected void show_toast(String txt){
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
    }



}
