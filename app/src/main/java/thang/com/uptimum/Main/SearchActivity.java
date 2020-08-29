package thang.com.uptimum.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.R;
import thang.com.uptimum.adapter.AdapterSearch;
import thang.com.uptimum.model.Error;
import thang.com.uptimum.model.Users;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.SearchRetrofit;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    private LinearLayout linearOldSearch, linearNewSearch;
    private RecyclerView rcvOldSearch, rcvNewSearch;
    private TextView txtSearch;
    private Toolbar toolbar;

    private SharedPreferences sessionManagement;
    private String token= "", id="", avata="", coverimage="", username="", iduserLogin = "", keyword="";

    private ArrayList<Users> usersArr;
    private AdapterSearch adapterSearch;

    private NetworkUtil networkUtil;
    private Retrofit retrofit;
    private SearchRetrofit searchRetrofit;

    private AdapterSearch.onClickItemSearch mListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        searchRetrofit = retrofit.create(SearchRetrofit.class);
        //
        usersArr = new ArrayList<>();
        getDataLogin();
        customToolbar();
        mapingView();
        customRecycler();
        keySuggestion();// gợi ý từ khóa
        onClickItemSearch();
    }
    private void getDataLogin(){
        sessionManagement = SearchActivity.this.getSharedPreferences("userlogin", Context.MODE_PRIVATE);
        iduserLogin = sessionManagement.getString("id","");
        token = "Bearer "+sessionManagement.getString("token","");
    }
    private void mapingView() {
        linearOldSearch = (LinearLayout) findViewById(R.id.linearOldSearch);
        linearNewSearch = (LinearLayout) findViewById(R.id.linearNewSearch);
        rcvOldSearch = (RecyclerView) findViewById(R.id.rcvOldSearch);
        rcvNewSearch = (RecyclerView) findViewById(R.id.rcvNewSearch);
        txtSearch = (TextView) findViewById(R.id.txtSearch);
    }
    private void customRecycler(){
        rcvOldSearch.setHasFixedSize(true);
        rcvNewSearch.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerrcvOldSearch = new LinearLayoutManager(
                SearchActivity.this, RecyclerView.VERTICAL, false
        );
        LinearLayoutManager linearLayoutManagerNewSearch = new LinearLayoutManager(
            SearchActivity.this, RecyclerView.VERTICAL, false
        );
        rcvOldSearch.setLayoutManager(linearLayoutManagerrcvOldSearch);
        rcvNewSearch.setLayoutManager(linearLayoutManagerNewSearch);
        //
    }
    private void customToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbarPersonal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void keySuggestion(){
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(txtSearch.getText().toString())){
                    linearOldSearch.setVisibility(View.VISIBLE);
                    linearNewSearch.setVisibility(View.GONE);
                }else{
                    linearOldSearch.setVisibility(View.GONE);
                    linearNewSearch.setVisibility(View.VISIBLE);
                    keyword = txtSearch.getText().toString();
                    getDataSuggestion(keyword);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void getDataSuggestion(String key){
        Call<List<Users>> listCall = searchRetrofit.getDataSuggestion(token, key);
        listCall.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(SearchActivity.this, "lỗi mạng", Toast.LENGTH_SHORT).show();
                }else{
                    List<Users> users = response.body();
                    usersArr.clear();
                    for (Users datausers : users){
                        usersArr.add(datausers);
                    }
                }
                adapterSearch.notifyDataSetChanged();
                call.cancel();
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Log.d(TAG, " " + t.getMessage());
            }
        });

        adapterSearch = new AdapterSearch(usersArr, SearchActivity.this, mListener);
        rcvNewSearch.setAdapter(adapterSearch);
    }
    private void onClickItemSearch(){
        mListener = new AdapterSearch.onClickItemSearch() {
            @Override
            public void onClick(LinearLayout linearSearchAdapter, int position) {
                intentPersonal(position);
                updateSearchHistory();
            }
        };
    }
    private void updateSearchHistory(){
        Call<Error> errorCall = searchRetrofit.postKeySearch(token,iduserLogin,keyword);
        errorCall.enqueue(new Callback<Error>() {
            @Override
            public void onResponse(Call<Error> call, Response<Error> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, " lỗi respone");
                }else{
                    Error error = response.body();
                    if(error.isSuccess()){

                    }else{

                    }
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<Error> call, Throwable t) {
                Log.d(TAG, " lỗi "+ t.getMessage());
                call.cancel();
            }
        });
    }
    private void intentPersonal(int position){
        id = usersArr.get(position).getId();
        avata = usersArr.get(position).getAvata();
        coverimage = usersArr.get(position).getCoverimage();
        username = usersArr.get(position).getUsername();
        Intent personal = new Intent(this, PersonalActivity.class);
        personal.putExtra("iduser", id);
        personal.putExtra("avata",avata);
        personal.putExtra("coverimage", coverimage);
        personal.putExtra("username",username);
        startActivity(personal);
    }
}