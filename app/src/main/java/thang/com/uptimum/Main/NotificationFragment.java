package thang.com.uptimum.Main;

import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import static thang.com.uptimum.Socket.SocketIO.socket;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.Date.Timeupload;
import thang.com.uptimum.Main.SwipeTouch.FriendRequetsActivity;
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
public class NotificationFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "NotificationFragment";
    private View view;
    private RecyclerView recyclerViewNotification, recyclerViewFriendReq, recyclerviewNewNotification;
    private LinearLayoutManager linearLayoutManagerNotify, linearLayoutManagerFriendReq, linearLayoutManagerNewNotify;
    private LinearLayout newNotification, AddFriendNotification, oldNotification;
    private SwipeRefreshLayout refreshNotification;
    private CircleImageView btnSearch;
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
    private friendrequestAdapter.onClickItemFriendRequest mListener;
    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
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
        onListenerClick();
        return view;
    }
    private void maping(){
        btnSearch = (CircleImageView) view.findViewById(R.id.btnSearch);
        refreshNotification = (SwipeRefreshLayout) view.findViewById(R.id.refreshNotification);
        recyclerViewNotification = (RecyclerView) view.findViewById(R.id.recyclerViewNotification);
        recyclerviewNewNotification = (RecyclerView) view.findViewById(R.id.recyclerviewNewNotification);
        recyclerViewFriendReq = (RecyclerView) view.findViewById(R.id.recyclerViewFriendReq);
        newNotification = (LinearLayout) view.findViewById(R.id.newNotification);
        AddFriendNotification = (LinearLayout) view.findViewById(R.id.AddFriendNotification);
        oldNotification = (LinearLayout) view.findViewById(R.id.oldNotification);

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

        btnSearch.setOnClickListener(this);
        recyclerViewFriendReq.setOnClickListener(this);
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
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.post(new Runnable() {
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
                        Log.d(TAG, " "+listNotificationsArr.size()+" "+listFriendReqArr.size()+" "+listNewNotify.size());
                        if(listNewNotify.size() > 0){
                            newNotification.setVisibility(View.VISIBLE);
                            notificationAdapter = new notificationAdapter(listNewNotify, getContext());
                            recyclerviewNewNotification.setAdapter(notificationAdapter);
                        }else newNotification.setVisibility(View.GONE);
                        if(listFriendReqArr.size() > 0){
                            AddFriendNotification.setVisibility(View.VISIBLE);
                            friendrequestAdapter = new friendrequestAdapter(listFriendReqArr, getContext(), mListener);
                            recyclerViewFriendReq.setAdapter(friendrequestAdapter);
                        }else AddFriendNotification.setVisibility(View.GONE);
                        if(listNotificationsArr.size() > 0){
                            oldNotification.setVisibility(View.VISIBLE);
                            notificationAdapter = new notificationAdapter(listNotificationsArr, getContext());
                            recyclerViewNotification.setAdapter(notificationAdapter);
                        }else oldNotification.setVisibility(View.GONE);
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
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.post(new Runnable() {
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

                        Log.d("qưeqweqw123123e", " lấy xuống");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(listNewNotify.size() > 0){
                        newNotification.setVisibility(View.VISIBLE);
                        notificationAdapter = new notificationAdapter(listNewNotify, getContext());
                        recyclerviewNewNotification.setAdapter(notificationAdapter);
                    }else newNotification.setVisibility(View.GONE);
                    if(listFriendReqArr.size() > 0){
                        AddFriendNotification.setVisibility(View.VISIBLE);
                        friendrequestAdapter = new friendrequestAdapter(listFriendReqArr, getContext(), mListener);
                        recyclerViewFriendReq.setAdapter(friendrequestAdapter);
                    }else AddFriendNotification.setVisibility(View.GONE);
                    if(listNotificationsArr.size() > 0){
                        oldNotification.setVisibility(View.VISIBLE);
                        notificationAdapter = new notificationAdapter(listNotificationsArr, getContext());
                        recyclerViewNotification.setAdapter(notificationAdapter);
                    }else oldNotification.setVisibility(View.GONE);
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

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.recyclerViewFriendReq:
                intent = new Intent(getContext(), FriendRequetsActivity.class);
                intent.putExtra("array", listFriendReqArr);
                startActivity(intent);
            case R.id.btnSearch:
                intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
                break;
        }
    }
    private void onListenerClick(){
        mListener = new friendrequestAdapter.onClickItemFriendRequest() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getContext(), FriendRequetsActivity.class);
                startActivity(intent);
            }
        };
    }
}
