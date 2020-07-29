package thang.com.uptimum.network;

import android.provider.ContactsContract;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import thang.com.uptimum.model.Error;
import thang.com.uptimum.model.ProfileUser;

public interface ProfileRetrofit {
    @GET("api/profile/{id}")
    Call<ProfileUser> getProfile(
            @Path("id") String iduser
    );

    @POST("api/profile/{id}")
    @FormUrlEncoded
    Call<Error> postProfile(
            @Path("id") String iduser,
            @Field("nickname") String nickname,
            @Field("phone") String phone,
            @Field("dateofbirth") String dateofbirth,
            @Field("studies_at") String studies_at,
            @Field("studied_at") String studied_at,
            @Field("placeslive") String placeslive,
            @Field("from") String from,
            @Field("job") String job
    );

    @POST("api/profileusername/{id}")
    @FormUrlEncoded
    Call<Error> postUserName(
            @Path("id") String iduser,
            @Field("username") String username
    );

    @Multipart
    @POST("api/{type}/{id}")
    Call<Error> postImgProfile(
            @Path("id") String iduser,
            @Path("type") String type,
            @Part MultipartBody.Part upload
    );
}
