package thang.com.uptimum.Main.other.Stories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.R;
import thang.com.uptimum.model.Story;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.StoryRetrofit;

public class ViewpagerStoriesActivity extends AppCompatActivity {
    private ViewPager2 viewPager2story;
    private StoriesViewpaerAdapter storiesViewpaerAdapter;

    private StoryRetrofit storyRetrofit;
    private NetworkUtil networkUtil;
    private Retrofit retrofit;

    private SharedPreferences sessionManagement;
    private String id ="", token = "";
    private int numberStory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_stories);

        viewPager2story = (ViewPager2) findViewById(R.id.storiesViewpager);
        Intent intent = getIntent();
        numberStory  = (int) intent.getSerializableExtra("numberClickStory");
        Log.d("storhjkl", " "+numberStory);
        networkUtil = new NetworkUtil();
        getStory();
        viewPager2story.setPageTransformer(new CubeTransformerViewpager());
        viewPager2story.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

            }
        });
    }
    private void getStory() {
        sessionManagement = ViewpagerStoriesActivity.this.getApplicationContext().getSharedPreferences("userlogin", Context.MODE_PRIVATE);
        id = sessionManagement.getString("id","");
        token = "Bearer "+sessionManagement.getString("token","");
        List<Fragment> fragments = new ArrayList<>();
        retrofit = networkUtil.getRetrofit();
        storyRetrofit = retrofit.create(StoryRetrofit.class);
        Call<List<Story>> callstory = storyRetrofit.getStory(token);
        callstory.enqueue(new Callback<List<Story>>() {
            @Override
            public void onResponse(Call<List<Story>> call, Response<List<Story>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(ViewpagerStoriesActivity.this, "lỗi rác story", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Story> storys = response.body();
                for(Story story : storys){
                    if(story.getUsers().getId().equals(id)){
                        ArrayList<Story> stories = new ArrayList<>();
                        stories.add(story);
                        fragments.add(new StoriesFragment(stories, viewPager2story));
                        break;
                    }
                }
                for(Story story : storys){
                    if(!story.getUsers().getId().equals(id)){
                        ArrayList<Story> stories = new ArrayList<>();
                        stories.add(story);
                        fragments.add(new StoriesFragment(stories, viewPager2story));
                    }
                }
//                Collections.reverse(arrayStory);
                storiesViewpaerAdapter = new StoriesViewpaerAdapter(getSupportFragmentManager(),getLifecycle(),fragments);
                viewPager2story.setAdapter(storiesViewpaerAdapter);
                viewPager2story.setCurrentItem(numberStory,false);
            }

            @Override
            public void onFailure(Call<List<Story>> call, Throwable t) {
                Log.d("loaddataa","Load không được lỗi : "+t.getMessage());
            }
        });
    }
}
