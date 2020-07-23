package thang.com.uptimum.Main.other.Personal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Retrofit;
import thang.com.uptimum.Dialog.DialogShowImgPersonal;
import thang.com.uptimum.R;
import thang.com.uptimum.network.NetworkUtil;

public class ShowImagePersonalActivity extends AppCompatActivity {
    private final String TAG = "ShowImagePersonalActivity";

    private RecyclerView rcvImgPersonal;

    private String id="";
    private ArrayList<String> arrayList;
    private AdapterShowImgPersonal AdapterShowImgPersonal;
    private AdapterShowImgPersonal.OnClickImgPersonalListener listener;
//    private NetworkUtil networkUtil;
//    private Retrofit retrofit;
//    private
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_personal);
//        networkUtil = new NetworkUtil();
//        retrofit = networkUtil.getRetrofit();
        rcvImgPersonal = (RecyclerView) findViewById(R.id.rcvImgPersonal);
        rcvImgPersonal.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        rcvImgPersonal.setLayoutManager(gridLayoutManager);
        setOnlickitemImg();
        getImg();
        showImg();
    }
    private void getImg(){
        arrayList = new ArrayList<>();
        arrayList.clear();
        Intent intent = getIntent();
        id = intent.getStringExtra("idpersonal");
        arrayList = intent.getStringArrayListExtra("arrPosts");
    }
    private void showImg(){

        AdapterShowImgPersonal = new AdapterShowImgPersonal(arrayList, this.getApplicationContext(), listener);
        rcvImgPersonal.setAdapter(AdapterShowImgPersonal);
    }
    private void setOnlickitemImg(){
        listener = new AdapterShowImgPersonal.OnClickImgPersonalListener() {
            @Override
            public void onclik(ImageView imageView, int position) {
                DialogShowImgPersonal dialogShowImgPersonal = new DialogShowImgPersonal(arrayList, position);
                dialogShowImgPersonal.show(getSupportFragmentManager(),"dialog show img personal");
            }
        };
    }
}
