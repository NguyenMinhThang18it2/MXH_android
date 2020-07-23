package thang.com.uptimum.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import thang.com.uptimum.R;
import thang.com.uptimum.adapter.postsAdapter;
import thang.com.uptimum.model.Posts;
import thang.com.uptimum.model.Profile;
import thang.com.uptimum.model.ProfileUser;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.PostsRetrofit;
import thang.com.uptimum.network.ProfileRetrofit;
import thang.com.uptimum.upload.UploadPostsActivity;

import static thang.com.uptimum.util.Constants.BASE_URL;

public class PersonalActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "PersonalActivity";

    private Toolbar toolBarPf;
    private RoundedImageView roundedImageViewCover;
    private CircleImageView circleImageViewAvata, AvataPosts;
    private ImageView ChangeAvata, ChangeCoverImage;
    private TextView txtUserNameProfile, txtNickName, txtJob, txtStudies, txtStudied, txtPlaceslive, txtFrom, txtFollower, txtPfStatus;
    private LinearLayout linearUserLogin, linearOtherPeople, linearProfileJob, linearProfileStudies,
            linearProfileStudied, linearProfilePlaceslive, linearProfileFrom, linearProfileFollower, linearPfShowImg;
    //thông tin user
    private SharedPreferences sessionManagement;
    private String id = "", iduserLogin ="", avata ="", coverimage ="", username="", nickname ="";
    //retrofit
    private ProfileRetrofit profileRetrofit;
    private PostsRetrofit postsRetrofit;
    private NetworkUtil networkUtil;
    private Retrofit retrofit;

    private ArrayList<Posts> arrayPosts;
    private RecyclerView rcvPostsUser;
    private LinearLayoutManager linearLayoutManager;
    private postsAdapter adapterPosts;
    private postsAdapter.RecyclerviewClickListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        arrayPosts = new ArrayList<>();
        toolBarPf = (Toolbar) findViewById(R.id.toolBarPf);
        setSupportActionBar(toolBarPf);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mapingView();
        getIdUser();
        getDataUser();
//        getNumberFollower();
//        getFriend();
        getPostsUser();
        setEvenOnClickListener();
        setOnClickRecyclerViewListener();
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
        // recycler
        rcvPostsUser = (RecyclerView) findViewById(R.id.rcvPostsUser);
        rcvPostsUser.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(PersonalActivity.this,LinearLayoutManager.VERTICAL,false);
        rcvPostsUser.setLayoutManager(linearLayoutManager);
    }
    private void setEvenOnClickListener(){
        txtPfStatus.setOnClickListener(this);
        linearPfShowImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtPfStatus:
                Intent intent = new Intent(PersonalActivity.this, UploadPostsActivity.class);
                startActivity(intent);
                break;
            case R.id.linearPfShowImg:

        }
    }
    private void getIdUser(){
        Intent intent = getIntent();
        id = intent.getStringExtra("iduser");
        Log.d(TAG, " "+ id);
        sessionManagement = PersonalActivity.this.getSharedPreferences("userlogin",Context.MODE_PRIVATE);
        iduserLogin = sessionManagement.getString("id","");

        if(id.equals(iduserLogin)){
            linearUserLogin.setVisibility(View.VISIBLE);
        }else{
            linearOtherPeople.setVisibility(View.VISIBLE);
        }
    }
    private void getDataUser(){
        Log.d(TAG, "vào ");
        profileRetrofit = retrofit.create(ProfileRetrofit.class);
        Call<ProfileUser> profileCall = profileRetrofit.getProfile("5ebcb9d7dc03951448af68ce");
        profileCall.enqueue(new Callback<ProfileUser>() {
            @Override
            public void onResponse(Call<ProfileUser> call, Response<ProfileUser> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(PersonalActivity.this, "ko kết nối được", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    ProfileUser profileUserList = response.body();
                    setDataProfile(profileUserList);
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
            txtStudies.setText(Html.fromHtml(text));
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
        adapterPosts = new postsAdapter(arrayPosts, PersonalActivity.this, listener);
        rcvPostsUser.setAdapter(adapterPosts);
    }
    private void setOnClickRecyclerViewListener(){
        listener = new postsAdapter.RecyclerviewClickListener() {
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
    }
}
