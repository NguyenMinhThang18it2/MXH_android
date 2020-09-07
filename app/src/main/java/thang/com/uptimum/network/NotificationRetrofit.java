package thang.com.uptimum.network;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import thang.com.uptimum.model.Error;
import thang.com.uptimum.model.Notification;

public interface NotificationRetrofit {
    @GET("api/updatenotification/{id}")
    Call<Error> readAll(
            @Header("Authorization") String auth,
            @Path("id") String iduser
    );

    @GET("api/notification/{id}")
    Call<Notification> getNotification(
            @Header("Authorization") String auth,
            @Path("id") String iduser
    );

    @DELETE("api/notification/{id}")
    @FormUrlEncoded
    Call<Error> deleteNotification(
            @Header("Authorization") String auth,
            @Path("id") String iduser,
            @Field("replyfriend") String replyfriend
    );
}
