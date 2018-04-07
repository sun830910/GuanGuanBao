package com.enjoygreenlife.guanguanbao.model.ApiModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.enjoygreenlife.guanguanbao.R;

/**
 * Created by luthertsai on 2017/11/18.
 */

public class SharedFileHandler {

    public void saveUserSession(Context context, String session, String userID) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(
                context.getString(R.string.user_file_key), Context.MODE_PRIVATE);
//        System.out.println(context + "---SESSION+SAVE=>> " + session);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user_session", session); // Storing Session
        editor.putString("user_id", userID); // Storing UserID
        editor.commit(); // commit changes
    }

    public String retreiveUserSession(Context context) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(
                context.getString(R.string.user_file_key), Context.MODE_PRIVATE);
//        System.out.println(context + "---SESSION+OUT>>> " + pref.getString("user_session", ""));
        return pref.getString("user_session", "");
    }

    public String retreiveUserID(Context context) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(
                context.getString(R.string.user_file_key), Context.MODE_PRIVATE);
        return pref.getString("user_id", null);
    }
}
