package cn.edu.zwu.repairbao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zwu.repairbao.adapter.MarkInfoAdapter;
import cn.edu.zwu.repairbao.bean.MarkInfo;

/**
 * 切换功能没有实现  注意52行的注释 没有实现“完成订单界面”到“详情界面”的跳转
 * 2018年4月13日17:32:02
 */
public class OrderListActivity extends AppCompatActivity {

    public static final String NO_FINISH_ORDER = "no_finish_order";
    public static final String FINISH_ORDER = "finish_order";
    private String[] data = {"Apple", "Banana", "Orange", "Watermelon", "Pear", "Grape", "Pineapple",
            "Strawberry", "Cherry", "Mango", "Apple", "Banana", "Orange", "Watermelon", "Pear", "Grape", "Pineapple",
            "Strawberry", "Cherry", "Mango"};
    private List<MarkInfo> noFinishMarkInfoList = new ArrayList<>();
    private List<MarkInfo> finishMarkInfoList = new ArrayList<>();
    private Button bt_back_button;
    private Button bt_switch_button;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);
        //初始化控件
        initControl();
        //设置控件的监听器
        setListeners();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(OrderListActivity.this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
        initMarkInfo();//初始化订单数据
        //fruitList???这个是什么 回来该 估计是换成noFinishMarkInfoList 或 finishMarkInfoList
        MarkInfoAdapter markInfoAdapter = new MarkInfoAdapter(OrderListActivity.this, R.layout.orderlist_item, finishMarkInfoList);
        listView.setAdapter(markInfoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MarkInfo markInfo = finishMarkInfoList.get(position);
                //在这里加上if判断 判断当前是什么页面 然后进行不同的意图转发
                //跳转到对应的详情界面
                DetailsActivity.actionStart(OrderListActivity.this, NO_FINISH_ORDER);
                Toast.makeText(OrderListActivity.this, "我是" + markInfo.getUsername(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initMarkInfo() {
        finishMarkInfoList.add(new MarkInfo(32.079254, 118.787623, "地点", "类型",
                "报价", "维修时间", "报修信息",
                "用户姓名", "手机", null, "已维修"));
        finishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "火星哈哈", "修电脑",
                "50万 ", "2018年4月10日-2018年4月13日", "电脑爆炸了呵呵呵", "石乐志",
                "17899992222", null, "已维修"));
        finishMarkInfoList.add(new MarkInfo(32.079254, 118.787623, "地点", "类型",
                "报价", "维修时间", "报修信息",
                "用户姓名", "手机", null, "已维修"));
        finishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "火星哈哈", "修电脑",
                "50万 ", "2018年4月10日-2018年4月13日", "电脑爆炸了呵呵呵", "石乐志",
                "17899992222", null, "已维修"));
        finishMarkInfoList.add(new MarkInfo(32.079254, 118.787623, "地点", "类型",
                "报价", "维修时间", "报修信息",
                "用户姓名", "手机", null, "已维修"));
        finishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "火星哈哈", "修电脑",
                "50万 ", "2018年4月10日-2018年4月13日", "电脑爆炸了呵呵呵", "石乐志",
                "17899992222", null, "已维修"));
        finishMarkInfoList.add(new MarkInfo(32.079254, 118.787623, "地点", "类型",
                "报价", "维修时间", "报修信息",
                "用户姓名", "手机", null, "已维修"));
        finishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "火星哈哈", "修电脑",
                "50万 ", "2018年4月10日-2018年4月13日", "电脑爆炸了呵呵呵", "石乐志",
                "17899992222", null, "已维修"));
        finishMarkInfoList.add(new MarkInfo(32.079254, 118.787623, "地点", "类型",
                "报价", "维修时间", "报修信息",
                "用户姓名", "手机", null, "已维修"));
        finishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "火星哈哈", "修电脑",
                "50万 ", "2018年4月10日-2018年4月13日", "电脑爆炸了呵呵呵", "石乐志",
                "17899992222", null, "已维修"));
        finishMarkInfoList.add(new MarkInfo(32.079254, 118.787623, "地点", "类型",
                "报价", "维修时间", "报修信息",
                "用户姓名", "手机", null, "已维修"));
        finishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "火星哈哈", "修电脑",
                "50万 ", "2018年4月10日-2018年4月13日", "电脑爆炸了呵呵呵", "石乐志",
                "17899992222", null, "已维修"));
        finishMarkInfoList.add(new MarkInfo(32.079254, 118.787623, "地点", "类型",
                "报价", "维修时间", "报修信息",
                "用户姓名", "手机", null, "已维修"));
        finishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "火星哈哈", "修电脑",
                "50万 ", "2018年4月10日-2018年4月13日", "电脑爆炸了呵呵呵", "石乐志",
                "17899992222", null, "已维修"));
        finishMarkInfoList.add(new MarkInfo(32.079254, 118.787623, "地点", "类型",
                "报价", "维修时间", "报修信息",
                "用户姓名", "手机", null, "已维修"));
        finishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "火星哈哈", "修电脑",
                "50万 ", "2018年4月10日-2018年4月13日", "电脑爆炸了呵呵呵", "石乐志",
                "17899992222", null, "已维修"));
        finishMarkInfoList.add(new MarkInfo(32.079254, 118.787623, "地点", "类型",
                "报价", "维修时间", "报修信息",
                "用户姓名", "手机", null, "已维修"));
        finishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "火星哈哈", "修电脑",
                "50万 ", "2018年4月10日-2018年4月13日", "电脑爆炸了呵呵呵", "石乐志",
                "17899992222", null, "已维修"));
    }

    /**
     * 初始化控件
     */
    public void initControl() {
        bt_back_button = (Button) findViewById(R.id.bt_back_button);
        bt_switch_button = (Button) findViewById(R.id.bt_switch_button);
        listView = (ListView) findViewById(R.id.lv_order_list);
    }

    /**
     * 设置按钮的监听事件
     */
    public void setListeners() {
        bt_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderListActivity.this.finish();
            }
        });

        bt_switch_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OrderListActivity.this, "切换", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
