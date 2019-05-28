package id.rumahawan.manajemenkomplain.Data;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    private static final String SHARED_PREFERENCES = "shared_preferences";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public Session(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setSessionBoolean(String key, Boolean value){
        editor.putBoolean(key, value);
        editor.commit();
    }
    public Boolean getSessionBoolean(String key){
        return sharedPreferences.getBoolean(key, false);
    }


    public void setSessionString(String key, String value){
        editor.putString(key, value);
        editor.commit();
    }
    public String getSessionString(String key){
        return sharedPreferences.getString(key, null);
    }

    public void setSessionInt(String key, int value){
        editor.putInt(key, value);
        editor.commit();
    }
    public int getSessionInt(String key){
        return sharedPreferences.getInt(key, 0);
    }

    public void removeSession(String key){
        editor.remove(key);
        editor.commit();
    }

    public boolean isExist(String key){
        return sharedPreferences.contains(key);
    }

}