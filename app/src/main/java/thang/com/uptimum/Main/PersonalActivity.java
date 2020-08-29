package thang.com.uptimum.Main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Application;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.JsonObject;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.Dialog.CommentBottomSheetDialog;
import thang.com.uptimum.Dialog.DialogMenuPosts;
import thang.com.uptimum.Dialog.DialogShowImageStatus;
import thang.com.uptimum.Main.other.Personal.Personal_informationActivity;
import thang.com.uptimum.Main.other.Personal.ShowImagePersonalActivity;
import thang.com.uptimum.Main.other.StatusDetail.StatusDetailActivity;
import thang.com.uptimum.R;
import thang.com.uptimum.Socket.SocketIO;
import thang.com.uptimum.adapter.AdapterFriendPf;
import thang.com.uptimum.adapter.postsAdapter;
import thang.com.uptimum.model.Error;
import thang.com.uptimum.model.Followers;
import thang.com.uptimum.model.Following;
import thang.com.uptimum.model.Friend;
import thang.com.uptimum.model.ListNotification;
import thang.com.uptimum.model.Notification;
import thang.com.uptimum.model.Posts;
import thang.com.uptimum.model.Profile;
import thang.com.uptimum.model.ProfileUser;
import thang.com.uptimum.network.FollowRetrofit;
import thang.com.uptimum.network.FriendRetrofit;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.NotificationRetrofit;
import thang.com.uptimum.network.PostsRetrofit;
import thang.com.uptimum.network.ProfileRetrofit;
import thang.com.uptimum.upload.UploadPostsActivity;

import static thang.com.uptimum.util.Constants.BASE_URL;
import static thang.com.uptimum.Socket.SocketIO.socket;

public class PersonalActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "PersonalActivity";

    private NestedScrollView nestedScrollViewPersonal;
    private RecyclerView rcvFriendPf;
    private RoundedImageView roundedImageViewCover;
    private CircleImageView circleImageViewAvata, AvataPosts;
    private ImageView ChangeAvata, ChangeCoverImage;
    private TextView txtUserNameProfile, txtNickName, txtJob, txtStudies, txtStudied, txtPlaceslive, txtFrom, txtFollower,
            txtPfStatus, txtMess, txtAddFriend, txtFriendNotifi;
    private LinearLayout linearUserLogin, linearOtherPeople, linearProfileJob, linearProfileStudies,
            linearProfileStudied, linearProfilePlaceslive, linearProfileFrom, linearProfileFollower, linearPfShowImg, btnAddNewProfile
            , btnEditProfile, OtherPeopleAdd, OtherPeopletxtMess, OtherPeopletxtNotifi;
    //thông tin user
    private SwipeRefreshLayout refreshPersonal;
    private SharedPreferences sessionManagement;
    private String id = "", iduserLogin ="", avata ="", coverimage ="", token = "";
    private String username="", nickname="", phone="", dateofbirth="",studies="", studied="", placeslive="", from="", job="";
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
    private NotificationRetrofit notificationRetrofit;
    private AdapterFriendPf adapterFriendPf;
    private ArrayList<ListNotification> arrNotifications;
    private postsAdapter.RecyclerviewClickListener listenerPosts;
    private AdapterFriendPf.OnclickRecycelListener mListenerFriend;

    private boolean checkProfile = true, checkImg = true;

    private final int PICK_AVATA_REQUEST = 123;
    private final int PICK_COVERIMG_REQUEST = 456;
    private Uri uri;
    private String realdPath = "";

    private Toolbar toolbar;
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        toolbar = (Toolbar) findViewById(R.id.toolbarPersonal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SocketIO socketIO = new SocketIO();
        socketIO.ConnectSocket();
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        arrayPosts = new ArrayList<>();
        arrFollowers = new ArrayList<>();
        arrFriend = new ArrayList<>();
        //arr này kiểm tra xem đã gửi lời mời kết bạn hay chưa
        arrNotifications = new ArrayList<>();
        setOnClickRecyclerViewListener();
        mapingView();
        getIdUser();
        getNumberFollower();
        getFriendNotification();
        getFriend();
        getDataUser();
        getPostsUser();
        setEvenOnClickListener();

    }
    private void mapingView(){
        txtFriendNotifi = (TextView) findViewById(R.id.txtFriendNotifi);
        OtherPeopletxtNotifi = (LinearLayout) findViewById(R.id.OtherPeopletxtNotifi);
        nestedScrollViewPersonal = (NestedScrollView) findViewById(R.id.nestedScrollViewPersonal);
        refreshPersonal = (SwipeRefreshLayout) findViewById(R.id.refreshPersonal);
        OtherPeopleAdd = (LinearLayout) findViewById(R.id.OtherPeopleAdd);
        OtherPeopletxtMess = (LinearLayout) findViewById(R.id.OtherPeopletxtMess);
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
    public void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

//        getFragmentManager().beginTransaction().addToBackStack(null).commit();
    }
    private void clearData(){
        rcvFriendPf.getRecycledViewPool().clear();
        rcvFriendPf.setAdapter(null);
        rcvPostsUser.getRecycledViewPool().clear();
        rcvPostsUser.setAdapter(null);

    }
    private void setEvenOnClickListener(){
        refreshPersonal.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                clearData();
                getIdUser();
                getNumberFollower();
                getFriendNotification();
                getFriend();
                getDataUser();
                getPostsUser();
                setEvenOnClickListener();
                freeMemory();
                refreshPersonal.setRefreshing(false);
            }
        });
        txtPfStatus.setOnClickListener(this);
        linearPfShowImg.setOnClickListener(this);
        btnAddNewProfile.setOnClickListener(this);
        btnEditProfile.setOnClickListener(this);
        ChangeCoverImage.setOnClickListener(this);
        ChangeAvata.setOnClickListener(this);
        OtherPeopleAdd.setOnClickListener(this);
        OtherPeopletxtMess.setOnClickListener(this);
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
            Log.d(TAG, "1111 "+ arrFriend.size());
            Log.d(TAG, "1111 "+ arrNotifications.size());
            for (int i = 0; i < arrFriend.size(); i++){
                if(iduserLogin.equals(arrFriend.get(i).getIdfriend().getId())){
                    OtherPeopletxtMess.setVisibility(View.VISIBLE);
                    Log.d(TAG, "1111 5 "+ arrFriend.get(i).getIdfriend().getId());
                    break;
                }
            }
            if(OtherPeopletxtMess.getVisibility() != View.VISIBLE) {
                for (int i = 0; i < arrNotifications.size(); i++) {
                    if (arrNotifications.get(i).getTitle().equals("addfriend")) {
                        if (iduserLogin.equals(arrNotifications.get(i).getIduserNotify().getId())) {
                            Log.d(TAG, "1111 5 " + arrNotifications.get(i).getIduserNotify().getId());
                            OtherPeopletxtNotifi.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                }
            }
            if(OtherPeopletxtMess.getVisibility() != View.VISIBLE && OtherPeopletxtNotifi.getVisibility() != View.VISIBLE){
                OtherPeopleAdd.setVisibility(View.VISIBLE);
            }
        }

    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.txtPfStatus:
                intent = new Intent(PersonalActivity.this, UploadPostsActivity.class);
                startActivity(intent);
                break;
            case R.id.linearPfShowImg:
                sendArrImg();
                break;
            case R.id.btnAddNewProfile:
                intent = new Intent(this, Personal_informationActivity.class);
                intent.putExtra("iduser",id);
                intent.putExtra("action","addnew");
                startActivity(intent);
                break;
            case R.id.ChangeAvata:
                selectAvata();
                break;
            case R.id.ChangeCoverImage:
                selectCoverImg();
                break;
            case R.id.btnEditProfile:
                intent = new Intent(this,Personal_informationActivity.class);
                putDataEdit(intent);
                startActivity(intent);
            case R.id.OtherPeopleAdd:
                addFriend();
        }
    }
    private void putDataEdit(Intent intent){
        intent.putExtra("iduser",id);
        intent.putExtra("username",username);
        intent.putExtra("nickname",nickname);
        intent.putExtra("phone",phone);
        intent.putExtra("dateofbirth",dateofbirth);
        intent.putExtra("studies",studies);
        intent.putExtra("studied",studied);
        intent.putExtra("placeslive",placeslive);
        intent.putExtra("from",from);
        intent.putExtra("job",job);
        intent.putExtra("action","edit");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data != null){
            if(requestCode == PICK_AVATA_REQUEST){
                checkImg = false;
                uri = data.getData();
                realdPath = getPathFromURI(getApplicationContext(),uri);
                Log.d(TAG, " haha "+realdPath);
                Picasso.get().load(uri).fit().centerCrop().into(circleImageViewAvata);
                uploadImgProfile("avataprofile");
            }
            if(requestCode == PICK_COVERIMG_REQUEST){
                checkImg = false;
                uri = data.getData();
                realdPath = getPathFromURI(getApplicationContext(),uri);
                Log.d(TAG, " haha "+realdPath);
                Picasso.get().load(uri).fit().centerCrop().into(roundedImageViewCover);
                uploadImgProfile("coverimgprofile");
            }
        }
        if(resultCode == 100){
            Log.d(TAG,"load lại đây");
            ReloadPage();
        }

    }
    private void ReloadPage(){
        circleImageViewAvata.setImageDrawable(null);
        roundedImageViewCover.setImageDrawable(null);

        getIdUser();
        getFriendNotification();
        getFriend();
        getDataUser();
        getPostsUser();
        getNumberFollower();
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
        token = "Bearer "+sessionManagement.getString("token","");;
    }
    private void setDataNotProfile(){
        if(checkImg == true)
            Glide.with(PersonalActivity.this).load(BASE_URL+"uploads/"+avata).fitCenter().centerCrop().into(circleImageViewAvata);
        Glide.with(PersonalActivity.this).load(BASE_URL+"uploads/"+coverimage).fitCenter().centerCrop().into(roundedImageViewCover);
        txtUserNameProfile.setText(username);
    }
    private void getDataUser(){
        Log.d(TAG, "vào ");
        profileRetrofit = retrofit.create(ProfileRetrofit.class);
        Call<ProfileUser> profileCall = profileRetrofit.getProfile(token, id);
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
        nickname = dataProfile.getProfile().getNickname();
        phone = dataProfile.getProfile().getPhone();
        dateofbirth = dataProfile.getProfile().getDateofbirth();
        studies = dataProfile.getEducation().getStudies_at();
        studied = dataProfile.getEducation().getStudied_at();
        placeslive = dataProfile.getPlaceslive();
        from = dataProfile.getFrom();
        job = dataProfile.getJob();
        if(checkImg == true)
            Glide.with(PersonalActivity.this).load(avata).fitCenter().centerCrop().into(circleImageViewAvata);
        Glide.with(PersonalActivity.this).load(coverimage).fitCenter().centerCrop().into(roundedImageViewCover);
        txtUserNameProfile.setText(username);

        // thông tin
        if(!dataProfile.getProfile().getNickname().equals("")){
            txtNickName.setVisibility(View.VISIBLE);
            String text = "("+nickname+")";
            txtNickName.setText(text);
        }
        if(!dataProfile.getJob().equals("")){
            linearProfileJob.setVisibility(View.VISIBLE);
            String text = "Công việc <b>"+job+"</b>";
            txtJob.setText(Html.fromHtml(text));
        }
        if(!dataProfile.getEducation().getStudies_at().equals("")){
            linearProfileStudies.setVisibility(View.VISIBLE);
            String text = "Học tại <b>"+studies+"</b>";
            txtStudies.setText(Html.fromHtml(text));
        }
        if(!dataProfile.getEducation().getStudied_at().equals("")){
            linearProfileStudied.setVisibility(View.VISIBLE);
            String text = "Học tại <b>"+studied+"</b>";
            txtStudied.setText(Html.fromHtml(text));
        }
        if(!dataProfile.getFrom().equals("")){
            linearProfileFrom.setVisibility(View.VISIBLE);
            String text = "Đến từ <b>"+from+"</b>";
            txtFrom.setText(Html.fromHtml(text));
        }
        if(!dataProfile.getPlaceslive().equals("")){
            linearProfilePlaceslive.setVisibility(View.VISIBLE);
            String text = "Sống tại <b>"+placeslive+"</b>";
            txtPlaceslive.setText(Html.fromHtml(text));
        }
    }
    private void getPostsUser(){
        postsRetrofit = retrofit.create(PostsRetrofit.class);
        Call<List<Posts>> listCallPosts = postsRetrofit.getPostsUser(token, id);
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
        adapterPosts = new postsAdapter(arrayPosts, PersonalActivity.this, listenerPosts, nestedScrollViewPersonal);
        rcvPostsUser.setAdapter(adapterPosts);
    }
    private void getNumberFollower(){
        arrFollowers.clear();
        followRetrofit = retrofit.create(FollowRetrofit.class);
        Call<List<Followers>> listCall = followRetrofit.getFollowers(token, id);
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
        Call<List<Following>> listCall = followRetrofit.getFollowing(token, id);
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
        Log.d(TAG, "friend aa"+arrFriend);
        adapterFriendPf = new AdapterFriendPf(arrFriend, PersonalActivity.this, mListenerFriend);
        rcvFriendPf.setAdapter(adapterFriendPf);
    }
    private void getDataFriend(){
        friendRetrofit = retrofit.create(FriendRetrofit.class);
        Call<List<Friend>> listCall = friendRetrofit.getFriend(token, id);
        listCall.enqueue(new Callback<List<Friend>>() {
            @Override
            public void onResponse(Call<List<Friend>> call, Response<List<Friend>> response) {
                Log.d(TAG, " "+ response);
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
                CommentBottomSheetDialog commentBottomSheetDialog =
                        CommentBottomSheetDialog.newInstance(arrayPosts.get(position).getId());
                commentBottomSheetDialog.show(getSupportFragmentManager(),
                        "add_photo_dialog_fragment");
            }

            @Override
            public void onclickMenu(int position) {
                boolean checkuser = false;
                if(id.equals(arrayPosts.get(position).getIduser().getId())){
                    checkuser = true;
                }else{
                    checkuser = false;
                }
                DialogMenuPosts dialogMenuPosts = new DialogMenuPosts(
                       PersonalActivity.this, arrayPosts.get(position).getId(), arrayPosts.get(position).getDocument(), checkuser
                );
                dialogMenuPosts.show(getSupportFragmentManager(),"menu posts");
            }

            @Override
            public void showImg(ImageView imgShow, int position, int typeClick) {
                DialogShowImageStatus dialogShowImageStatus = new DialogShowImageStatus(position, arrayPosts);
                dialogShowImageStatus.show(getSupportFragmentManager(),"ShowImg_dialog_fragment");
            }

            @Override
            public void showStatusDetail(String idposts) {
                Intent intent = new Intent(PersonalActivity.this, StatusDetailActivity.class);
                intent.putExtra("idposts", idposts);
                startActivity(intent);
            }

            @Override
            public void personalUser(String iduser, int position) {

            }


        };
        mListenerFriend = new AdapterFriendPf.OnclickRecycelListener() {
            @Override
            public void onclick(int position) {
                Toast.makeText(PersonalActivity.this, ""+arrFriend.get(position).getIdfriend().getUsername(), Toast.LENGTH_SHORT).show();
            }
        };
    }
    private void selectAvata(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent,"Sellect file video"), PICK_AVATA_REQUEST);
    }
    private void selectCoverImg(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent,"Sellect file video"), PICK_COVERIMG_REQUEST);
    }
    private void uploadImgProfile(String typeimg){
        File file = new File(realdPath);
        String file_path = file.getAbsolutePath();
        Log.d(TAG, " "+ file_path);
        RequestBody requestBody = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        MultipartBody.Part part = MultipartBody.Part.createFormData("image",file_path, requestBody);
        Log.d(TAG," "+part);
        profileRetrofit = retrofit.create(ProfileRetrofit.class);
        Call<Error> errorCall = profileRetrofit.postImgProfile(token, id,typeimg,part);
        errorCall.enqueue(new Callback<Error>() {
            @Override
            public void onResponse(Call<Error> call, Response<Error> response) {
                Log.d(TAG, " "+response);
                if(!response.isSuccessful()){
                    Toast.makeText(PersonalActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                }else{
                    Error errors = response.body();
                    if(!errors.isSuccess()){
                        Toast.makeText(PersonalActivity.this, "Posts Fail", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(PersonalActivity.this, "Posts new Success", Toast.LENGTH_SHORT).show();
                        Log.d(TAG," "+errors);
                        SharedPreferences.Editor editor = getSharedPreferences("userlogin", MODE_PRIVATE).edit();
                        if(typeimg.equals("avataprofile")){
                            editor.putString("avata",errors.getData());
                        }
                        else if(typeimg.equals("coverimgprofile")){
                            editor.putString("coverimage", errors.getData());
                        }
                        editor.apply();
                    }
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<Error> call, Throwable t) {
                Toast.makeText(PersonalActivity.this, "lỗi", Toast.LENGTH_SHORT).show();
                Log.d("TAG", " "+t.getMessage());
                call.cancel();
            }
        });
    }
    private void addFriend(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("iduserLogin", iduserLogin);
            jsonObject.put("friend", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("add notication friend", jsonObject);
        socket.on("add friend success", callbackAddfriend);
    }
    private void getFriendNotification(){
        Log.d(TAG, "getFriendNotification ");
        arrNotifications.clear();
        notificationRetrofit = retrofit.create(NotificationRetrofit.class);
        Call<Notification> notificationCall = notificationRetrofit.getNotification(token, id);
        notificationCall.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                Log.d(TAG, "getFriendNotification2 ");
                Log.d(TAG, "getFriendNotification "+response);
                if(!response.isSuccessful()){
                    Toast.makeText(PersonalActivity.this, "ko kết nối được", Toast.LENGTH_SHORT).show();
                    return;
                }else{

                    Notification notification = response.body();
                    for (ListNotification listNotification : notification.getListNotification()){
                        arrNotifications.add(listNotification);
                    }
                }
                checkProfileUser();
                call.cancel();
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                Log.d(TAG, "getFriendNotification lỗi "+ t.getMessage());
                call.cancel();
            }
        });
    }
    private Emitter.Listener callbackAddfriend = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    OtherPeopleAdd.setVisibility(View.GONE);
                    OtherPeopletxtNotifi.setVisibility(View.VISIBLE);
                }
            });
        }
    };
    public static String getPathFromURI(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
