package thang.com.uptimum.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.Main.MainActivity;
import thang.com.uptimum.R;
import thang.com.uptimum.login.LoginActivity;
import thang.com.uptimum.model.Error;
import thang.com.uptimum.model.Login;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.RetrofitInterface;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN = 4000;
    private static String TAG = "SplashActivity";
    private Animation topAnimation, bottomAnimation;
    private ImageView image;
    private TextView tvSlogan;
    private SharedPreferences userPref;
    private NetworkUtil networkUtil;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String token;
    private SharedPreferences.Editor editoruser;
    private Handler handler;
    private int check = 0;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        image.setImageDrawable(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        userPref = SplashActivity.this.getSharedPreferences("userlogin", MODE_PRIVATE);
        token = "Bearer "+userPref.getString("token","");

        editoruser = userPref.edit();
        handler = new Handler();
        new checkLogin().execute();



    }
    private class checkLogin extends AsyncTask<Void, String, Integer>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mapingView();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            Log.d(TAG, token);
            retrofitInterface = retrofit.create(RetrofitInterface.class);
            Call<Login> call = retrofitInterface.checkLogin(token);
            call.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(SplashActivity.this, "Lỗi response", Toast.LENGTH_SHORT).show();
                        check = 0;
                    }else{
                        Login login = response.body();
                        Log.d(TAG,"a   "+ login  );
                        editoruser.putString("id",login.getUsers().getId());
                        editoruser.putString("username",login.getUsers().getUsername());
                        editoruser.putString("email",login.getUsers().getEmail());
                        editoruser.putString("avata",login.getUsers().getAvata());
                        editoruser.putString("coverimage",login.getUsers().getCoverimage());
                        editoruser.putString("token", userPref.getString("token",""));
                        editoruser.apply();
                        check = 1;
                    }
                    call.cancel();
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    Log.d(TAG,"lỗi "+ t.getMessage());
                    check = 0;
                }
            });
            return check;
        }

        @Override
        protected void onPostExecute(Integer aInt) {
            super.onPostExecute(aInt);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(check == 0){
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else if(check == 1){
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                }
            },SPLASH_SCREEN);
        }
    }
    private void mapingView(){
        topAnimation = (Animation) AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = (Animation) AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        image = (ImageView) findViewById(R.id.splashimage);
        tvSlogan = (TextView) findViewById(R.id.slogan);

        image.setAnimation(topAnimation);
        tvSlogan.setAnimation(bottomAnimation);
    }
}
