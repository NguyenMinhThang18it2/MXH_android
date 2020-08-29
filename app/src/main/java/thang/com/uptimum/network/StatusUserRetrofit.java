package thang.com.uptimum.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import thang.com.uptimum.model.StatusUserLogin;

public interface StatusUserRetrofit {
    @GET("api/statususer")
    Call<List<StatusUserLogin>> getData(
            @Header("Authorization") String auth
    );
}
