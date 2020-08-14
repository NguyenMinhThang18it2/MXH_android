package thang.com.uptimum.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import thang.com.uptimum.model.Error;

public interface NotificationRetrofit {
    @GET("api/updatenotification/{id}")
    Call<Error> readAll(
            @Header("Authorization") String auth,
            @Path("id") String iduser
    );
}
