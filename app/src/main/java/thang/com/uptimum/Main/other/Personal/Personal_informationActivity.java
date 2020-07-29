package thang.com.uptimum.Main.other.Personal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.R;
import thang.com.uptimum.login.LoginActivity;
import thang.com.uptimum.model.Error;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.ProfileRetrofit;

public class Personal_informationActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "Personal_informationActivity";
    private EditText edtNickName, edtPhone, edtDateofbirth, edtstudies_at, edtstudied_at, edtPlaceslive
            , edtFrom, edtJob, edtUsername;
    private LinearLayout linearEdtUserName;
    private Button btnUploadInfor, btnReset;
    private String action ="", username ="", id ="", nickname="", phone="", dateofbirth="",studies="", studied="", placeslive="", from="", job="";

    private NetworkUtil networkUtil;
    private Retrofit retrofit;
    private ProfileRetrofit profileRetrofit;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        toolbar = (Toolbar) findViewById(R.id.toolbarInfor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thông tin cá nhân");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mapingView();
        getDataIntent();
    }
    private void mapingView(){
        linearEdtUserName = (LinearLayout) findViewById(R.id.linearEdtUserName);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtNickName = (EditText) findViewById(R.id.edtNickName);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtDateofbirth = (EditText) findViewById(R.id.edtDateofbirth);
        edtstudies_at = (EditText) findViewById(R.id.edtstudies_at);
        edtstudied_at = (EditText) findViewById(R.id.edtstudied_at);
        edtPlaceslive = (EditText) findViewById(R.id.edtPlaceslive);
        edtFrom = (EditText) findViewById(R.id.edtFrom);
        edtJob = (EditText) findViewById(R.id.edtJob);
        btnUploadInfor = (Button) findViewById(R.id.btnUploadInfor);
        btnReset = (Button) findViewById(R.id.btnReset);

        btnReset.setOnClickListener(this);
        btnUploadInfor.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUploadInfor:
                getInfor();
                checkInfor();
                uploadInfor();
                break;
            case R.id.btnReset:
                break;
        }
    }
    private void getDataIntent(){
        Intent intent = getIntent();
        id = intent.getStringExtra("iduser");
        action = intent.getStringExtra("action");
        if(action.equals("edit")){
            linearEdtUserName.setVisibility(View.VISIBLE);
            edtUsername.setText(intent.getStringExtra("username"));
            edtNickName.setText(intent.getStringExtra("nickname"));
            edtPhone.setText(intent.getStringExtra("phone"));
            edtDateofbirth.setText(intent.getStringExtra("dateofbirth"));
            edtstudies_at.setText(intent.getStringExtra("studies"));
            edtstudied_at.setText(intent.getStringExtra("studied"));
            edtPlaceslive.setText(intent.getStringExtra("placeslive"));
            edtFrom.setText(intent.getStringExtra("from"));
            edtJob.setText(intent.getStringExtra("job"));
        }
    }
    private void getInfor(){
        username = edtUsername.getText().toString();
        nickname = edtNickName.getText().toString();
        phone = edtPhone.getText().toString();
        dateofbirth = edtDateofbirth.getText().toString();
        studies = edtstudies_at.getText().toString();
        studied = edtstudied_at.getText().toString();
        placeslive = edtPlaceslive.getText().toString();
        from = edtFrom.getText().toString();
        job = edtJob.getText().toString();
        Log.d("ahksdj"," a "+nickname+phone+dateofbirth+studied);
    }
    private void checkInfor(){

        if(TextUtils.isEmpty(nickname)){
            nickname = "";
        }
        if(TextUtils.isEmpty(phone)){
            phone = "";
        }
        if(TextUtils.isEmpty(dateofbirth)){
            dateofbirth = "";
        }
        if(TextUtils.isEmpty(studies)){
            studies = "";
        }
        if(TextUtils.isEmpty(studied)){
            studied = "";
        }
        if(TextUtils.isEmpty(placeslive)){
            placeslive = "";
        }
        if(TextUtils.isEmpty(from)){
            from = "";
        }
        if(TextUtils.isEmpty(job)){
            job = "";
        }
        Log.d("ahksdj"," a "+nickname+phone+dateofbirth+studied);
    }
    private void uploadInfor(){
        profileRetrofit = retrofit.create(ProfileRetrofit.class);
        Call<Error> errorCall = profileRetrofit.postProfile(id,nickname,phone,dateofbirth,studies,studied,placeslive,from,job);
        errorCall.enqueue(new Callback<Error>() {
            @Override
            public void onResponse(Call<Error> call, Response<Error> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(Personal_informationActivity.this, "Connect Fail", Toast.LENGTH_SHORT).show();
                }else{
                    Error errors = response.body();
                    if(!errors.isSuccess()){
                        Toast.makeText(Personal_informationActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(Personal_informationActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<Error> call, Throwable t) {
                Toast.makeText(Personal_informationActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                Log.d("informationActivity", " "+t.getMessage());
                call.cancel();
            }
        });
        if(!username.isEmpty()){
            Log.d("informationActivity", "zoo "+username);
            Call<Error> errorCall1 = profileRetrofit.postUserName(id, username);
            errorCall1.enqueue(new Callback<Error>() {
                @Override
                public void onResponse(Call<Error> call, Response<Error> response) {
                    Log.d("informationActivity", "zoo "+response);
                    if(!response.isSuccessful()){
                        Toast.makeText(Personal_informationActivity.this, "Connect Fail", Toast.LENGTH_SHORT).show();
                    }else{
                        Error errors = response.body();
                        if(!errors.isSuccess()){
                            Toast.makeText(Personal_informationActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Log.d("informationActivity"," aadsa "+ errors.getData());
                            Toast.makeText(Personal_informationActivity.this, "Username Success ", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = getSharedPreferences("userlogin", MODE_PRIVATE).edit();
                            editor.putString("username", errors.getData());
                            editor.apply();
                            setResult(100);
                            finish();
                        }
                    }
                    call.cancel();
                }

                @Override
                public void onFailure(Call<Error> call, Throwable t) {
                    Toast.makeText(Personal_informationActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    Log.d("informationActivity", " "+t.getMessage());
                    call.cancel();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
