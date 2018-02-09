package shmehdi.demo.notely;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rizz on 05-02-2018.
 */

public class MyFunction {

    public static String getCurrentDate(){
        return new SimpleDateFormat("MMM dd yyyy").format(new Date());
    }
    public static String getCurrentTime(){
        return new SimpleDateFormat("hh:mm a").format(new Date());
    }

    public static String createGist(String text){
        //text = "a new story was it is a time to take";
        if(text.length()>40){
            //create gist
            text = text.substring(0,40).trim();
        }
        Log.d("gist", text);
        return text;
    }

    public static String customDate(String date){

        if(date.equals(getCurrentDate()))
            date = "Today";
        if(date.equals(new SimpleDateFormat("MMM dd yyyy").format(yesterday())))
            date = "Yesterday";
        Log.d("customdate",date);
        return date;

    }

    private static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
}
