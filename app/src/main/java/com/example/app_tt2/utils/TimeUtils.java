package com.example.app_tt2.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    public static String formatTime(long timeMillis) {
        SimpleDateFormat format = new SimpleDateFormat(
                "HH:mm - dd/MM/yyyy",
                new Locale("vi", "VN")
        );

        return format.format(new Date(timeMillis));
    }
}