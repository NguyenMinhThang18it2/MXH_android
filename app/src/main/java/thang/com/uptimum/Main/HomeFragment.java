package thang.com.uptimum.Main;

import android.app.AlertDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.supercharge.shimmerlayout.ShimmerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.Dialog.CommentBottomSheetDialog;
import thang.com.uptimum.Dialog.DialogMenuPosts;
import thang.com.uptimum.Dialog.DialogMenuSharePosts;
import thang.com.uptimum.Dialog.DialogShowImageStatus;
import thang.com.uptimum.Main.other.StatusDetail.StatusDetailActivity;
import thang.com.uptimum.Main.other.Stories.ShowAllStoriesActivity;
import thang.com.uptimum.Main.other.Stories.UploadStoriesActivity;
import thang.com.uptimum.R;
import thang.com.uptimum.adapter.postsAdapter;
import thang.com.uptimum.adapter.storyAdapter;
import thang.com.uptimum.login.LoginActivity;
import thang.com.uptimum.login.SessionManagement;
import thang.com.uptimum.model.Posts;
import thang.com.uptimum.model.Story;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.PostsRetrofit;
import thang.com.uptimum.network.StoryRetrofit;
import thang.com.uptimum.upload.UploadPostsActivity;
//import thang.com.uptimum.upload.UploadPostsActivity;
//import static thang.com.uptimum.Main.MainActivity.relativeLayout;
import static thang.com.uptimum.Socket.SocketIO.socket;
import static thang.com.uptimum.util.Constants.BASE_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener , ComponentCallbacks2 {
    private final String TAG = "HomeFragment";

    private SwipeRefreshLayout swipe_refresh_layout;
    private ShimmerLayout shimmerLayout;
    private View contactsview;
    private CircleImageView imguser, imgAvataUserLogin;
    private TextView txtstatus, txtThongbao, txtUserNameLogin;
    private RoundedImageView  ImgstoriesUserLogin;
    private NestedScrollView nestedScrollView;
    private RecyclerView recyclerViewStory, recyclerViewstatus;
    private ImageView addStories;
    private LinearLayoutManager linearLayoutManagerstatus, linearLayoutManagerstory;
    private ArrayList<Posts> arrayPosts;
    private ArrayList<Story> arrayStory;
    private postsAdapter adapterPosts;
    private storyAdapter adapterStory;
    private FrameLayout homeFragment;
    private LinearLayout linearShowAllStories;

    private NetworkUtil networkUtil;
    private PostsRetrofit postsRetrofit;
    private StoryRetrofit storyRetrofit;
    private Retrofit retrofit;

    private BottomSheetBehavior mBehavior;

    private SharedPreferences sessionManagement;
    private String id ="";
    private String avata = "";
    private String coverimage = "";
    private String username = "";
    private String token = "";
    private int numberClickStory = 0;

    private ArrayList<Integer> arrGif;

    private postsAdapter.RecyclerviewClickListener listener;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
        contactsview =  inflater.inflate(R.layout.fragment_home, container, false);
//        // thêm tiếp nghe ba
        linearShowAllStories = (LinearLayout) contactsview.findViewById(R.id.linearShowAllStories);
        homeFragment = (FrameLayout) contactsview.findViewById(R.id.homeFragment);
        nestedScrollView = (NestedScrollView) contactsview.findViewById(R.id.nestedScroolView);
        imguser = (CircleImageView) contactsview.findViewById(R.id.user);
        txtstatus = (TextView) contactsview.findViewById(R.id.status);
        txtThongbao = (TextView) contactsview.findViewById(R.id.txtThongbao);
        shimmerLayout = (ShimmerLayout) contactsview.findViewById(R.id.shimmer_layout);
        swipe_refresh_layout = (SwipeRefreshLayout) contactsview.findViewById(R.id.swipe_refresh_layout);
        txtUserNameLogin = (TextView) contactsview.findViewById(R.id.txtUserNameLogin);
        imgAvataUserLogin = (CircleImageView) contactsview.findViewById(R.id.imgAvataUserLogin);
        ImgstoriesUserLogin = (RoundedImageView) contactsview.findViewById(R.id.ImgstoriesUserLogin);

        recyclerViewStory = (RecyclerView) contactsview.findViewById(R.id.recyclerViewStory);
        recyclerViewstatus = (RecyclerView) contactsview.findViewById(R.id.recyclerViewstatus);
        recyclerViewStory.setHasFixedSize(true);
        recyclerViewstatus.setHasFixedSize(true);
        linearLayoutManagerstory =  new LinearLayoutManager
                (getContext(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManagerstatus =  new LinearLayoutManager
                (getContext(), LinearLayoutManager.VERTICAL, false);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
//        recyclerViewstatus.addItemDecoration(dividerItemDecoration);
        recyclerViewStory.setLayoutManager(linearLayoutManagerstory);
        recyclerViewstatus.setLayoutManager(linearLayoutManagerstatus);
        recyclerViewstatus.setNestedScrollingEnabled(false);
        //
        //arr
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        addEvents();
        setOnClickListener();
        addDataUserlogin();
        getStory();
        getPosts();
        addEjmotion();


        return contactsview;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerViewstatus.getRecycledViewPool().clear();
        recyclerViewstatus.setAdapter(null);
        recyclerViewStory.getRecycledViewPool().clear();
        recyclerViewStory.setAdapter(null);

    }

    public void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.status:
                addNewStatus();
                break;
            case R.id.ImgstoriesUserLogin:
                Intent intent = new Intent(getActivity().getApplicationContext(), UploadStoriesActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.user:
                Intent personal = new Intent(getActivity().getApplicationContext(), PersonalActivity.class);
                personal.putExtra("iduser", id);
                personal.putExtra("avata",avata);
                personal.putExtra("coverimage", coverimage);
                personal.putExtra("username",username);
                startActivity(personal);
                break;
            case R.id.linearShowAllStories:
                Intent intent1 = new Intent(getActivity().getApplicationContext(), ShowAllStoriesActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }
    public void refresh(){
        txtThongbao.setVisibility(View.GONE);
        shimmerLayout.setVisibility(View.VISIBLE);
        nestedScrollView.setVisibility(View.INVISIBLE);
        getStory();
        getPosts();
        addDataUserlogin();
        freeMemory();
        swipe_refresh_layout.setRefreshing(false);
    }
    private void addEvents() {
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        linearShowAllStories.setOnClickListener(this);
        txtstatus.setOnClickListener(this);
        ImgstoriesUserLogin.setOnClickListener(this);
        imguser.setOnClickListener(this);
    }

    private void addNewStatus() {
        Intent intent = new Intent(getContext(), UploadPostsActivity.class);
        startActivity(intent);
    }

    private void addDataUserlogin() {
        // get thông tin
        sessionManagement = getContext().getApplicationContext().getSharedPreferences("userlogin",Context.MODE_PRIVATE);
        id = sessionManagement.getString("id","");
        avata = sessionManagement.getString("avata", "");
        coverimage = sessionManagement.getString("coverimage", "");
        username = sessionManagement.getString("username","");
        token = "Bearer "+sessionManagement.getString("token","");
        Log.d(TAG," "+id+avata+coverimage+username+token);
        //set thông tin
        Picasso.get().load(BASE_URL+"uploads/"+avata).into(ImgstoriesUserLogin);
        Picasso.get().load(BASE_URL+"uploads/"+avata).resize(100,100).into(imguser);
        txtstatus.setHint(username+" đang nghĩ gì ?");
        socket.emit("chat message", id);
    }

    private void getStory() {

        arrayStory = new ArrayList<>();
        shimmerLayout.startShimmerAnimation();
        arrayStory.clear();
        storyRetrofit = retrofit.create(StoryRetrofit.class);
        Call<List<Story>> callstory = storyRetrofit.getStory(token);
        callstory.enqueue(new Callback<List<Story>>() {
            @Override
            public void onResponse(Call<List<Story>> call, Response<List<Story>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "lỗi rác story", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    List<Story> storys = response.body();
                    arrayStory.clear();
                    for(Story story : storys){
                        if(story.getUsers().getId().equals(id)) { // lấy story userLogin để đầu mảng
                            arrayStory.add(story);
                            break;
                        }
                    }
                    for(Story story : storys){
                        if(!story.getUsers().getId().equals(id)){ // lấy story userLogin để đầu mảng
                            arrayStory.add(story);
                        }
                    }
                    adapterStory.notifyDataSetChanged();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        shimmerLayout.stopShimmerAnimation();
                        nestedScrollView.setVisibility(View.VISIBLE);
                        shimmerLayout.setVisibility(View.GONE);
                    }
                }, 2000);
                call.cancel();
            }

            @Override
            public void onFailure(Call<List<Story>> call, Throwable t) {
                Log.d("loaddataa","Load không được lỗi : "+t.getMessage());
                nestedScrollView.setVisibility(View.VISIBLE);
                shimmerLayout.setVisibility(View.GONE);
                txtThongbao.setVisibility(View.VISIBLE);
                call.cancel();
            }
        });
        adapterStory = new storyAdapter(arrayStory, getActivity().getApplicationContext());
        recyclerViewStory.setAdapter(adapterStory);
    }

    private void getPosts() {

        arrayPosts = new ArrayList<>();
        postsRetrofit = retrofit.create(PostsRetrofit.class);
        arrayPosts.clear();
        Call<List<Posts>> callposts = postsRetrofit.getPosts(token);
        callposts.enqueue(new Callback<List<Posts>>() {
            @Override
            public void onResponse(Call<List<Posts>> call, Response<List<Posts>> response) {
                if(!response.isSuccessful()){
                    LogoutSuccess();
                }else{
                    List<Posts> posts = response.body();
                    for(Posts post : posts){
                        if(post.getFile().getVideo() == null)
                            arrayPosts.add(post);
                        else if(post.getFile().getVideo().length() < 10)
                            arrayPosts.add(post);
                    }
                    Collections.reverse(arrayPosts);
                    adapterPosts.notifyDataSetChanged();
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<List<Posts>> call, Throwable t) {
                Log.d("lỗi posts",t.getMessage(),t);
                call.cancel();
            }
        });
        adapterPosts = new postsAdapter(arrayPosts, getActivity().getApplicationContext(), listener, nestedScrollView);
        recyclerViewstatus.setAdapter(adapterPosts);
    }
    private void setOnClickListener(){
        listener = new postsAdapter.RecyclerviewClickListener() {
            @Override
            public void onClickComment(RelativeLayout btnComment, int position, int typeClick) {

                    Log.d("kjqhwekwqe", " " + position);
                    CommentBottomSheetDialog commentBottomSheetDialog =
                            CommentBottomSheetDialog.newInstance(arrayPosts.get(position).getId());
                    commentBottomSheetDialog.show(getFragmentManager(),
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
                  getContext(), arrayPosts.get(position).getId(), arrayPosts.get(position).getDocument(), checkuser
                );
                dialogMenuPosts.show(getFragmentManager(),"menu posts");
            }

            @Override
            public void showImg(ImageView imgShow, int position, int typeClick) {
                DialogShowImageStatus dialogShowImageStatus = new DialogShowImageStatus(position, arrayPosts);
                dialogShowImageStatus.show(getFragmentManager(),"ShowImg_dialog_fragment");
            }

            @Override
            public void showStatusDetail(String idposts) {
                Log.d("StatusDetailActivity", "id "+ idposts);
                Intent intent = new Intent(getContext().getApplicationContext(), StatusDetailActivity.class);
                intent.putExtra("idposts", idposts);
                startActivity(intent);
            }

            @Override
            public void personalUser(String iduser, int position) {
                Intent personal = new Intent(getActivity().getApplicationContext(), PersonalActivity.class);
                personal.putExtra("iduser", iduser);
                personal.putExtra("avata",arrayPosts.get(position).getIduser().getAvata());
                personal.putExtra("coverimage", arrayPosts.get(position).getIduser().getCoverimage());
                personal.putExtra("username",arrayPosts.get(position).getIduser().getUsername());
                startActivity(personal);
            }

            @Override
            public void sharePosts(int position) {
                DialogMenuSharePosts dialogMenuSharePosts
                        = new DialogMenuSharePosts(getContext().getApplicationContext(), arrayPosts.get(position).getId(), arrayPosts.get(position).getIduser().getId());
                dialogMenuSharePosts.show(getFragmentManager(), "menu share posts");
            }
        };

    }
    private void addEjmotion(){
        arrGif = new ArrayList<>();
        arrGif.add(R.drawable.ejmotionlike);
        arrGif.add(R.drawable.ejmotionlove);
        arrGif.add(R.drawable.ejmotionthuongthuong);
        arrGif.add(R.drawable.ejmotionhahah);
        arrGif.add(R.drawable.ejmotionwow);
        arrGif.add(R.drawable.ejmotionsad);
        arrGif.add(R.drawable.ejmotionphanno);
    }
    private void LogoutSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Hết phiên đăng nhập")
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SessionManagement sessionManagement = new SessionManagement(getActivity().getApplicationContext());
                        sessionManagement.removeSession();
                        Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
        builder.create().show();
    }
    @Override
    public void onTrimMemory(int level) {
        switch (level) {

            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:


                break;

            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:


                break;

            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                break;

            default:

                break;
        }
    }
    private void unbindDrawables(View view) {
        if (view.getBackground() != null)
            view.getBackground().setCallback(null);

        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            imageView.setImageBitmap(null);
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++)
                unbindDrawables(viewGroup.getChildAt(i));

            if (!(view instanceof AdapterView))
                viewGroup.removeAllViews();
        }
    }
}
