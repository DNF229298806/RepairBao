package cn.edu.zwu.repairbao.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import cn.edu.zwu.repairbao.MyApplication;

/**
 * @author Richard_Y_Wang
 * @version $Rev$
 * @des 2018/4/24
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    public static NetworkChangeReceiver n;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                Toast.makeText(context, "WIFI网络已连接，可正常使用~", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "正在使用移动网络，请注意流量的使用", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "网络连接已断开，无法正常使用应用", Toast.LENGTH_SHORT).show();
        }
        //            Toast.makeText(context, "Network Changes!", Toast.LENGTH_SHORT).show();
    }

    /**
     * 添加网络状态广播接收者
     */
    public static void addNetworkChangeReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        NetworkChangeReceiver networkChange = new NetworkChangeReceiver();
        NetworkChangeReceiver.n = networkChange;
        MyApplication.getContext().registerReceiver(networkChange, intentFilter);
    }

    public static void unregisterNetworkChangeReceiver() {
        MyApplication.getContext().unregisterReceiver(n);
    }
}

