package cn.edu.zwu.repairbao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zwu.repairbao.adapter.MarkInfoAdapter;
import cn.edu.zwu.repairbao.bean.MarkInfo;

/**
 * 切换功能没有实现  注意52行的注释 没有实现“完成订单界面”到“详情界面”的跳转
 * 2018年4月13日17:32:02
 * 实现切换功能
 * 2018年4月16日08:33:56
 */
public class OrderListActivity extends AppCompatActivity {

    public static final String NO_FINISH_ORDER = "no_finish_order";
    public static final String FINISH_ORDER = "finish_order";
    private List<MarkInfo> noFinishMarkInfoList = new ArrayList<>();
    private List<MarkInfo> finishMarkInfoList = new ArrayList<>();
    private Button bt_back_button;
    private Button bt_switch_button;
    private ListView listView;
    private TextView tv_title;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);
        //初始化控件
        initControl();
        //设置控件的监听器
        setListeners();
        //初始化ListView的监视器并检查意图 动态设置ListView的点击事件
        initAdapterAndCheckIntent();
        initMarkInfo();//初始化订单数据

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

        //加入未完成订单
        noFinishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "火星哈哈", "滚筒洗衣机",
                "50万 ", "2018年4月10日-2018年4月13日", "电脑爆炸了呵呵呵", "真皮沙发",
                "17899992222", null, "未维修"));
        noFinishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "M78星云", "滚筒洗衣机",
                "5000万 ", "2018年4月10日-2018年4月13日", "人造人18号", "真皮沙发",
                "17899992222", null, "未维修"));
        noFinishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "火星哈哈", "滚筒洗衣机",
                "50万 ", "2018年4月10日-2018年4月13日", "电脑爆炸了呵呵呵", "真皮沙发",
                "17899992222", null, "未维修"));
        noFinishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "M78星云", "滚筒洗衣机",
                "5000万 ", "2018年4月10日-2018年4月13日", "人造人18号", "真皮沙发",
                "17899992222", null, "未维修"));
        noFinishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "火星哈哈", "滚筒洗衣机",
                "50万 ", "2018年4月10日-2018年4月13日", "电脑爆炸了呵呵呵", "真皮沙发",
                "17899992222", null, "未维修"));
        noFinishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "M78星云", "滚筒洗衣机",
                "5000万 ", "2018年4月10日-2018年4月13日", "人造人18号", "真皮沙发",
                "17899992222", null, "未维修"));
        noFinishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "火星哈哈", "滚筒洗衣机",
                "50万 ", "2018年4月10日-2018年4月13日", "电脑爆炸了呵呵呵", "真皮沙发",
                "17899992222", null, "未维修"));
        noFinishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "M78星云", "滚筒洗衣机",
                "5000万 ", "2018年4月10日-2018年4月13日", "人造人18号", "真皮沙发",
                "17899992222", null, "未维修"));
        noFinishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "火星哈哈", "滚筒洗衣机",
                "50万 ", "2018年4月10日-2018年4月13日", "电脑爆炸了呵呵呵", "真皮沙发",
                "17899992222", null, "未维修"));
        noFinishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "M78星云", "滚筒洗衣机",
                "5000万 ", "2018年4月10日-2018年4月13日", "人造人18号", "真皮沙发",
                "17899992222", null, "未维修"));
        noFinishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "火星哈哈", "滚筒洗衣机",
                "50万 ", "2018年4月10日-2018年4月13日", "电脑爆炸了呵呵呵", "真皮沙发",
                "17899992222", null, "未维修"));
        noFinishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "M78星云", "滚筒洗衣机",
                "5000万 ", "2018年4月10日-2018年4月13日", "人造人18号", "真皮沙发",
                "17899992222", null, "未维修"));
        noFinishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "火星哈哈", "滚筒洗衣机",
                "50万 ", "2018年4月10日-2018年4月13日", "电脑爆炸了呵呵呵", "真皮沙发",
                "17899992222", null, "未维修"));
        noFinishMarkInfoList.add(new MarkInfo(32.064355, 118.787624, "M78星云", "滚筒洗衣机",
                "5000万 ", "2018年4月10日-2018年4月13日", "人造人18号", "真皮沙发",
                "17899992222", null, "未维修"));
    }

    /**
     * 初始化控件
     */
    public void initControl() {
        bt_back_button = (Button) findViewById(R.id.bt_back_button);
        bt_switch_button = (Button) findViewById(R.id.bt_switch_button);
        listView = (ListView) findViewById(R.id.lv_order_list);
        tv_title = (TextView) findViewById(R.id.tv_title);
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
                finish();
                if (type.equals(FINISH_ORDER)) {
                    actionStart(OrderListActivity.this, NO_FINISH_ORDER);
                } else if (type.equals(NO_FINISH_ORDER)) {
                    actionStart(OrderListActivity.this, FINISH_ORDER);
                }
                Toast.makeText(OrderListActivity.this, "切换", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 接受来自别的activity的意图
     *
     * @param context
     * @param type
     */
    public static void actionStart(Context context, String type) {
        Intent intent = new Intent(context, OrderListActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    /**
     * 初始化ListView的适配器并检查从别的Activity传送过来的意图以及动态设置标题上的文字
     */
    public void initAdapterAndCheckIntent() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(OrderListActivity.this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        //进行意图判断
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if (type.equals(FINISH_ORDER)) {
            tv_title.setText("已完成订单");
            setOnItemClick(finishMarkInfoList, FINISH_ORDER);
        } else if (type.equals(NO_FINISH_ORDER)) {
            tv_title.setText("未完成订单");
            setOnItemClick(noFinishMarkInfoList, NO_FINISH_ORDER);
        }
    }

    /**
     * 动态的设置ListView的数据源 以及 点击事件
     *
     * @param type
     */
    public void setOnItemClick(final List<MarkInfo> dataList, final String type) {
        MarkInfoAdapter markInfoAdapter;
        markInfoAdapter = new MarkInfoAdapter(OrderListActivity.this, R.layout.orderlist_item, dataList);
        listView.setAdapter(markInfoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MarkInfo markInfo = dataList.get(position);
                //在这里加上if判断 判断当前是什么页面 然后进行不同的意图转发
                //跳转到对应的详情界面
                DetailsActivity.actionStart(OrderListActivity.this, type, markInfo);
                Toast.makeText(OrderListActivity.this, "我是" + markInfo.getUsername(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
