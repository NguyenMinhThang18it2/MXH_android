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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.R;
import thang.com.uptimum.adapter.AdapterFriend;
import thang.com.uptimum.model.Friend;
import thang.com.uptimum.network.FriendRetrofit;
import thang.com.uptimum.network.NetworkUtil;


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

    private SharedPreferences sessionManagement;
    private String id ="", token = "";
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
        mapingView();
        getIdUserLogin();
        getData();
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
    private void getData(){
        friendRetrofit = retrofit.create(FriendRetrofit.class);
        Call<List<Friend>> listCall = friendRetrofit.getFriend(token, id);
        listCall.enqueue(new Callback<List<Friend>>() {
            @Override
            public void onResponse(Call<List<Friend>> call, Response<List<Friend>> response) {
                Log.d(TAG, " "+ response);
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
        adapterFriend = new AdapterFriend(arrFriend, getContext().getApplicationContext());
        rcvMess.setAdapter(adapterFriend);
    }
}
