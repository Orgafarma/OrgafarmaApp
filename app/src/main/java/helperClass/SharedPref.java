package helperClass;

import android.content.Context;
import android.content.SharedPreferences;
public class SharedPref {
    public static final String PREF_ID = "OrgafarmaApp";

    public static void setString(Context ctx, String key, String value){
        SharedPreferences pref = ctx.getSharedPreferences(PREF_ID, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context ctx, String key){
        SharedPreferences pref = ctx.getSharedPreferences(PREF_ID, 0);
        String returner = pref.getString(key, "default");
        return returner;
    }

    public static int getInt(Context ctx, String key){
        SharedPreferences pref = ctx.getSharedPreferences(PREF_ID, 0);
        int returner = pref.getInt(key, 0);
        return returner;
    }

    public static void setInt(Context ctx, String key, int value) {
        SharedPreferences pref = ctx.getSharedPreferences(PREF_ID, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void setBoolean(Context ctx, String key, boolean value){
        SharedPreferences pref = ctx.getSharedPreferences(PREF_ID, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context ctx, String key){
        SharedPreferences pref = ctx.getSharedPreferences(PREF_ID, 0);
        boolean returner = pref.getBoolean(key, false);
        return returner;
    }

    public static void clear(Context ctx)
    {
        SharedPreferences pref = ctx.getSharedPreferences(PREF_ID, 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.clear();
        edit.commit();
    }

}
