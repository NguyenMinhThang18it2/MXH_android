package thang.com.uptimum.network;

import android.provider.ContactsContract;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import thang.com.uptimum.model.ProfileUser;

public interface ProfileRetrofit {
    @GET("api/profile/{id}")
    Call<ProfileUser> getProfile(
            @Path("id") String iduser
    );
}
