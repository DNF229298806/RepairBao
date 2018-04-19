package cn.edu.zwu.repairbao.util;

import com.google.gson.Gson;

import cn.edu.zwu.repairbao.bean.EngineerBean;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author Richard_Y_Wang
 * @version $Rev$
 * @des 2018/4/19
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class LoginUtil {
    public static RequestBody getResponseBody(String phone) {
        EngineerBean engineer = new EngineerBean(phone);
        //使用Gson 添加 依赖 compile 'com.google.code.gson:gson:2.8.1'
        Gson gson = new Gson();
        //使用Gson将对象转换为json字符串
        String json = gson.toJson(engineer);
        System.out.println(json);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return requestBody;
    }
}
