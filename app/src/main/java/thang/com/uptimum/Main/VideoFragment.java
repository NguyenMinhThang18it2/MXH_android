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
import android.widget.Toast;

import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.R;
import thang.com.uptimum.adapter.postsAdapter;
import thang.com.uptimum.adapter.videoAdapter;
import thang.com.uptimum.model.Posts;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.PostsRetrofit;
import thang.com.uptimum.network.StoryRetrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {
    private View viewVideo;
    private RecyclerView recyclerViewVideo;
    private LinearLayoutManager linearLayoutManagerVideo;

    private ArrayList<Posts> arrayVideo;
    private videoAdapter adapterVideo;

    private NetworkUtil networkUtil;
    private Retrofit retrofit;
    private PostsRetrofit postsRetrofit;
    private SharedPreferences sharedPreferences;
    private String token = "";
    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewVideo =  inflater.inflate(R.layout.fragment_video, container, false);
        recyclerViewVideo = (RecyclerView) viewVideo.findViewById(R.id.recyclerViewVideo);
        recyclerViewVideo.setHasFixedSize(true);
        linearLayoutManagerVideo =  new LinearLayoutManager
                (getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewVideo.setLayoutManager(linearLayoutManagerVideo);
        adapterVideo = new videoAdapter();
        networkUtil = new NetworkUtil();
        getPosts();
        return viewVideo;
    }
    private void getPosts() {
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("userlogin", Context.MODE_PRIVATE);
        token = "Bearer "+sharedPreferences.getString("token","");
        arrayVideo = new ArrayList<>();
        retrofit = networkUtil.getRetrofit();
        postsRetrofit = retrofit.create(PostsRetrofit.class);

        Call<List<Posts>> callposts = postsRetrofit.getPosts(token);
        callposts.enqueue(new Callback<List<Posts>>() {
            @Override
            public void onResponse(Call<List<Posts>> call, Response<List<Posts>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "lỗi rác posts", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Posts> posts = response.body();
                for(Posts post : posts){
                    if(post.getFile().getVideo().length()>10){
                        arrayVideo.add(post);
                    }
                }
                Collections.reverse(arrayVideo);
                adapterVideo = new videoAdapter(arrayVideo, getContext());
                recyclerViewVideo.setAdapter(adapterVideo);
                Log.d("arrvideo", " "+arrayVideo);
            }

            @Override
            public void onFailure(Call<List<Posts>> call, Throwable t) {
                Log.d("lỗi posts",t.getMessage(),t);
            }
        });
    }
}
