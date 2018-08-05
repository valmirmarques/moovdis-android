package br.com.cardapia.company.acessmap;

import android.app.Application;
import android.content.Context;

/**
 * Created by tvtios-01 on 13/02/18.
 */
public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}
