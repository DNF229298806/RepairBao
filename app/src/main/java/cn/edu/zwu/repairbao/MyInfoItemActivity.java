package cn.edu.zwu.repairbao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.edu.zwu.repairbao.Interface.ActivityInitControl;

/**
 * 2018年4月17日14:58:08
 * 明天回来写fdvc和监听器 溜了溜了
 */
public class MyInfoItemActivity extends AppCompatActivity implements ActivityInitControl, View.OnClickListener {

    private TextView tv_myinfo_item;
    private Button bt_myinfo_item_back;
    private LinearLayout ll_modify_specialty;
    private RadioButton rb_electric_appliance;
    private RadioButton rb_electric_kitchen;
    private RadioButton rb_electronics;
    private RadioButton rb_others;
    private Button bt_modify_specialty;
    private EditText et_myinfo_item_introduce;
    private Button bt_modify_introduce;
    private LinearLayout ll_modify_pwd;
    private DeleteEditText det_myinfo_item_old_pwd;
    private DeleteEditText det_myinfo_item_new_pwd;
    private DeleteEditText det_myinfo_item_new_pwd_again;
    private Button bt_modify_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo_item);
    }

    @Override
    public void initControl() {
        tv_myinfo_item = (TextView) findViewById(R.id.tv_myinfo_item);
        bt_myinfo_item_back = (Button) findViewById(R.id.bt_myinfo_item_back);
        ll_modify_specialty = (LinearLayout) findViewById(R.id.ll_modify_specialty);
        rb_electric_appliance = (RadioButton) findViewById(R.id.rb_electric_appliance);
        rb_electric_kitchen = (RadioButton) findViewById(R.id.rb_electric_kitchen);
        rb_electronics = (RadioButton) findViewById(R.id.rb_electronics);
        rb_others = (RadioButton) findViewById(R.id.rb_others);
        bt_modify_specialty = (Button) findViewById(R.id.bt_modify_specialty);
        et_myinfo_item_introduce = (EditText) findViewById(R.id.et_myinfo_item_introduce);
        bt_modify_introduce = (Button) findViewById(R.id.bt_modify_introduce);
        ll_modify_pwd = (LinearLayout) findViewById(R.id.ll_modify_pwd);
        det_myinfo_item_old_pwd = (DeleteEditText) findViewById(R.id.det_myinfo_item_old_pwd);
        det_myinfo_item_new_pwd = (DeleteEditText) findViewById(R.id.det_myinfo_item_new_pwd);
        det_myinfo_item_new_pwd_again = (DeleteEditText) findViewById(R.id.det_myinfo_item_new_pwd_again);
        bt_modify_pwd = (Button) findViewById(R.id.bt_modify_pwd);
    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View v) {
        switch () {
            case :
        
                break;
        
            default:
                break;
        }
    }
}
