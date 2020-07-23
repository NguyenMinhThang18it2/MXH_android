package thang.com.uptimum.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FriendRetrofit {
    @GET("api/friend/{id}")
    Call<String> getFriend(
            @Path("id") String iduser
    );
}
