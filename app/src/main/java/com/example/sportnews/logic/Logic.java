package com.example.sportnews.logic;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Adapter;

import androidx.annotation.NonNull;

import com.example.sportnews.pojo.Author;
import com.example.sportnews.pojo.Post;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logic {
    public static String parseDate(String time,String outputPattern) {
        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch ( ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static Post MackPost(String type, String publishedAt, String url, String title, String sectionName, Author author){
        String Date= Logic.parseDate(publishedAt.replace("Z",""),"MMM dd, yyyy");
        String time=Logic.parseDate(publishedAt.replace("Z",""),"h:mm a");
        return new Post(type,title,url,sectionName,Date+"-"+time,author);
    }

    public static Boolean isNetworkAvailable(Application application) {
        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Network nw = connectivityManager.getActiveNetwork();
            if (nw == null) return false;
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
            return actNw != null &&
                    (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        } else {
            NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
            return nwInfo != null && nwInfo.isConnected();
        }
    }
}
