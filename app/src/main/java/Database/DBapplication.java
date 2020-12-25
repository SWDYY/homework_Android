package Database;

import android.app.Application;

public class DBapplication extends Application {
    private static final database db=new database("172.20.6.219:80");
    public database getDB(){
        return db;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
