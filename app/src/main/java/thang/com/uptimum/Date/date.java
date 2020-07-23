package thang.com.uptimum.Date;

import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class date {
    public static void Datepost(String dtStart){
        Log.d("aiasd", " "+ dtStart);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        try {
            Date date = format.parse(dtStart);
            long old = date.getTime();
            long now = System.currentTimeMillis();
            long kq = now - old;
            Log.d("123aweqwe", " "+ time(kq, date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private static String time(long t, Date date){
        Date currentTime = Calendar.getInstance().getTime();
        int ycurrentTime = Integer.parseInt((String) DateFormat.format("yyyy", currentTime));
        int y = Integer.parseInt((String) DateFormat.format("yyyy", date));
        Log.d("123aweqwe", " "+y);
        int d = (int) Math.floor(t / (1000 * 60 * 60 * 24));
        int h = (int) Math.floor((t % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        int m = (int) Math.floor((t % (1000 * 60 * 60)) / (1000 * 60));
        int s = (int) Math.floor((t % (1000 * 60)) / 1000);
        // lấy ngày - tháng - 5
        String day          = (String) DateFormat.format("dd",   date); // 20
        String monthNumber  = (String) DateFormat.format("MM",   date); // 06
        String year         = (String) DateFormat.format("yyyy", date); // 2013
        if(d>7){
            if(y<ycurrentTime)return day + " tháng "+monthNumber+", "+year;// 26 tháng 7,1999
            else return day + " tháng "+monthNumber ;// 26 thang 7
        }
        else if(d>0) return d + " ngày";
        else if(h>0) return h + " h";
        else if(m>0) return m + " m";
        else if(s>0) return "vừa xong";
        else return null;
    }
}
