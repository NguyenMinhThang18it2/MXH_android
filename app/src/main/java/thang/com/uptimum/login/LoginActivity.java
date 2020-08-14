package thang.com.uptimum.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.Main.HomeFragment;
import thang.com.uptimum.Main.MainActivity;
import thang.com.uptimum.R;
import thang.com.uptimum.model.Login;
import thang.com.uptimum.model.Story;
import thang.com.uptimum.model.Users;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.RetrofitInterface;

public class LoginActivity extends AppCompatActivity {
    private TextView txtRegister;
    private EditText etEmail, etPassword;
    private Button btnLogin;
//    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RetrofitInterface retrofitInterface;
    private SessionManagement sessionManagement;

    private NetworkUtil networkUtil;
    private Retrofit retrofit;

//    @Override
//    protected void onStart() {
//        super.onStart();
//        sessionManagement = new SessionManagement(LoginActivity.this);
//        sessionManagement.getSession();
//
//        int idsession = sessionManagement.getSession();
//        if(idsession != -1){
//            LoginSuccess();
//        }
//        else {
//
//        }
//    }

    //    @Override
//    protected void onStop() {
//        compositeDisposable.clear();
//        super.onStop();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //init service
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //init view
        txtRegister = (TextView) findViewById(R.id.register);
        etEmail = (EditText) findViewById(R.id.etemail);
        etPassword = (EditText) findViewById(R.id.etpassword);
        btnLogin = (Button) findViewById(R.id.btnlogin);

        Event();
    }
    private void Event() {
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(validate()){
//
//                }
                loginUser(etEmail.getText().toString(), etPassword.getText().toString());
            }

            private boolean validate() {
                if(etEmail.getText().toString().isEmpty()){
                    return false;
                }
                if(etPassword.getText().toString().isEmpty()){
                    return false;
                }
                return true;
            }

            private void loginUser(String email, String password) {
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this, "Email cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Password cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                Call<Login> call = retrofitInterface.loginUser(email,password);
                call.enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> call, Response<Login> response) {
                        Login login = response.body();
                        Log.d("Login", " "+ login);
                        if(!login.isSuccess()){
                            Toast.makeText(LoginActivity.this, login.getMsg(), Toast.LENGTH_SHORT).show();
                        }else{

                            Users users = new Users(login.getUsers().getId(),login.getUsers().getUsername(),login.getUsers().getEmail(),login.getUsers().getPassword(), login.getUsers().getAvata(), login.getUsers().getCoverimage(), login.getUsers().getToken());

                            SharedPreferences userPref = LoginActivity.this.getSharedPreferences("userlogin", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editoruser = userPref.edit();
                            editoruser.putString("id",login.getUsers().getId());
                            editoruser.putString("username",login.getUsers().getUsername());
                            editoruser.putString("email",login.getUsers().getEmail());
                            editoruser.putString("avata",login.getUsers().getAvata());
                            editoruser.putString("coverimage",login.getUsers().getCoverimage());
                            editoruser.putString("token", login.getToken());
                            editoruser.apply();

                            sessionManagement = new SessionManagement(LoginActivity.this);
                            sessionManagement.saveSession(users);

                            Toast.makeText(LoginActivity.this, login.getMsg(), Toast.LENGTH_SHORT).show();
                            LoginSuccess();
                        }
                    }

                    @Override
                    public void onFailure(Call<Login> call, Throwable t) {
                        Log.d("lỗi login","đây : "+t.getMessage());
                    }
                });
            }
        });
    }
    private void LoginSuccess(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
