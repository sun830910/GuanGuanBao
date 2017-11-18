package com.enjoygreenlife.guanguanbao.model.ApiModel;

import android.content.Context;
import android.content.SharedPreferences;

import com.enjoygreenlife.guanguanbao.R;
import com.enjoygreenlife.guanguanbao.model.DataModel.UserLoginResponse;

/**
 * Created by luthertsai on 2017/11/18.
 */

public class SharedFileHandler {

    public void saveUserSession(Context context, UserLoginResponse userLoginResponse) {
        SharedPreferences pref = context.getSharedPreferences(
                context.getString(R.string.user_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user_session", userLoginResponse.getSession()); // Storing Session
        editor.putString("user_id", new String("" + userLoginResponse.getUser().getId())); // Storing UserID
        editor.commit(); // commit changes
    }

    public String retreiveUserSession(Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                context.getString(R.string.user_file_key), Context.MODE_PRIVATE);
        return pref.getString("user_session", null);
    }

    public String retreiveUserID(Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                context.getString(R.string.user_file_key), Context.MODE_PRIVATE);
        return pref.getString("user_id", null);
    }
}
