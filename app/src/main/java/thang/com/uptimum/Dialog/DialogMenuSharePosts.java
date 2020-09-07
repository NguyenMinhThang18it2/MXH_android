package thang.com.uptimum.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.Main.HomeFragment;
import thang.com.uptimum.R;
import thang.com.uptimum.model.Error;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.PostsRetrofit;

import static thang.com.uptimum.Socket.SocketIO.socket;
import static thang.com.uptimum.util.Constants.BASE_URL;

public class DialogMenuSharePosts extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String TAG = "DialogMenuSharePosts";
    private View view;
    private SharedPreferences sessionManagement;
    private Context context;
    private String idposts= "",iduserPosts= "", token = "", iduserlogin ="", avata ="", username= "";

    private CircleImageView user;
    private TextView txtusername;
    private EditText etDocument;
    private Button btnSharePosts;

    private NetworkUtil networkUtil;
    private Retrofit retrofit;
    private PostsRetrofit postsRetrofit;

    public DialogMenuSharePosts(Context context, String idposts, String iduserPosts) {
        this.context = context;
        this.idposts = idposts;
        this.iduserPosts = iduserPosts;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        view = View.inflate(getContext(), R.layout.menu_posts_share, null);
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        mapingView();
        getDataUserLogin();

        bottomSheetDialog.setContentView(view);
        return bottomSheetDialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSharePosts:
                sharePosts();
//                HomeFragment homeFragment = HomeFragment.newInstance();
//                homeFragment.refresh();
                DialogMenuSharePosts.this.getDialog().dismiss();
                updateNotification();
                break;
        }
    }
    private void mapingView(){
        user = (CircleImageView) view.findViewById(R.id.user);
        txtusername = (TextView) view.findViewById(R.id.txtusername);
        etDocument = (EditText) view.findViewById(R.id.etDocument);
        btnSharePosts = (Button) view.findViewById(R.id.btnSharePosts);

        btnSharePosts.setOnClickListener(this);
    }
    private void getDataUserLogin(){
        sessionManagement = context.getApplicationContext().getSharedPreferences("userlogin", Context.MODE_PRIVATE);
        iduserlogin = sessionManagement.getString("id","");
        avata = sessionManagement.getString("avata", "");
        username = sessionManagement.getString("username","");
        token = "Bearer "+sessionManagement.getString("token","");

        Glide.with(context).load(BASE_URL+"uploads/"+avata).into(user);
        txtusername.setText(username);
    }
    private void sharePosts(){
        Log.d(TAG, " "+ iduserlogin + " "+ etDocument.getText().toString() + " "+idposts);
        postsRetrofit = retrofit.create(PostsRetrofit.class);
        Call<Error> errorCall = postsRetrofit.sharePosts(token, iduserlogin, etDocument.getText().toString(), idposts);
        errorCall.enqueue(new Callback<Error>() {
            @Override
            public void onResponse(Call<Error> call, Response<Error> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, " lỗi respone"+response);
                }else{
                    Error error = response.body();
                    if(error.isSuccess()){
                        Toast.makeText(context, "chia sẽ thành công", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "chia sẽ thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<Error> call, Throwable t) {
                Log.d(TAG, "lỗi "+ t.getMessage());
                call.cancel();
            }
        });
    }
    private void updateNotification(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idposts", idposts);
            jsonObject.put("iduser", iduserPosts);
            jsonObject.put("iduserShare", iduserlogin);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("share posts to server", jsonObject);
    }
    // idposts, iduser notification, iduserShare    share posts to server
}
