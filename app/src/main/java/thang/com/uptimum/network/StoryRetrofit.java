package thang.com.uptimum.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import thang.com.uptimum.model.Story;

public interface StoryRetrofit {
    @GET("api/story")
    Call<List<Story>> getStory();
}
