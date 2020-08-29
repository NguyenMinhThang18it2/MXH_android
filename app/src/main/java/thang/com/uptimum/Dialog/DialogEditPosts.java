package thang.com.uptimum.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.R;
import thang.com.uptimum.model.Error;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.PostsRetrofit;

public class DialogEditPosts extends DialogFragment {
    private static final String TAG = "DialogEditPosts";
    private String idposts="", document="", token ="";
    private View view;
    private EditText etEditPosts;
    private Context context;

    private NetworkUtil networkUtil;
    private Retrofit retrofit;
    private PostsRetrofit postsRetrofit;
    private SharedPreferences sessionManagement;
    public DialogEditPosts(Context context, String idposts, String document) {
        this.idposts = idposts;
        this.document = document;
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_edit_posts,null);
        etEditPosts = (EditText) view.findViewById(R.id.etEditPosts);
        etEditPosts.setText(document);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        editPosts();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogEditPosts.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
    private void editPosts(){
        sessionManagement = getContext().getApplicationContext().getSharedPreferences("userlogin", Context.MODE_PRIVATE);
//        id = sessionManagement.getString("id","");
//        avata = sessionManagement.getString("avata", "");
//        coverimage = sessionManagement.getString("coverimage", "");
//        username = sessionManagement.getString("username","");
        token = "Bearer "+sessionManagement.getString("token","");
        postsRetrofit = retrofit.create(PostsRetrofit.class);
        Call<Error> errorCall = postsRetrofit.putPosts(token, idposts, etEditPosts.getText().toString());
        errorCall.enqueue(new Callback<Error>() {
            @Override
            public void onResponse(Call<Error> call, Response<Error> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, " lỗi respone");
                }else{
                    Error error = response.body();
                    if(error.isSuccess()){
                        Toast.makeText(context, "Chỉnh sữa thành công", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Chỉnh sữa thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<Error> call, Throwable t) {
                Log.d(TAG, " Lỗi "+t.getMessage() );
                call.cancel();
            }
        });
        DialogEditPosts.this.getDialog().dismiss();
    }
}
