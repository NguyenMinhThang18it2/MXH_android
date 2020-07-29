package thang.com.uptimum.Main.other.StatusDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.Date.Timeupload;
import thang.com.uptimum.Dialog.adapterDialog.AdapterDialogShowImgPersonal;
import thang.com.uptimum.R;
import thang.com.uptimum.model.Posts;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.PostsRetrofit;

import static thang.com.uptimum.util.Constants.BASE_URL;

public class StatusDetailActivity extends AppCompatActivity {
    private static final String TAG = "StatusDetailActivity";
    private Toolbar toolbar;
    private CircleImageView Avatauser;
    private TextView txtusername, txtTimeUpload, txtLike, txtCmt, txtdocument, textStatusBacground;
    private ImageView menu;
    private RelativeLayout btnLike, btnComment;
    private ViewPager2 viewpagerStatusDetail;
    private String idposts = "";

    private NetworkUtil networkUtil;
    private Retrofit retrofit;
    private PostsRetrofit postsRetrofit;
    private Timeupload timeupload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        timeupload = new Timeupload();

        Intent intent = getIntent();
        idposts = intent.getStringExtra("idposts");
        mapingView();
        getToolBar();
        getDataStatus();
    }
    private void getToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbarStatusDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bài viết");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void mapingView(){
        Avatauser = (CircleImageView) findViewById(R.id.Avatauser);
        txtusername = (TextView) findViewById(R.id.txtusername);
        txtTimeUpload = (TextView) findViewById(R.id.txtTimeUpload);
        txtLike = (TextView) findViewById(R.id.txtLike);
        txtCmt = (TextView) findViewById(R.id.txtCmt);
        txtdocument = (TextView) findViewById(R.id.txtdocument);
        textStatusBacground = (TextView) findViewById(R.id.textStatusBacground);
        menu = (ImageView) findViewById(R.id.menu);
        btnLike = (RelativeLayout) findViewById(R.id.btnLike);
        btnComment = (RelativeLayout) findViewById(R.id.btnComment);
        viewpagerStatusDetail = (ViewPager2) findViewById(R.id.viewpagerStatusDetail);
    }
    private void getDataStatus(){
        postsRetrofit = retrofit.create(PostsRetrofit.class);
        Call<Posts> postsCall = postsRetrofit.getPostsDetail(idposts);
        postsCall.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(StatusDetailActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                }else{
                    Posts posts = response.body();
                    setData(posts);
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                Toast.makeText(StatusDetailActivity.this, "lỗi", Toast.LENGTH_SHORT).show();
                Log.d(TAG," "+t.getMessage());
                call.cancel();
            }
        });
    }
    private void setData(Posts posts){
        Picasso.get().load(BASE_URL+"uploads/"+posts.getIduser().getAvata()).resize(100,100).into(Avatauser);
        txtusername.setText(posts.getIduser().getUsername());
        txtTimeUpload.setText(timeupload.time(posts.getCreatedAt()));
        txtLike.setText(String.valueOf(posts.getLike().length));
        ArrayList<String> strings = new ArrayList<>();
        for(int i = 0;i < posts.getFile().getImage().length; i++){
            strings.add(posts.getFile().getImage()[i]);
        }
        viewpagerStatusDetail.setAdapter(new AdapterDialogShowImgPersonal(strings,StatusDetailActivity.this));

        viewpagerStatusDetail.setClipToPadding(false);
        viewpagerStatusDetail.setClipChildren(false);
        viewpagerStatusDetail.setOffscreenPageLimit(3);
        viewpagerStatusDetail.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }
}
