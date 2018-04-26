package cn.edu.zwu.repairbao.Util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Richard_Y_Wang
 * @version $Rev$
 * @des 2018/4/11
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class PermissionUtil {

    static List<String> permissionList = new ArrayList<>();

    public static void addPermission(Context context, String[] arr_permission) {

        for (int i = 0; i < arr_permission.length; i++) {
            if (ContextCompat.checkSelfPermission(context, arr_permission[i]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(arr_permission[i]);
            }
        }
    }

    public static boolean checkPermission(Activity activity) {
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(activity, permissions, 1);
            return false;
        }
        return true;
    }
}
