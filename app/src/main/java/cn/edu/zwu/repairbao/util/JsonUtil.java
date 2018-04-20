package cn.edu.zwu.repairbao.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import cn.edu.zwu.repairbao.gson.Engineer;

/**
 * @author Richard_Y_Wang
 * @version $Rev$
 * @des 2018/4/19
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class JsonUtil {
    public static Engineer handleEngineerResponse(String response) {
        return new Gson().fromJson(response, Engineer.class);
    }

    public static String getJson(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString("engineer", null);
        return json;
    }

    public static void saveJson(Context context,String json){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("engineer", json);
        editor.apply();
    }
}
