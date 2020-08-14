package thang.com.uptimum.Main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import static thang.com.uptimum.Socket.SocketIO.socket;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.Date.Timeupload;
import thang.com.uptimum.R;
import thang.com.uptimum.adapter.friendrequestAdapter;
import thang.com.uptimum.adapter.notificationAdapter;
import thang.com.uptimum.model.Error;
import thang.com.uptimum.model.ListNotification;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.NotificationRetrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    private static final String TAG = "NotificationFragment";
    private View view;
    private RecyclerView recyclerViewNotification, recyclerViewFriendReq, recyclerviewNewNotification;
    private LinearLayoutManager linearLayoutManagerNotify, linearLayoutManagerFriendReq, linearLayoutManagerNewNotify;
    private SwipeRefreshLayout refreshNotification;
    // notification
    private ArrayList<ListNotification> listNotificationsArr;
    private notificationAdapter notificationAdapter;
    // friend reqest
    private ArrayList<ListNotification> listFriendReqArr;
    private friendrequestAdapter friendrequestAdapter;
    //new Notification
    private ArrayList<ListNotification> listNewNotify;

    private SharedPreferences sessionManagement ;
    private String iduser="", token="" ;

    private Timeupload timeupload;
    private JSONObject iduserNotification;

    private NetworkUtil networkUtil;
    private Retrofit retrofit;
    private NotificationRetrofit notificationRetrofit;
    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_notification, container, false);

        timeupload = new Timeupload();
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        maping();
        eventListener();
        getData();
        listenerUpdateData();
        return view;
    }
    private void maping(){
        refreshNotification = (SwipeRefreshLayout) view.findViewById(R.id.refreshNotification);
        recyclerViewNotification = (RecyclerView) view.findViewById(R.id.recyclerViewNotification);
        recyclerviewNewNotification = (RecyclerView) view.findViewById(R.id.recyclerviewNewNotification);
        recyclerViewFriendReq = (RecyclerView) view.findViewById(R.id.recyclerViewFriendReq);
        recyclerviewNewNotification.setHasFixedSize(true);
        recyclerViewFriendReq.setHasFixedSize(true);
        recyclerViewNotification.setHasFixedSize(true);
        linearLayoutManagerNotify =  new LinearLayoutManager
                (getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManagerNewNotify = new LinearLayoutManager
                (getContext(),LinearLayoutManager.VERTICAL,false);
        linearLayoutManagerFriendReq = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewNotification.setLayoutManager(linearLayoutManagerNotify);
        recyclerViewFriendReq.setLayoutManager(linearLayoutManagerFriendReq);
        recyclerviewNewNotification.setLayoutManager(linearLayoutManagerNewNotify);

        listNotificationsArr = new ArrayList<>();
        listFriendReqArr = new ArrayList<>();
        listNewNotify = new ArrayList<>();

        sessionManagement = getContext().getApplicationContext().getSharedPreferences("userlogin", Context.MODE_PRIVATE);
        iduser = sessionManagement.getString("id","");
        token = "Bearer "+sessionManagement.getString("token","");
    }
    private void eventListener(){
        refreshNotification.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                clearData();
                readAll();// update đánh dấu tất cả đã đọc
                socket.emit("get all notification", iduserNotification);
                refreshNotification.setRefreshing(false);
            }
        });
    }
    private void clearData(){
        recyclerViewFriendReq.getRecycledViewPool().clear();
        recyclerViewFriendReq.setAdapter(null);
        recyclerViewNotification.getRecycledViewPool().clear();
        recyclerViewNotification.setAdapter(null);
        recyclerviewNewNotification.getRecycledViewPool().clear();
        recyclerviewNewNotification.setAdapter(null);
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }
    private void getData(){
        iduserNotification = new JSONObject();
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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    listNewNotify.clear();
                    listFriendReqArr.clear();
                    listNotificationsArr.clear();
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("listnotification");
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectNotify = (JSONObject) jsonArray.get(i);
                                Gson gson = new Gson();
                                ListNotification notification = gson.fromJson(String.valueOf(jsonObjectNotify), ListNotification.class);
                                if (notification.getTitle().equals("addfriend")) {
                                    listFriendReqArr.add(notification);
                                } else {
                                    if (timeupload.NumberTime(notification.getCreateAt()) < 3600000) {
                                        listNewNotify.add(notification);
                                    } else {
                                        listNotificationsArr.add(notification);
                                    }
                                }
                            }
                        }
                        Collections.reverse(listNewNotify);
                        Collections.reverse(listNotificationsArr);
                        Collections.reverse(listFriendReqArr);

                        notificationAdapter = new notificationAdapter(listNotificationsArr, getContext());
                        recyclerViewNotification.setAdapter(notificationAdapter);

                        friendrequestAdapter = new friendrequestAdapter(listFriendReqArr, getContext());
                        recyclerViewFriendReq.setAdapter(friendrequestAdapter);

                        notificationAdapter = new notificationAdapter(listNewNotify, getContext());
                        recyclerviewNewNotification.setAdapter(notificationAdapter);
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
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.post(new Runnable() {
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
                        }else { //if(action.equals("likeposts"))
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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    listNewNotify.clear();
                    listFriendReqArr.clear();
                    listNotificationsArr.clear();
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("listnotification");
                        if (jsonArray != null) {
                            listNotificationsArr.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectNotify = (JSONObject) jsonArray.get(i);
                                Gson gson = new Gson();
                                ListNotification notification = gson.fromJson(String.valueOf(jsonObjectNotify), ListNotification.class);
                                if (notification.getTitle().equals("addfriend")) {
                                    listFriendReqArr.add(notification);
                                } else {
                                    if (timeupload.NumberTime(notification.getCreateAt()) < 3600000) {
                                        listNewNotify.add(notification);
                                    } else {
                                        listNotificationsArr.add(notification);
                                    }
                                }
                            }
                        }
                        Collections.reverse(listNewNotify);
                        Collections.reverse(listNotificationsArr);
                        Collections.reverse(listFriendReqArr);

                        notificationAdapter.notifyDataSetChanged();
                        friendrequestAdapter.notifyDataSetChanged();
                        notificationAdapter.notifyDataSetChanged();

                        notificationAdapter = new notificationAdapter(listNotificationsArr, getContext().getApplicationContext());
                        recyclerViewNotification.setAdapter(notificationAdapter);

                        friendrequestAdapter = new friendrequestAdapter(listFriendReqArr, getContext());
                        recyclerViewFriendReq.setAdapter(friendrequestAdapter);

                        notificationAdapter = new notificationAdapter(listNewNotify, getContext());
                        recyclerviewNewNotification.setAdapter(notificationAdapter);
                        Log.d("qưeqweqw123123e", " lấy xuống");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    private void readAll(){
        notificationRetrofit = retrofit.create(NotificationRetrofit.class);
        Call<Error> errorCall = notificationRetrofit.readAll(token, iduser);
        errorCall.enqueue(new Callback<Error>() {
            @Override
            public void onResponse(Call<Error> call, Response<Error> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "lỗi rác story", Toast.LENGTH_SHORT).show();
                    return;
                }
                Error error = response.body();
                if(!error.isSuccess()){
                    Toast.makeText(getContext(), "read all faile", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "read all success", Toast.LENGTH_SHORT).show();
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<Error> call, Throwable t) {
                Toast.makeText(getContext(), "lỗi", Toast.LENGTH_SHORT).show();
                Log.d(TAG," "+ t.getMessage());
                call.cancel();
            }
        });
    }
}
