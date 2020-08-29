package thang.com.uptimum.Main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.R;
import thang.com.uptimum.adapter.AdapterFriend;
import thang.com.uptimum.model.Friend;
import thang.com.uptimum.model.StatusUserLogin;
import thang.com.uptimum.network.FriendRetrofit;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.StatusUserRetrofit;

import static thang.com.uptimum.Socket.SocketIO.socket;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends Fragment {
    private static final String TAG = "FriendFragment";
    private View view;
    private RecyclerView rcvMess;
    private CircleImageView userAvataMess;
    private LinearLayoutManager linearLayoutManager;

    private NetworkUtil networkUtil;
    private Retrofit retrofit;
    private FriendRetrofit friendRetrofit;
    private ArrayList<Friend> arrFriend;
    private AdapterFriend adapterFriend;

    private StatusUserRetrofit statusUserRetrofit;
    private ArrayList<StatusUserLogin> arrStatusUser;

    private SharedPreferences sessionManagement;
    private String id ="", token = "";
    private boolean check = false;

    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_newfriend, container, false);
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        arrFriend = new ArrayList<>();
        arrStatusUser = new ArrayList<>();
        mapingView();
        getIdUserLogin();
        getStatusUser();
        getData();
        mSocketCheckStatusUser();
        return view;
    }
    private void mapingView(){
        rcvMess = (RecyclerView) view.findViewById(R.id.rcvMess);
        userAvataMess = (CircleImageView) view.findViewById(R.id.userAvatamess);

        rcvMess.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvMess.setLayoutManager(linearLayoutManager);
    }
    private void getIdUserLogin(){
        sessionManagement = getContext().getApplicationContext().getSharedPreferences("userlogin", Context.MODE_PRIVATE);
        id = sessionManagement.getString("id","");
        token = "Bearer "+sessionManagement.getString("token","");
    }
    private void mSocketCheckStatusUser(){
        socket.on("status user connect", mListenerSocket);
    }
    private Emitter.Listener mListenerSocket = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getStatusUser();
                    getData();
                }
            });
        }
    };
    private void getData(){
        friendRetrofit = retrofit.create(FriendRetrofit.class);
        Call<List<Friend>> listCall = friendRetrofit.getFriend(token, id);
        listCall.enqueue(new Callback<List<Friend>>() {
            @Override
            public void onResponse(Call<List<Friend>> call, Response<List<Friend>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "ko kết nối được", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    List<Friend> friendList = response.body();
                    arrFriend.clear();
                    Log.d(TAG, "friend "+friendList);
                    for (Friend friend : friendList){
                        arrFriend.add(friend);
                    }

                    for (int i = 0; i < arrStatusUser.size(); i++){
                        check = false;
                        for (int j = 0; j < arrFriend.size(); j++) {
                            // kiểm tra tồn tại id trong mản arrStatus trùm với arrFriend hay không
                            if(arrStatusUser.get(i).getIduser().getId().equals(arrFriend.get(j).getIdfriend().getId())){
                                Log.d(TAG, "friend "+arrFriend.get(j).getIdfriend().getUsername());
                                check = true;
                                break;
                            }
                        }
                        if(check == false) arrStatusUser.remove(i);
                    }
                    Log.d(TAG, arrStatusUser.size()+" 2 "+ arrStatusUser);
                    adapterFriend.notifyDataSetChanged();
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<List<Friend>> call, Throwable t) {
                Toast.makeText(getContext(), "ko kết nối được", Toast.LENGTH_SHORT).show();
                Log.d(TAG, " "+ t.getMessage());
                call.cancel();
            }
        });
        adapterFriend = new AdapterFriend(arrFriend, arrStatusUser, getContext().getApplicationContext());
        rcvMess.setAdapter(adapterFriend);
    }
    private void getStatusUser(){
        Log.d(TAG, " aaaaaaaaaa ");
        statusUserRetrofit = retrofit.create(StatusUserRetrofit.class);
        Call<List<StatusUserLogin>> statusUserLoginCall = statusUserRetrofit.getData(token);
        statusUserLoginCall.enqueue(new Callback<List<StatusUserLogin>>() {
            @Override
            public void onResponse(Call<List<StatusUserLogin>> call, Response<List<StatusUserLogin>> response) {
                Log.d(TAG, "abc "+ response);
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "ko kết nối được", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Log.d(TAG, "abcdef "+ response.body());
                    List<StatusUserLogin> statusUserLogins = response.body();
                    arrStatusUser.clear();
                    for (StatusUserLogin userLogin : statusUserLogins){
                        arrStatusUser.add(userLogin);
                    }
                    Log.d(TAG, arrStatusUser.size()+" aaaaaaaaaa "+ arrStatusUser);
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<List<StatusUserLogin>> call, Throwable t) {
                Log.d(TAG, "abcdef "+ t.getMessage());
                call.cancel();
            }
        });
    }
}
