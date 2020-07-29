package thang.com.uptimum.network;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import thang.com.uptimum.model.Error;
import thang.com.uptimum.model.Story;

public interface StoryRetrofit {
    @GET("api/story")
    Call<List<Story>> getStory();

    @Multipart
    @POST("api/story/{id}")
    Call<Error> postStory(
            @Path("id") String id,
            @Part List<MultipartBody.Part> upload
    );
}
