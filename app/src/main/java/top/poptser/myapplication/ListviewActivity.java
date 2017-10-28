package top.poptser.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yf on 2017/8/11.
 */

public class ListviewActivity extends Activity {

    private static final String TAG = "List View";
    private static final String[] strs = new String[]{"first", "second", "third", "fourth", "fifth"};
    private ListView lv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.list_view);
        lv = (ListView) findViewById(R.id.lv);
/*定义一个动态数组*/
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();
/*在数组中存放数据*/
        for(int i=1;i<2;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            //加入图片
            map.put("ItemTitle", "第"+i+"行");
            map.put("ItemText", "这是第"+i+"行");
            map.put("ItemImage", R.drawable.a7);
            listItem.add(map);
        }

        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this,listItem,//需要绑定的数据
                R.layout.cust_list_view,//每一行的布局
                    //动态数组中的数据源的键对应到定义布局的View中
                new String[] {"ItemImage","ItemTitle", "ItemText"},
                new int[] {R.id.ItemImage,R.id.ItemTitle,R.id.ItemText}
            );

        lv.setAdapter(mSimpleAdapter);//为ListView绑定适配器

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Toast.makeText(getApplicationContext(), "你点击了第"+arg2+"行", Toast.LENGTH_SHORT).show();
            }
        });

/*        Intent intent = getIntent();
        String message = intent.getStringExtra("EXTRA_MESSAGE");
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(message);*/
    }
}
