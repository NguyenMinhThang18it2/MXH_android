package thang.com.uptimum.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import thang.com.uptimum.model.ThemeStatus;


public interface ThemeStatusRetrofit {
    @GET("api/theme")
    Call<List<ThemeStatus>> getTheme();
}
