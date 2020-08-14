package thang.com.uptimum.network;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import thang.com.uptimum.model.Error;
import thang.com.uptimum.model.Login;
import thang.com.uptimum.model.Users;

public interface RetrofitInterface {
    @POST("api/register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("username") String username,
                                    @Field("email") String email,
                                    @Field("password") String password);

    @POST("api/login")
    @FormUrlEncoded
    Call<Login> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("api/checklogin")
    Call<Login> checkLogin(
            @Header("Authorization") String auth
    );
//    @GET("users/{email}")
//    Observable<Users> getProfile(@Path("email") String email);

//    @PUT("users/{email}")
//    Observable<Response> changePassword(@Path("email") String email, @Body Users user);
//
//    @POST("users/{email}/password")
//    Observable<Response> resetPasswordInit(@Path("email") String email);
//
//    @POST("users/{email}/password")
//    Observable<Response> resetPasswordFinish(@Path("email") String email, @Body Users user);
}
