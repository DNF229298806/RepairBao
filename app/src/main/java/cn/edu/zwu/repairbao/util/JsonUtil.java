package cn.edu.zwu.repairbao.util;

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
}
