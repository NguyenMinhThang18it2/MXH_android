package thang.com.uptimum.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import thang.com.uptimum.model.Followers;
import thang.com.uptimum.model.Following;

public interface FollowRetrofit {
    @GET("api/followers/{id}")
    Call<List<Followers>> getFollowers(
            @Header("Authorization") String auth,
            @Path("id") String iduser
    );

    @GET("api/following/{id}")
    Call<List<Following>> getFollowing(
            @Header("Authorization") String auth,
            @Path("id") String iduser
    );
}
