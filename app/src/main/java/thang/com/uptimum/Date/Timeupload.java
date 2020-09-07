package thang.com.uptimum.Date;

import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Timeupload {
    public String time(String dtStart){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date date = new Date();
        long t = 0;
        try {
            date = format.parse(dtStart);
            long old = date.getTime();
            long now = System.currentTimeMillis();
            t = now - old;
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
            if(y<ycurrentTime)return day + " thg "+monthNumber+", "+year;// 26 tháng 7,1999
            else return day + " thg "+monthNumber ;// 26 thang 7
        }
        else if(d>0) return d + " ngày";
        else if(h>0) return h + " h";
        else if(m>0) return m + " m";
        else if(s>=0 || s < 0) return "vừa xong";
        else return null;
    }
    public long NumberTime(String dtStart){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date date = new Date();
        long t = 0;
        try {
            date = format.parse(dtStart);
            long old = date.getTime();
            long now = System.currentTimeMillis();
            t = now - old;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return t;
    }
}
