package cn.edu.zwu.repairbao;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * @author Richard_Y_Wang
 * @version $Rev$
 * @des 2018/4/24
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePalApplication.initialize(context);
    }

    public static Context getContext() {
        return context;
    }
}
