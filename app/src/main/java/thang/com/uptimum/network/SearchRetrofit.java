package thang.com.uptimum.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import thang.com.uptimum.model.Error;
import thang.com.uptimum.model.Users;

public interface SearchRetrofit {
    @GET("api/search/{key}")
    Call<List<Users>> getDataSuggestion(
            @Header("Authorization") String auth,
            @Path("key") String keyword
    );

    @POST("api/search-history")
    @FormUrlEncoded
    Call<Error> postKeySearch(
            @Header("Authorization") String auth,
            @Field("id") String iduser,
            @Field("key") String keywork
    );
}
