package top.poptser.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yf on 2017/10/23.
 */

public class DriverActivity extends BaseActivity {

    private static String token = "";
    private static String user_id = "";
    private static String role_type = "";
    private static String contact = "";
    private static String phone = "";
    private static String logistics_name = "";
    private static String logistics_status = "";
    private static String wld_type = "";    //当前分物流点概览类型
    private static String overview_data_json = "";    //

    private ListView lv;
    ArrayList<HashMap<String, Object>> listItem_g;

    private DriverActivity.wld_data wld_data;   //物流点区域集合

    private static String wld_data_json = "";    //

    private static String wld_id = "0";//所点击物流点ID

    private DriverActivity.order_data order_data;   //订单集合
    private static String order_id = "0";//所点击订单ID

    //全部概览数据模型
    public class overview_data {
        public String status;
        public String err_code;
        public String error_msg;
        public String in_warehouse_back;
        public String need_to_warehouse_back;
        public String not_picking_up;
        public String in_transit;
    }

    //物流点订单数据模型
    public class wld_data{
        public String status;
        public String err_code;
        public String error_msg;
        public String is_last;
        public List<data_list> list;

        public class data_list{
            public String id;
            public String name;
            public String longitude;
            public String latitude;
            public String store_name;
            public String address;
            public String count;
        }
    }


    public class order_data{
        public String status;
        public String err_code;
        public String error_msg;
        public String is_last;
        public List<order_list> list;
        public class order_list{
            public String order_state;
            public String order_store;
            public String order_area;
            public String distribution_type;
            public String order_want_time;
            public String is_collection;
            public String third_sn;
            public String order_id;
            public String order_sn;
            public String sign_building;
            public String consignee;
            public String mobile;
            public String order_remark;
            public String order_time;
            public String address;
            public String latitude;
            public String longitude;
            public List<goods_list> goods_list;

            public class goods_list{
                public String id;
                public String order_id;
                public String third_sn;
                public String goods_sn;
                public String goods_name;
                public String goods_number;
                public String goods_weight;
                public String goods_price;
                public String goods_unit;
                public String refund_num;
            }
        }
    }


    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        user_id = intent.getStringExtra("user_id");
        role_type = intent.getStringExtra("role_type");
        contact = intent.getStringExtra("contact");
        phone = intent.getStringExtra("phone");
        logistics_name = intent.getStringExtra("logistics_name");
        logistics_status = intent.getStringExtra("logistics_status");
        //show_toast(contact);

        load_main_overview();
    }
    ///////////////////////////////////////////////////
    /*
         * 新建一个类继承BaseAdapter，实现视图与数据的绑定
         */
    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        /*构造函数*/
        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {

            return listItem_g.size();//返回数组的长度
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        /*书中详细解释该方法*/
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            //观察convertView随ListView滚动情况
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.order_list,
                        null);
                holder = new ViewHolder();
                    /*得到各个控件的对象*/

                holder.order_sn = (TextView) convertView.findViewById(R.id.order_sn);
                holder.third_sn = (TextView) convertView.findViewById(R.id.third_sn);
                holder.consignee = (TextView) convertView.findViewById(R.id.consignee);
                holder.mobile = (TextView) convertView.findViewById(R.id.mobile);
                holder.address = (TextView) convertView.findViewById(R.id.address);
                holder.order_remark = (TextView) convertView.findViewById(R.id.order_remark);
                holder.order_want_time = (TextView) convertView.findViewById(R.id.order_want_time);
                holder.order_area = (TextView) convertView.findViewById(R.id.order_area);
                holder.order_store = (TextView) convertView.findViewById(R.id.order_store);
                holder.bt = (Button) convertView.findViewById(R.id.handle_order);
                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.order_sn.setText(listItem_g.get(position).get("order_sn").toString());
            holder.third_sn.setText(listItem_g.get(position).get("third_sn").toString());
            holder.consignee.setText(listItem_g.get(position).get("consignee").toString());
            holder.mobile.setText(listItem_g.get(position).get("mobile").toString());
            holder.address.setText(listItem_g.get(position).get("address").toString());
            holder.order_remark.setText(listItem_g.get(position).get("order_remark").toString());
            holder.order_want_time.setText(listItem_g.get(position).get("order_want_time").toString());
            holder.order_area.setText(listItem_g.get(position).get("order_area").toString());
            holder.order_store.setText(listItem_g.get(position).get("order_store").toString());


            holder.bt.setClickable(true);
            //操作按钮名称
            switch (wld_type){
                case "1":
                    //已取货
                    holder.bt.setText("运输中");
                    holder.bt.setClickable(false);
                    break;
                case "2":
                    //待取货
                    holder.bt.setText("装车揽件");
                    break;
                case "3":
                    //待回仓
                    holder.bt.setText("回仓揽件");
                    break;
                case "4":
                    holder.bt.setText("确认回仓");
                    //回仓中
                    break;
                default:
                    holder.bt.setText("迷失在荒原");
                    holder.bt.setClickable(false);
            }

            /*为Button添加点击事件*/
            holder.bt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    show_toast("这里就不操作啦，你想操作的订单ID是："+order_data.list.get(position).order_id);
                    //Log.d("MyListViewBase", "你点击了按钮" + order_data.list.get(position).order_id);                                //打印Button的点击信息

                }
            });

            return convertView;
        }

    }
    /*存放控件*/
    public final class ViewHolder{
        public TextView order_sn;
        public TextView third_sn;
        public TextView consignee;
        public TextView mobile;
        public TextView address;
        public TextView order_remark;
        public TextView order_want_time;
        public TextView order_area;
        public TextView order_store;
        public Button   bt;
    }




























        /////////////////////////////////////////
    protected void load_main_overview(){
        showWaitingDialog();
        String url="http://bsc.3send.cn/index.php?s=/Api/Driver/main_overview";
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
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("token",token);
                map.put("user_id", user_id);
                return map;
            }
        };
        queue.add(request);
    }




    //处理登录后的json处理
    protected void data_json(String JSON_DATA){
        Log.d("所有订单概览:",JSON_DATA);
        overview_data_json = JSON_DATA;
        now_action = 1;
        Gson gson = new Gson();
        DriverActivity.overview_data person = gson.fromJson(JSON_DATA,DriverActivity.overview_data.class);

        if(Integer.parseInt(person.status) == 0){
            //失败
            show_toast(person.error_msg);
        }else{
            //成功
            setContentView(R.layout.activity_scrolling);

            Button submit  = (Button)findViewById(R.id.button6);    //已取货
            submit.setText("已取货:("+person.in_transit+")");

            Button submit1  = (Button)findViewById(R.id.button7);    //待取货
            submit1.setText("待取货:("+person.not_picking_up+")");

            Button submit2  = (Button)findViewById(R.id.button8);    //待回仓
            submit2.setText("待回仓:("+person.need_to_warehouse_back+")");

            Button submit3  = (Button)findViewById(R.id.button9);    //回仓中
            submit3.setText("回仓中:("+person.in_warehouse_back+")");

            TextView username  = (TextView)findViewById(R.id.textView);    //
            username.setText(contact+" [ "+phone+" ] "+logistics_name);
        }
        hideWaitingDialog();
    }

    protected void swich_wld1(View view){
        load_wld("1");
    }

    protected void swich_wld2(View view){
        load_wld("2");
    }
    protected void swich_wld3(View view){
        load_wld("3");
    }
    protected void swich_wld4(View view){
        load_wld("4");
    }



    //载入按订单类型  以物流点分组
    protected void load_wld(String type){
        showWaitingDialog();
        String url="http://bsc.3send.cn/index.php?s=/Api/Driver/prepare_list_overview";
        wld_type = type;
        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                data_json_wld(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //callback.onError();
                show_toast(error.getMessage());
                Log.d("登录错误", "异常信息-->" + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("token",token);
                map.put("user_id", user_id);
                map.put("overview_type", wld_type);
                return map;
            }
        };
        queue.add(request);
    }


    protected void data_json_wld(String JSON_DATA){
        Log.d("指定订单类型，分物流点概览",JSON_DATA);
        Gson gson = new Gson();
        wld_data = gson.fromJson(JSON_DATA,DriverActivity.wld_data.class);
        wld_data_json = JSON_DATA;
        now_action = 2;
        if(Integer.parseInt(wld_data.status) == 0){
            //失败
            show_toast(wld_data.error_msg);
        }else{
            //成功

            if(wld_data.list.size()==0){
                show_toast("无数据");
                data_json(overview_data_json);
                return;
            }


            setContentView(R.layout.list_view);
            lv = (ListView) findViewById(R.id.lv);
            /*定义一个动态数组*/
            ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();
            /*在数组中存放数据*/
            for(int i=0;i<wld_data.list.size();i++)
            {
                HashMap<String, Object> map = new HashMap<String, Object>();
                //加入图片
                map.put("name", wld_data.list.get(i).name);
                map.put("store_name",wld_data.list.get(i).store_name);
                map.put("address",wld_data.list.get(i).address);
                map.put("count",wld_data.list.get(i).count);
                listItem.add(map);
            }

            SimpleAdapter mSimpleAdapter = new SimpleAdapter(
                    this,
                    listItem,//需要绑定的数据
                    R.layout.wld_list,
                    new String[] {"name","store_name", "address", "count"},
                    new int[] {R.id.name,R.id.store_name,R.id.address,R.id.count}
            );

            lv.setAdapter(mSimpleAdapter);//为ListView绑定适配器
            hideWaitingDialog();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    show_toast("正在为你加载 [ "+wld_data.list.get(arg2).name+" ] 的订单");
                    showWaitingDialog();
                    load_order_data(arg2);
                }
            });
        }
    }
    //载入订单数据
    protected void load_order_data(int item_num){
        wld_id = wld_data.list.get(item_num).id+"";
        String action_url="";
        switch(wld_type){
            case "1":
                //待取货
                action_url="prepare_list";
                break;
            case "2":
                //已取货
                action_url="transit_list";
                break;
            case "3":
                //需回仓
                action_url="back_wdr_list";
                break;
            case "4":
                //回仓中
                action_url="back_wdr_list_ing";
                break;
            default:
                return;
        }
        String url="http://bsc.3send.cn/index.php?s=/Api/Driver/"+action_url;
        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                data_json_order(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //callback.onError();
                show_toast(error.getMessage());
                Log.d("登录错误", "异常信息-->" + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("token",token);
                map.put("user_id", user_id);
                map.put("ssc_id", wld_id);
                return map;
            }
        };
        queue.add(request);
    }
    protected void data_json_order(String JSON_DATA){
        Log.d("订单列表",JSON_DATA);
        Gson gson = new Gson();
        now_action = 3;
        order_data = gson.fromJson(JSON_DATA,DriverActivity.order_data.class);
        if(Integer.parseInt(order_data.status) == 0){
            //失败
            show_toast(order_data.error_msg);
        }else{
            //成功
            setContentView(R.layout.list_view);
            lv = (ListView) findViewById(R.id.lv);

            /*定义一个动态数组*/
            ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();
            /*在数组中存放数据*/
            for(int i=0;i<order_data.list.size();i++)
            {
                HashMap<String, Object> map = new HashMap<String, Object>();
                //加入图片
                map.put("order_state", order_data.list.get(i).order_state);
                map.put("order_store",order_data.list.get(i).order_store);
                map.put("order_area",order_data.list.get(i).order_area);
                map.put("distribution_type",order_data.list.get(i).distribution_type);
                map.put("order_want_time",order_data.list.get(i).order_want_time);
                map.put("third_sn",order_data.list.get(i).third_sn);
                map.put("order_id",order_data.list.get(i).order_id);
                map.put("order_sn",order_data.list.get(i).order_sn);
                map.put("sign_building",order_data.list.get(i).sign_building);
                map.put("consignee",order_data.list.get(i).consignee);
                map.put("mobile",order_data.list.get(i).mobile);
                map.put("order_remark",order_data.list.get(i).order_remark);
                map.put("order_time",order_data.list.get(i).order_time);
                map.put("address",order_data.list.get(i).address);
                map.put("latitude",order_data.list.get(i).latitude);
                map.put("longitude",order_data.list.get(i).longitude);
                listItem.add(map);
            }

            listItem_g = listItem;
            hideWaitingDialog();

            MyAdapter mAdapter = new MyAdapter(this);//得到一个MyAdapter对象
            lv.setAdapter(mAdapter);//为ListView绑定Adapter

//
//            SimpleAdapter mSimpleAdapter = new SimpleAdapter(
//                    this,
//                    listItem,//需要绑定的数据
//                    R.layout.order_list,
//                    new String[] {"order_sn","third_sn","consignee","mobile","address","order_remark","order_want_time","order_area","order_store"},
//                    new int[] {R.id.order_sn,R.id.third_sn,R.id.consignee,R.id.mobile,R.id.address,R.id.order_remark,R.id.order_want_time,R.id.order_area,R.id.order_store}
//            );
//
//            lv.setAdapter(mSimpleAdapter);//为ListView绑定适配器
//
//
//            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                    show_toast("还没做:"+arg3);
//                    //load_order_data(arg2);
//                }
//            });


        }
    }
    //重写返回键
    public void onBackPressed() {
        //super.onBackPressed();
        switch (now_action){
            case 3:
                data_json_wld(wld_data_json);
                break;
            case 2:
                data_json(overview_data_json);
                break;
            case 1:
                Intent intent_l  =  new Intent(this,MainActivity.class);
                startActivity(intent_l);
                super.finish();
                break;
            default:
                show_toast("还不支持其它界面的返回");
        }
    }

}
