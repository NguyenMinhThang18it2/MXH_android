package thang.com.uptimum.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import thang.com.uptimum.model.ReplyComment;

public interface ReplyCommentRetrofit {
    @GET("api/replycmt/{id}")
    Call<ReplyComment> getReplyCmt(
            @Header("Authorization") String auth,
            @Path("id") String idcmt
    );
}
