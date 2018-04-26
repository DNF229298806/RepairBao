package cn.edu.zwu.repairbao.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import cn.edu.zwu.repairbao.Gson.Engineer;
import cn.edu.zwu.repairbao.Util.EncryptAndDecrypt.AESUtils;

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
        String decryStr = AESUtils.decrypt(AESUtils.secretKey, json);
        System.out.println( "AES解密后json数据 ---->" + decryStr);
        return decryStr;
    }

    public static void saveJson(Context context,String json){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        //进行加密操作
        System.out.println( "AES加密前json数据长度 ---->" + json.length());
        //生成一个动态key
        AESUtils.generateKey();
        System.out.println( "AES动态secretKey ---->" + AESUtils.secretKey);
        //AES加密
        String encryStr = AESUtils.encrypt(AESUtils.secretKey, json);
        System.out.println( "AES加密后json数据 ---->" + encryStr);
        System.out.println( "AES加密后json数据长度 ---->" + encryStr.length());
        editor.putString("engineer", encryStr);
        editor.apply();
    }
}
