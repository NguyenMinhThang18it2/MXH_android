package thang.com.uptimum.Main.SwipeTouch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.Main.SearchActivity;
import thang.com.uptimum.R;
import thang.com.uptimum.Socket.SocketIO;
import thang.com.uptimum.adapter.ListRequetsFriendAdapter;
import thang.com.uptimum.model.Error;
import thang.com.uptimum.model.ListNotification;
import thang.com.uptimum.model.Notification;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.NotificationRetrofit;

import static thang.com.uptimum.Socket.SocketIO.socket;

public class FriendRequetsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FriendRequetsActivity";
    private Toolbar toolbar;
    private RecyclerView rcvFriendRequets;
    private ArrayList<ListNotification> listFriendReqArr;
    private ListRequetsFriendAdapter friendAdapter;
    private ImageView btnSearch;
    private ListRequetsFriendAdapter.onClickListFriend mListener;
    private SharedPreferences sessionManagement;
    private String iduser = "", token = "";
    private NetworkUtil networkUtil;
    private Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requets);
        toolbar = (Toolbar) findViewById(R.id.toolbarPersonal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        SocketIO socketIO = new SocketIO();
        socketIO.ConnectSocket();
        sessionManagement = FriendRequetsActivity.this.getSharedPreferences("userlogin", Context.MODE_PRIVATE);
        iduser = sessionManagement.getString("id","");
        token = "Bearer "+sessionManagement.getString("token","");

        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mapingView();
        setDataFriendRequest();


        onClickFriend();
    }
    private void mapingView(){
        btnSearch = (ImageView) findViewById(R.id.btnSearch);
        rcvFriendRequets = (RecyclerView) findViewById(R.id.rcvFriendRequets);

        rcvFriendRequets.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                FriendRequetsActivity.this, RecyclerView.VERTICAL , false
        );
        rcvFriendRequets.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSearch:
                Intent intent = new Intent(FriendRequetsActivity.this, SearchActivity.class);
                startActivity(intent);
        }
    }
    private void setDataFriendRequest(){
        listFriendReqArr = new ArrayList<>();
        NotificationRetrofit  notificationRetrofit = retrofit.create(NotificationRetrofit.class);
        Log.d(TAG, " iduser " + iduser);
        Call<Notification> notificationCall = notificationRetrofit.getNotification(token, iduser);
        notificationCall.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if(!response.isSuccessful()) Log.d(TAG, " lỗi response" + response);
                else {
                    listFriendReqArr.clear();
                    Notification notification = response.body();
                    Log.d(TAG, " lỗi response" + notification);
                    for (ListNotification listNotification : notification.getListNotification()){
                        if(listNotification.getTitle().equals("addfriend"))
                            listFriendReqArr.add(listNotification);
                    }
                    friendAdapter.notifyDataSetChanged();
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                Log.d(TAG, " lỗi " + t.getMessage());
                call.cancel();
            }
        });
        friendAdapter = new ListRequetsFriendAdapter(listFriendReqArr, FriendRequetsActivity.this, mListener);
        rcvFriendRequets.setAdapter(friendAdapter);
    }
    private void onClickFriend(){
        mListener = new ListRequetsFriendAdapter.onClickListFriend() {
            @Override
            public void onClickAccept(int position, LinearLayout linearButton, LinearLayout linearAccept, TextView txtTimeFriendReq, TextView txtAccept) {
                JSONObject jsonObject = new JSONObject();
                String idFriend = "";
                idFriend = listFriendReqArr.get(position).getIduserNotify().getId();
                try {
                    jsonObject.put("iduserLogin", iduser);
                    jsonObject.put("replyfriend",idFriend);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("add new friend",jsonObject);
                socket.on("add friend success", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        Handler mHandler = new Handler(Looper.getMainLooper());
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                linearButton.setVisibility(View.GONE);
                                txtTimeFriendReq.setVisibility(View.GONE);
                                linearAccept.setVisibility(View.VISIBLE);
                                txtAccept.setText("Các bạn đã trở thành bạn bè");
                            }
                        });
                    }
                });

            }

            @Override
            public void onClickDelete(int position, LinearLayout linearButton, LinearLayout linearAccept, TextView txtTimeFriendReq, TextView txtAccept) {
                NotificationRetrofit  notificationRetrofit = retrofit.create(NotificationRetrofit.class);
                Call<Error> errorCall = notificationRetrofit.deleteNotification(token, iduser, listFriendReqArr.get(position).getIduserNotify().getId());
                errorCall.enqueue(new Callback<Error>() {
                    @Override
                    public void onResponse(Call<Error> call, Response<Error> response) {
                        if(!response.isSuccessful()) Log.d(TAG, " "+ response);
                        else{
                            Error error = response.body();
                            if(!error.isSuccess()) Log.d(TAG, "xóa chưa đc "+ error);
                            else {
                                linearButton.setVisibility(View.GONE);
                                txtTimeFriendReq.setVisibility(View.GONE);
                                linearAccept.setVisibility(View.VISIBLE);
                                txtAccept.setText("Đã xóa lời mời kết bạn");
                            }
                        }
                        call.cancel();
                    }

                    @Override
                    public void onFailure(Call<Error> call, Throwable t) {
                        Log.d(TAG, "Lỗi "+ t.getMessage());
                        call.cancel();
                    }
                });
            }
        };
    }
}