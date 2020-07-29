package thang.com.uptimum.Main.other.Stories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.R;
import thang.com.uptimum.adapter.storyAdapter;
import thang.com.uptimum.model.Story;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.StoryRetrofit;

public class ShowAllStoriesActivity extends AppCompatActivity {
    private RecyclerView rcvShowAllStories;
    private Toolbar toolbar;
    private SharedPreferences sessionManagement;
    private String id="";

    private StoryRetrofit storyRetrofit;
    private NetworkUtil networkUtil;
    private Retrofit retrofit;
    private ArrayList<Story> arrayStory;
    private storyAdapter adapterStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_stories);
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        arrayStory = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.toolbarShowAllStories);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tin");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rcvShowAllStories = (RecyclerView) findViewById(R.id.rcvShowAllStories);
        rcvShowAllStories.setHasFixedSize(true);
        rcvShowAllStories.setLayoutManager(new GridLayoutManager(ShowAllStoriesActivity.this, 3, GridLayoutManager.VERTICAL,false));

        sessionManagement = this.getSharedPreferences("userlogin", Context.MODE_PRIVATE);
        id = sessionManagement.getString("id","");

        getStory();
    }
    private void getStory() {
        arrayStory.clear();
        storyRetrofit = retrofit.create(StoryRetrofit.class);
        Call<List<Story>> callstory = storyRetrofit.getStory();
        callstory.enqueue(new Callback<List<Story>>() {
            @Override
            public void onResponse(Call<List<Story>> call, Response<List<Story>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(ShowAllStoriesActivity.this, "lỗi rác story", Toast.LENGTH_SHORT).show();
                    return;
                }
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
                call.cancel();
            }

            @Override
            public void onFailure(Call<List<Story>> call, Throwable t) {
                Log.d("loaddataa","Load không được lỗi : "+t.getMessage());
                call.cancel();
            }
        });
        adapterStory = new storyAdapter(arrayStory, getApplicationContext());
        rcvShowAllStories.setAdapter(adapterStory);
    }
}
