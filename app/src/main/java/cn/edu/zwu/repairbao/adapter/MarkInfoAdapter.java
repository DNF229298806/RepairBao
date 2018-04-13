package cn.edu.zwu.repairbao.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cn.edu.zwu.repairbao.R;
import cn.edu.zwu.repairbao.bean.MarkInfo;

/**
 * @author Richard_Y_Wang
 * @version $Rev$
 * @des 2018/4/13
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class MarkInfoAdapter extends ArrayAdapter<MarkInfo> {
    private int resourceId;

    public MarkInfoAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<MarkInfo> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    /**
     * 重写方法 getView方法在每个子项被滚动到屏幕内的时候会自动被调用
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MarkInfo markInfo = getItem(position);  //获得当前项的MarkInfo实例
        View view;//convertView参数用于将之前加载好的布局进行缓存，以便可以进行重用
        ViewHolder viewHolder;
        /*
            if部分为 初始化部分 把空间储存在viewHolder中
            else部分为 重用部分 重用convertView读出viewHolder中的控件
        */
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_Username_Orderlist_Item = (TextView) view.findViewById(R.id.tv_username_orderlist_item);
            viewHolder.tv_Phone_Orderlist_Item = (TextView) view.findViewById(R.id.tv_phone_orderlist_item);
            viewHolder.tv_Repair_User_Time_Orderlist_Item = (TextView) view.findViewById(R.id.tv_repair_user_time_orderlist_item);
            viewHolder.tv_repair_loc_orderlist_item = (TextView) view.findViewById(R.id.tv_repair_loc_orderlist_item);
            view.setTag(viewHolder);//将viewHolder储存在view中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();//重新获取viewHolder
        }
        viewHolder.tv_Username_Orderlist_Item.setText(markInfo.getUsername());
        viewHolder.tv_Phone_Orderlist_Item.setText(markInfo.getPhone());
        viewHolder.tv_Repair_User_Time_Orderlist_Item.setText(markInfo.getUser_time());
        viewHolder.tv_repair_loc_orderlist_item.setText(markInfo.getRepair_loc());
        return view;
    }

    private class ViewHolder {
        TextView tv_Username_Orderlist_Item;
        TextView tv_Phone_Orderlist_Item;
        TextView tv_Repair_User_Time_Orderlist_Item;
        TextView tv_repair_loc_orderlist_item;
    }
}
