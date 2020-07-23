package thang.com.uptimum.Main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import static thang.com.uptimum.Socket.SocketIO.socket;

import io.socket.emitter.Emitter;
import thang.com.uptimum.R;
import thang.com.uptimum.adapter.friendrequestAdapter;
import thang.com.uptimum.adapter.notificationAdapter;
import thang.com.uptimum.model.ListNotification;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    private View view;
    private RecyclerView recyclerViewNotification, recyclerViewFriendReq;
    private LinearLayoutManager linearLayoutManagerNotify, linearLayoutManagerFriendReq;
    // notification
    private ArrayList<ListNotification> listNotificationsArr;
    private notificationAdapter notificationAdapter;
    // friend reqest
    private ArrayList<ListNotification> listFriendReqArr;
    private friendrequestAdapter friendrequestAdapter;

    private SharedPreferences sessionManagement ;
    private String iduser ;
    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerViewNotification = (RecyclerView) view.findViewById(R.id.recyclerViewNotification);
        recyclerViewFriendReq = (RecyclerView) view.findViewById(R.id.recyclerViewFriendReq);
        recyclerViewFriendReq.setHasFixedSize(true);
        recyclerViewNotification.setHasFixedSize(true);
        linearLayoutManagerNotify =  new LinearLayoutManager
                (getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManagerFriendReq = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewNotification.setLayoutManager(linearLayoutManagerNotify);
        recyclerViewFriendReq.setLayoutManager(linearLayoutManagerFriendReq);

        listNotificationsArr = new ArrayList<>();
        listFriendReqArr = new ArrayList<>();

        sessionManagement = getContext().getApplicationContext().getSharedPreferences("userlogin", Context.MODE_PRIVATE);
        iduser = sessionManagement.getString("id","");
        getData();
        listenerUpdateData();
        return view;
    }
    private void getData(){
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
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObjectNotify = (JSONObject) jsonArray.get(i);
                                Gson gson = new Gson();
                                ListNotification notification = gson.fromJson(String.valueOf(jsonObjectNotify), ListNotification.class);
                                if(notification.getTitle().equals("addfriend")){
                                    listFriendReqArr.add(notification);
                                }else{
                                    listNotificationsArr.add(notification);
                                }
                            }
                        }
                        Collections.reverse(listNotificationsArr);
                        Collections.reverse(listFriendReqArr);

                        notificationAdapter = new notificationAdapter(listNotificationsArr, getContext());
                        recyclerViewNotification.setAdapter(notificationAdapter);

                        friendrequestAdapter = new friendrequestAdapter(listFriendReqArr, getContext());
                        recyclerViewFriendReq.setAdapter(friendrequestAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
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
                            listNotificationsArr.clear();
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObjectNotify = (JSONObject) jsonArray.get(i);
                                Gson gson = new Gson();
                                ListNotification notification = gson.fromJson(String.valueOf(jsonObjectNotify), ListNotification.class);
                                listNotificationsArr.add(notification);
                            }
                        }
                        Collections.reverse(listNotificationsArr);
                        notificationAdapter.notifyDataSetChanged();
                        notificationAdapter = new notificationAdapter(listNotificationsArr, getContext().getApplicationContext());
                        recyclerViewNotification.setAdapter(notificationAdapter);
                        Log.d("qưeqweqw123123e", " lấy xuống");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
}
