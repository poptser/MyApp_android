package top.poptser.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录界面
 * Created by yf on 2017/10/20.
 */

public class LoginActivity extends BaseActivity {
    //登录返回数据模型
    public class login_data {
        public String status;
        public String err_code;
        public String error_msg;
        public String token;
        public String user_id;
        public String role_type;
        public String contact;
        public String phone;
        public String logistics_name;
        public String logistics_status;
    }



    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);
    }
    //用户点击登录
    protected void submit_data(View view){
        //获取文本框内容
        String username=""; //用户名
        EditText user_name_view =(EditText)findViewById(R.id.username);
        username=user_name_view.getText().toString();
        String password=""; //密码
        EditText pssword_view =(EditText)findViewById(R.id.password);
        password=pssword_view.getText().toString();


        if(username.isEmpty()||password.isEmpty()){
            Toast.makeText(LoginActivity.this, "账号和密码需填写", Toast.LENGTH_SHORT).show();
        }else{
            //请求数据
            showWaitingDialog();
            postStringRequest(username,password);
        }


    }
    //恢复登录按钮可提交状态
    protected void re_button(){
        hideWaitingDialog();
    }


    //网络访问
    protected void postStringRequest(final String username , final String password) {
        String url="http://bsc.3send.cn/index.php?s=/Api/Login/login";
        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                data_json(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //callback.onError();
                show_toast(error.getMessage());
                Log.d("登录错误", "异常信息-->" + error.getMessage());
                re_button();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("name",username);
                map.put("password", password);
                return map;
            }
        };
        queue.add(request);
    }
    //处理登录后的json处理
    protected void data_json(String JSON_DATA){
        Log.d("登录返回信息：",JSON_DATA);
        Gson gson = new Gson();
        login_data person = gson.fromJson(JSON_DATA,login_data.class);

        if(Integer.parseInt(person.status) == 0){
            //登录失败
            re_button();
            show_toast(person.error_msg);
        }else{
            //登录成功
            show_toast(person.contact+" by "+person.logistics_name+",登录成功");

            //已经登录,跳转到
            if(Integer.parseInt(person.role_type) == 2){
                //跳转到司机端
                Intent intent_s  =  new Intent(this,DriverActivity.class);
                intent_s.putExtra("token", person.token);
                intent_s.putExtra("user_id", person.user_id);
                intent_s.putExtra("role_type", person.role_type);
                intent_s.putExtra("contact", person.contact);
                intent_s.putExtra("phone", person.phone);
                intent_s.putExtra("logistics_name", person.logistics_name);
                intent_s.putExtra("logistics_status", person.logistics_status);
                startActivity(intent_s);
                super.finish();
            }else{
                show_toast("目前仅支持司机端登录");
            }
        }
    }
    //登录界面点击返回键，直接退出程序
    public void onBackPressed() {
        super.onBackPressed();
    }
}
