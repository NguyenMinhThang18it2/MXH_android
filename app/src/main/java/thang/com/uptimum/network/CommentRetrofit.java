package thang.com.uptimum.network;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import thang.com.uptimum.model.Error;

public interface CommentRetrofit {
    @DELETE("api/deletecomment/{id}")
    Call<Error> deleteCmt(
            @Header("Authorization") String auth,
            @Path("id") String idcmt
    );

    @PUT("api/putcomment/{id}")
    @FormUrlEncoded
    Call<Error> putCmt(
            @Header("Authorization") String auth,
            @Path("id") String idcmt,
            @Field("document") String document
    );
}
