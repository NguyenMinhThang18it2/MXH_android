package thang.com.uptimum.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import thang.com.uptimum.Main.MainActivity;
import thang.com.uptimum.R;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.RetrofitInterface;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etEmail, etPassword, etConfirmpass;
    private Button btnRegister;
    private ImageView btnback;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RetrofitInterface retrofitInterface;

    private NetworkUtil networkUtil;
    private Retrofit retrofit;
    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //init service
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        etUsername = (EditText) findViewById(R.id.etusername);
        etEmail = (EditText) findViewById(R.id.etemail);
        etPassword = (EditText) findViewById(R.id.etpassword);
        etConfirmpass = (EditText) findViewById(R.id.etconfirmpassword);
        btnRegister = (Button) findViewById(R.id.btnregister);
        btnback = (ImageView) findViewById(R.id.buttonback);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(etUsername.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString(), etConfirmpass.getText().toString());

            }
        });
    }

    private void registerUser(String username, String email, String password, String confirmpassword) {
        if(TextUtils.isEmpty(email)){
            Toast.makeText(RegisterActivity.this, "Username cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this, "Email cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(RegisterActivity.this, "Password cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this, "ConfirmPassword cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if(!password.equals(confirmpassword)){
            Toast.makeText(RegisterActivity.this, "ConfirmPassword mismatched", Toast.LENGTH_LONG).show();
            return;
        }
        compositeDisposable.add(retrofitInterface.registerUser(username, email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String res) throws Exception {
                        Toast.makeText(RegisterActivity.this, res, Toast.LENGTH_LONG).show();
                        if(res.equals("Success")){
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }));
    }
}
