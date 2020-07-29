package thang.com.uptimum.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;
import thang.com.uptimum.R;
import thang.com.uptimum.Socket.SocketIO;
import thang.com.uptimum.model.ListNotification;

import static thang.com.uptimum.Socket.SocketIO.socket;

public class MainActivity extends AppCompatActivity {
    public static ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private TabLayoutMediator tabLayoutMediator;
    private BadgeDrawable badgeDrawable;
    private int numberStatus, numberNotify, numberVideo, numberMess;
    private Toolbar toolBar;
    private ArrayList<ListNotification> listNotifications;

    private SharedPreferences sessionManagement ;
    private String iduser ;

    public static MainActivity newInstance() {

        return new MainActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SocketIO socketIO = new SocketIO();
        socketIO.ConnectSocket();
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        viewPager2 = (ViewPager2) findViewById(R.id.viewpager2);
        viewPager2.setAdapter(new ViewPagerAdapter(this));
//        viewPager2.setCurrentItem(2,false);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        listNotifications = new ArrayList<>();

        //get iduser login
        sessionManagement = MainActivity.this.getSharedPreferences("userlogin", Context.MODE_PRIVATE);
        iduser = sessionManagement.getString("id","");
        // socket
        getData();
        listenerUpdateData();
    }
    private void tabLayoutMediator(){
        tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {

            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setIcon(R.drawable.ic_home_black_24dp);
                        if(numberStatus > 0){
                            badgeDrawable = tab.getOrCreateBadge();
                            badgeDrawable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent));
                            badgeDrawable.setVisible(true);
                        };
                        break;
                    case 1:
                        tab.setIcon(R.drawable.ic_ondemand_video_black_24dp);
                        if(numberVideo > 0){
                            badgeDrawable = tab.getOrCreateBadge();
                            badgeDrawable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                            badgeDrawable.setVisible(true);
                            badgeDrawable.setMaxCharacterCount(3);
                            badgeDrawable.setNumber(17);
                        };
                        break;
                    case 2:
                        tab.setIcon(R.drawable.icons_notification_24dp);
                        if(numberNotify > 0){
                            badgeDrawable = tab.getOrCreateBadge();
                            badgeDrawable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                            badgeDrawable.setVisible(true);
                            badgeDrawable.setNumber(numberNotify);
                            badgeDrawable.setMaxCharacterCount(3);
                        }
                        break;
                    case 3:
                        tab.setIcon(R.drawable.icon_mess);
                        if(numberMess > 0){
                            badgeDrawable = tab.getOrCreateBadge();
                            badgeDrawable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                            badgeDrawable.setVisible(true);
                            badgeDrawable.setNumber(100);
                            badgeDrawable.setMaxCharacterCount(3);
                        }
                        break;
                    case 4:
                        tab.setIcon(R.drawable.ic_dehaze_black_24dp);
                        break;
                }
            }
        });
        tabLayoutMediator.attach();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @SuppressLint("WrongConstant")
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setTheme(R.style.AppThemeTwo);
                badgeDrawable = tabLayout.getTabAt(position).getOrCreateBadge();
                badgeDrawable.setVisible(false);
                AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolBar.getLayoutParams();
                if(position==0){
                    toolBar.setVisibility(View.VISIBLE);
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
                }else{
                    toolBar.setVisibility(View.GONE);
                    params.setScrollFlags(0);  // clear all scroll flags
                }
            }
        });
    }
    public void getData(){
        JSONObject iduserNotification = new JSONObject();
        try {
            iduserNotification.put("iduser", iduser);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("get all notification", iduserNotification);
        socket.on("send all notification" , getdataNotification);
    }
    private Emitter.Listener getdataNotification = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("listnotification");
                        if (jsonArray != null) {
                            listNotifications.clear();
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObjectNotify = (JSONObject) jsonArray.get(i);
                                Gson gson = new Gson();
                                ListNotification notification = gson.fromJson(String.valueOf(jsonObjectNotify), ListNotification.class);
                                if(notification.isStatus() == false && !notification.getTitle().equals("addfriend")){
                                    listNotifications.add(notification);
                                }
                            }
                        }
                        numberNotify = listNotifications.size();
                        listNotifications.clear();
                        tabLayoutMediator();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    //update
    private void listenerUpdateData(){
        socket.on("update notify android", listenerEvent);
        socket.on("send update notification" , getListenerUpdateData);
    }
    private Emitter.Listener listenerEvent = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    JSONObject Update = new JSONObject();
                    try {
                        Update.put("iduser", iduser);
                        String action = jsonObject.getString("action");
                        if(action.equals("comment")){
                            JSONArray jsonArray = jsonObject.getJSONArray("arrUserCmt");
                            for(int i = 0; i<jsonArray.length(); i++){
                                String idcmt = (String) jsonArray.get(i);
                                if(iduser.equals(idcmt)){
                                    socket.emit("get update notification", Update);
                                }
                            }
                        }else if(action.equals("likeposts")){
                            socket.emit("get update notification", Update);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    private Emitter.Listener getListenerUpdateData = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("listnotification");
                        if (jsonArray != null) {
                            listNotifications.clear();
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObjectNotify = (JSONObject) jsonArray.get(i);
                                Gson gson = new Gson();
                                ListNotification notification = gson.fromJson(String.valueOf(jsonObjectNotify), ListNotification.class);
                                if(notification.isStatus() == false && !notification.getTitle().equals("addfriend")){
                                    listNotifications.add(notification);
                                }
                            }
                        }
                        numberNotify = listNotifications.size();
                        listNotifications.clear();
                        tabLayoutMediator();
                        final MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.notification);
                        mediaPlayer.start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

}
