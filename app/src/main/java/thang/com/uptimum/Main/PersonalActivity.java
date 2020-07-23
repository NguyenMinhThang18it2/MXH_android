package thang.com.uptimum.Main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.Dialog.CommentBottomSheetDialog;
import thang.com.uptimum.Dialog.DialogShowImageStatus;
import thang.com.uptimum.Main.other.Personal.Personal_informationActivity;
import thang.com.uptimum.Main.other.Personal.ShowImagePersonalActivity;
import thang.com.uptimum.R;
import thang.com.uptimum.Socket.SocketIO;
import thang.com.uptimum.adapter.AdapterFriendPf;
import thang.com.uptimum.adapter.postsAdapter;
import thang.com.uptimum.model.Followers;
import thang.com.uptimum.model.Following;
import thang.com.uptimum.model.Friend;
import thang.com.uptimum.model.Posts;
import thang.com.uptimum.model.Profile;
import thang.com.uptimum.model.ProfileUser;
import thang.com.uptimum.network.FollowRetrofit;
import thang.com.uptimum.network.FriendRetrofit;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.PostsRetrofit;
import thang.com.uptimum.network.ProfileRetrofit;
import thang.com.uptimum.upload.UploadPostsActivity;

import static thang.com.uptimum.util.Constants.BASE_URL;

public class PersonalActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "PersonalActivity";

    private RecyclerView rcvFriendPf;
    private RoundedImageView roundedImageViewCover;
    private CircleImageView circleImageViewAvata, AvataPosts;
    private ImageView ChangeAvata, ChangeCoverImage;
    private TextView txtUserNameProfile, txtNickName, txtJob, txtStudies, txtStudied, txtPlaceslive, txtFrom, txtFollower,
            txtPfStatus, txtMess, txtAddFriend;
    private LinearLayout linearUserLogin, linearOtherPeople, linearProfileJob, linearProfileStudies,
            linearProfileStudied, linearProfilePlaceslive, linearProfileFrom, linearProfileFollower, linearPfShowImg, btnAddNewProfile
            , btnEditProfile;
    //thông tin user
    private SharedPreferences sessionManagement;
    private String id = "", iduserLogin ="", avata ="", coverimage ="", username="", nickname ="";
    //retrofit
    private ProfileRetrofit profileRetrofit;
    private FollowRetrofit followRetrofit;
    private FriendRetrofit friendRetrofit;
    private PostsRetrofit postsRetrofit;
    private NetworkUtil networkUtil;
    private Retrofit retrofit;

    private ArrayList<Posts> arrayPosts;
    private ArrayList<Followers> arrFollowers;
    private ArrayList<Friend> arrFriend;
    private RecyclerView rcvPostsUser;
    private LinearLayoutManager linearLayoutManager;
    private postsAdapter adapterPosts;
    private AdapterFriendPf adapterFriendPf;
    private postsAdapter.RecyclerviewClickListener listenerPosts;
    private AdapterFriendPf.OnclickRecycelListener mListenerFriend;

    private boolean checkProfile = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        SocketIO socketIO = new SocketIO();
        socketIO.ConnectSocket();
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        arrayPosts = new ArrayList<>();
        arrFollowers = new ArrayList<>();
        arrFriend = new ArrayList<>();
        setOnClickRecyclerViewListener();
        mapingView();
        getIdUser();
        getDataUser();
        getNumberFollower();
        getFriend();
        getPostsUser();
        setEvenOnClickListener();

    }
    private void mapingView(){
        roundedImageViewCover = (RoundedImageView) findViewById(R.id.roundedImageViewCover);
        circleImageViewAvata = (CircleImageView) findViewById(R.id.circleImageViewAvata);
        AvataPosts = (CircleImageView) findViewById(R.id.AvataPosts);
        ChangeAvata = (ImageView) findViewById(R.id.ChangeAvata);
        ChangeCoverImage = (ImageView) findViewById(R.id.ChangeCoverImage);
        txtUserNameProfile = (TextView) findViewById(R.id.txtUserNameProfile);
        txtNickName = (TextView) findViewById(R.id.txtNickName);
        txtPfStatus = (TextView) findViewById(R.id.txtPfStatus);
        linearUserLogin = (LinearLayout) findViewById(R.id.linearUserLogin);
        linearOtherPeople = (LinearLayout) findViewById(R.id.linearOtherPeople);
        linearPfShowImg = (LinearLayout) findViewById(R.id.linearPfShowImg);
        btnAddNewProfile = (LinearLayout) findViewById(R.id.btnAddNewProfile);
        btnEditProfile = (LinearLayout) findViewById(R.id.btnEditProfile);
        txtMess = (TextView) findViewById(R.id.txtMess);
        txtAddFriend = (TextView) findViewById(R.id.txtAddFriend);
        // thông tin
        linearProfileJob = (LinearLayout) findViewById(R.id.linearProfileJob);
        linearProfileStudies = (LinearLayout) findViewById(R.id.linearProfileStudies);
        linearProfileStudied = (LinearLayout) findViewById(R.id.linearProfileStudied);
        linearProfilePlaceslive = (LinearLayout) findViewById(R.id.linearProfilePlaceslive);
        linearProfileFrom = (LinearLayout) findViewById(R.id.linearProfileFrom);
        linearProfileFollower = (LinearLayout) findViewById(R.id.linearProfileFollower);
        txtJob = (TextView) findViewById(R.id.txtJob);
        txtStudies = (TextView) findViewById(R.id.txtStudies);
        txtStudied = (TextView) findViewById(R.id.txtStudied);
        txtPlaceslive = (TextView) findViewById(R.id.txtPlaceslive);
        txtFrom = (TextView) findViewById(R.id.txtFrom);
        txtFollower = (TextView) findViewById(R.id.txtFollower);
        // recycler friend
        rcvFriendPf = (RecyclerView) findViewById(R.id.rcvFriendPf);
        rcvFriendPf.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        rcvFriendPf.setLayoutManager(gridLayoutManager);
        // recycler posts
        rcvPostsUser = (RecyclerView) findViewById(R.id.rcvPostsUser);
        rcvPostsUser.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(PersonalActivity.this,LinearLayoutManager.VERTICAL,false);
        rcvPostsUser.setLayoutManager(linearLayoutManager);
    }
    private void setEvenOnClickListener(){
        txtPfStatus.setOnClickListener(this);
        linearPfShowImg.setOnClickListener(this);
        btnAddNewProfile.setOnClickListener(this);
        btnEditProfile.setOnClickListener(this);
    }
    private void checkProfileUser(){
        if(id.equals(iduserLogin)){
            Log.d(TAG, "null "+checkProfile);
            linearUserLogin.setVisibility(View.VISIBLE);
            if(checkProfile == false){
                btnAddNewProfile.setVisibility(View.VISIBLE);
            }else{
                btnEditProfile.setVisibility(View.VISIBLE);
            }
        }else{
            linearOtherPeople.setVisibility(View.VISIBLE);
            for (int i = 0; i < arrFriend.size(); i++){
                if(iduserLogin.equals(arrFriend.get(i).getIdfriend().getId())){
                    txtMess.setVisibility(View.VISIBLE);
                    break;
                }
            }
            txtAddFriend.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtPfStatus:
                Intent intent = new Intent(PersonalActivity.this, UploadPostsActivity.class);
                startActivity(intent);
                break;
            case R.id.linearPfShowImg:
                sendArrImg();
                break;
            case R.id.btnAddNewProfile:
                Intent intent1 = new Intent(this, Personal_informationActivity.class);
                intent1.putExtra("iduser",id);
                startActivity(intent1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            ReloadPage();
        }
    }
    private void ReloadPage(){
        Picasso.get().cancelRequest(circleImageViewAvata);
        Picasso.get().cancelRequest(roundedImageViewCover);
        circleImageViewAvata.setImageDrawable(null);
        roundedImageViewCover.setImageDrawable(null);

        getIdUser();
        getDataUser();
        getPostsUser();
        getNumberFollower();
        getFriend();
    }
    private void getIdUser(){
        Intent intent = getIntent();
        id = intent.getStringExtra("iduser");
        avata = intent.getStringExtra("avata");
        coverimage = intent.getStringExtra("coverimage");
        username = intent.getStringExtra("username");
        Log.d(TAG, " "+ id);
        sessionManagement = PersonalActivity.this.getSharedPreferences("userlogin",Context.MODE_PRIVATE);
        iduserLogin = sessionManagement.getString("id","");
    }
    private void setDataNotProfile(){
        Picasso.get().load(BASE_URL+"uploads/"+avata).resize(200,200).into(circleImageViewAvata);
        Picasso.get().load(BASE_URL+"uploads/"+coverimage).resize(400,200).into(roundedImageViewCover);
        txtUserNameProfile.setText(username);
    }
    private void getDataUser(){
        Log.d(TAG, "vào ");
        profileRetrofit = retrofit.create(ProfileRetrofit.class);
        Call<ProfileUser> profileCall = profileRetrofit.getProfile(id);
        profileCall.enqueue(new Callback<ProfileUser>() {
            @Override
            public void onResponse(Call<ProfileUser> call, Response<ProfileUser> response) {
                Log.d(TAG,"haha "+ response);
                if(!response.isSuccessful()){
                    Toast.makeText(PersonalActivity.this, "ko kết nối được", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                        ProfileUser profileUserList = response.body();
                    if(profileUserList.getId() == null){
                        checkProfile = false;
                        setDataNotProfile();
                        Log.d(TAG, "nulls "+checkProfile);
                    }else{
                        setDataProfile(profileUserList);
                    }
                    checkProfileUser();
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<ProfileUser> call, Throwable t) {
                Toast.makeText(PersonalActivity.this, "lỗi", Toast.LENGTH_SHORT).show();
                Log.d(TAG, " "+t.getMessage());
                call.cancel();
            }
        });
    }
    private void setDataProfile(ProfileUser dataProfile){
        avata = BASE_URL+"uploads/"+dataProfile.getUsers().getAvata();
        coverimage = BASE_URL+"uploads/"+dataProfile.getUsers().getCoverimage();
        username = dataProfile.getUsers().getUsername();
        nickname = "("+dataProfile.getProfile().getNickname()+")";
        Picasso.get().load(avata).resize(200,200).into(circleImageViewAvata);
        Picasso.get().load(coverimage).resize(400,200).into(roundedImageViewCover);
        txtUserNameProfile.setText(username);
        txtNickName.setText(nickname);
        // thông tin
        if(!dataProfile.getJob().equals("")){
            linearProfileJob.setVisibility(View.VISIBLE);
            String text = "Công việc <b>"+dataProfile.getJob()+"</b>";
            txtJob.setText(Html.fromHtml(text));
        }
        if(!dataProfile.getEducation().getStudies_at().equals("")){
            linearProfileStudies.setVisibility(View.VISIBLE);
            String text = "Học tại <b>"+dataProfile.getEducation().getStudies_at()+"</b>";
            txtStudies.setText(Html.fromHtml(text));
        }
        if(!dataProfile.getEducation().getStudied_at().equals("")){
            linearProfileStudied.setVisibility(View.VISIBLE);
            String text = "Học tại <b>"+dataProfile.getEducation().getStudied_at()+"</b>";
            txtStudied.setText(Html.fromHtml(text));
        }
        if(!dataProfile.getFrom().equals("")){
            linearProfileFrom.setVisibility(View.VISIBLE);
            String text = "Đến từ <b>"+dataProfile.getFrom()+"</b>";
            txtFrom.setText(Html.fromHtml(text));
        }
        if(!dataProfile.getPlaceslive().equals("")){
            linearProfilePlaceslive.setVisibility(View.VISIBLE);
            String text = "Sống tại <b>"+dataProfile.getPlaceslive()+"</b>";
            txtPlaceslive.setText(Html.fromHtml(text));
        }
    }
    private void getPostsUser(){
        postsRetrofit = retrofit.create(PostsRetrofit.class);
        Call<List<Posts>> listCallPosts = postsRetrofit.getPostsUser(id);
        listCallPosts.enqueue(new Callback<List<Posts>>() {
            @Override
            public void onResponse(Call<List<Posts>> call, Response<List<Posts>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(PersonalActivity.this, "lỗi", Toast.LENGTH_SHORT).show();
                }else{
                    arrayPosts.clear();
                    List<Posts> posts = response.body();
                    for(Posts post : posts){
                        arrayPosts.add(post);
                    }
                    adapterPosts.notifyDataSetChanged();
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<List<Posts>> call, Throwable t) {
                Toast.makeText(PersonalActivity.this, "lỗi", Toast.LENGTH_SHORT).show();
                Log.d(TAG, " "+t.getMessage());
                call.cancel();
            }
        });
        adapterPosts = new postsAdapter(arrayPosts, PersonalActivity.this, listenerPosts);
        rcvPostsUser.setAdapter(adapterPosts);
    }
    private void getNumberFollower(){
        arrFollowers.clear();
        followRetrofit = retrofit.create(FollowRetrofit.class);
        Call<List<Followers>> listCall = followRetrofit.getFollowers(id);
        listCall.enqueue(new Callback<List<Followers>>() {
            @Override
            public void onResponse(Call<List<Followers>> call, Response<List<Followers>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(PersonalActivity.this, "ko kết nối được", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    List<Followers> followersList = response.body();
                    Log.d(TAG," "+followersList);
                    for (Followers followers : followersList){
                        arrFollowers.add(followers);
                    }
                    String text = "Có <b>"+followersList.size()+"</b> người theo dõi";
                    txtFollower.setText(Html.fromHtml(text));
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<List<Followers>> call, Throwable t) {
                Toast.makeText(PersonalActivity.this, "ko kết nối được", Toast.LENGTH_SHORT).show();
                Log.d(TAG, " "+ t.getMessage());
                call.cancel();
            }
        });
    }
    private void getNumberFollowing(){
        arrFollowers.clear();
        followRetrofit = retrofit.create(FollowRetrofit.class);
        Call<List<Following>> listCall = followRetrofit.getFollowing(id);
        listCall.enqueue(new Callback<List<Following>>() {
            @Override
            public void onResponse(Call<List<Following>> call, Response<List<Following>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(PersonalActivity.this, "ko kết nối được", Toast.LENGTH_SHORT).show();
                    return;
                }else{

                    List<Following> followersList = response.body();
                    Log.d(TAG," "+followersList);
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<List<Following>> call, Throwable t) {
                Toast.makeText(PersonalActivity.this, "ko kết nối được", Toast.LENGTH_SHORT).show();
                Log.d(TAG, " "+ t.getMessage());
                call.cancel();
            }
        });
    }
    private void getFriend(){
        arrFriend.clear();
        getDataFriend();
        Log.d(TAG, "friend "+arrFriend);
        adapterFriendPf = new AdapterFriendPf(arrFriend, PersonalActivity.this, mListenerFriend);
        rcvFriendPf.setAdapter(adapterFriendPf);
    }
    private void getDataFriend(){
        friendRetrofit = retrofit.create(FriendRetrofit.class);
        Call<List<Friend>> listCall = friendRetrofit.getFriend(id);
        listCall.enqueue(new Callback<List<Friend>>() {
            @Override
            public void onResponse(Call<List<Friend>> call, Response<List<Friend>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(PersonalActivity.this, "ko kết nối được", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    List<Friend> friendList = response.body();
                    Log.d(TAG, "friend "+friendList);
                    for (Friend friend : friendList){
                        arrFriend.add(friend);
                    }
                    adapterFriendPf.notifyDataSetChanged();
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<List<Friend>> call, Throwable t) {
                Toast.makeText(PersonalActivity.this, "ko kết nối được", Toast.LENGTH_SHORT).show();
                Log.d(TAG, " "+ t.getMessage());
                call.cancel();
            }
        });
    }
    private void sendArrImg(){
        ArrayList<String> strings = new ArrayList<>();
        strings.clear();
        for (int i = 0; i < arrayPosts.size(); i++)
            for (int j = 0; j < arrayPosts.get(i).getFile().getImage().length; j++)
                strings.add(arrayPosts.get(i).getFile().getImage()[j]);
        Log.d(TAG,strings.size()+ " kaka "+strings);
        Intent showimg = new Intent(this, ShowImagePersonalActivity.class);
        showimg.putExtra("idpersonal",id);
        showimg.putStringArrayListExtra("arrPosts", (ArrayList<String>) strings);
        startActivity(showimg);
    }
    private void setOnClickRecyclerViewListener(){
        listenerPosts = new postsAdapter.RecyclerviewClickListener() {
            @Override
            public void onClickComment(RelativeLayout btnComment, int position, int typeClick) {
                CommentBottomSheetDialog commentBottomSheetDialog = new
                        CommentBottomSheetDialog(arrayPosts.get(position).getId());
                commentBottomSheetDialog.show(getSupportFragmentManager(),
                        "add_photo_dialog_fragment");
            }

            @Override
            public void showImg(ImageView imgShow, int position, int typeClick) {
                DialogShowImageStatus dialogShowImageStatus = new DialogShowImageStatus(position, arrayPosts);
                dialogShowImageStatus.show(getSupportFragmentManager(),"ShowImg_dialog_fragment");
            }
        };
        mListenerFriend = new AdapterFriendPf.OnclickRecycelListener() {
            @Override
            public void onclick(int position) {
                Toast.makeText(PersonalActivity.this, ""+arrFriend.get(position).getIdfriend().getUsername(), Toast.LENGTH_SHORT).show();
            }
        };
    }
}
