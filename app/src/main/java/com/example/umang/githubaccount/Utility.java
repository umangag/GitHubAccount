package com.example.umang.githubaccount;

import android.app.Activity;

/**
 * Created by umang on 19/8/16.
 */
public class Utility {
    Activity activity;

public Utility(Activity activity)
{
    this.activity=activity;
}
    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0 || null ==text || text.equals(""));
    }
    public String method(String string) {
        if (string != null && string.length() > 0 && string.charAt(string.length()-1)=='Z') {
            string = string.substring(0, string.length()-1);
        }
        return string;
    }

}
