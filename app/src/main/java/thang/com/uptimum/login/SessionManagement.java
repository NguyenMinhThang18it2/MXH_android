package thang.com.uptimum.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import thang.com.uptimum.model.Users;

public class SessionManagement {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String SHARED_PREF_NAME = "userlogin";
    private String SESSION_KEY = "session_key";


    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void saveSession(Users users){
        int idlogin = 1;
        editor.putInt(SESSION_KEY,idlogin).apply();
    }
    public int getSession(){
        return sharedPreferences.getInt(SESSION_KEY,-1);
    }
    public void removeSession(){
        editor.putInt(SESSION_KEY, -1).apply();
        editor.clear().apply();
    }
}
