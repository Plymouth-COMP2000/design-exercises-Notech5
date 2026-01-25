package com.example.restaurantapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class NotificationStore {

    private static final String PREFS = "pending_notifications";

    public static void add(Context context, String userId, String message) {
        SharedPreferences prefs =
                context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        Set<String> messages =
                new HashSet<>(prefs.getStringSet(userId, new HashSet<>()));

        messages.add(message);
        prefs.edit().putStringSet(userId, messages).apply();
    }

    public static Set<String> consume(Context context, String userId) {
        SharedPreferences prefs =
                context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        Set<String> messages = prefs.getStringSet(userId, new HashSet<>());
        prefs.edit().remove(userId).apply(); // show once
        return messages;
    }
}