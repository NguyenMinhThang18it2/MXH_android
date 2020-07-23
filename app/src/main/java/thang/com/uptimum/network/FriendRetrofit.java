package thang.com.uptimum.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import thang.com.uptimum.model.Friend;

public interface FriendRetrofit {
    @GET("api/friend/{id}")
    Call<List<Friend>> getFriend(
            @Path("id") String iduser
    );
}
