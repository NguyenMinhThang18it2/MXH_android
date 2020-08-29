package thang.com.uptimum.Dialog;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.emitter.Emitter;
import io.supercharge.shimmerlayout.ShimmerLayout;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.Main.PersonalActivity;
import thang.com.uptimum.R;
import thang.com.uptimum.adapter.AdapterReplyCmt;
import thang.com.uptimum.adapter.commentAdapter;
import thang.com.uptimum.model.Comment;
import thang.com.uptimum.model.Error;
import thang.com.uptimum.model.ReplyComment;
import thang.com.uptimum.model.ReplyListCmt;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.PostsRetrofit;
import thang.com.uptimum.network.ReplyCommentRetrofit;

import static android.app.Activity.RESULT_OK;
import static thang.com.uptimum.Socket.SocketIO.socket;
import static thang.com.uptimum.util.Constants.BASE_URL;

public class CommentBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener{
    private final String TAG = "Comment";
    private View view;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView recyclerViewCmt;
    private ArrayList<Comment> commentArrayList;
    private commentAdapter commentAdapter;
    private SharedPreferences sessionManagement ;
    private String iduser, token, idCmt ="" ;
    private String idPostsClick;
    private ImageView btnBackUpload, sentCmt, PostImgCmt, btnBackUploadReplyCmt;
    private RoundedImageView UploadImgCmt, roundedImageView;
    private EditText documentCmt;
    private CircleImageView avataUserCmt;
    private TextView txtNameUsercmt, txtUserCmt, txtNumberLikeCmt;
    private LinearLayoutManager linearLayoutManagerCmt;
    private RecyclerView recyclerViewCmtdialog, recyclerViewReplyCmt;
    private RelativeLayout linearUploadImgCmt, headertopReplyCmt, headertopcmt, rltReplyCmt, rltCmt, rltRecyclerview, linearRecyclerReplyCmt;
    private LinearLayout btnCloseImgCmt;
    private static final int PICK_IMAGESCMT_REQUEST = 221;
    private Uri uriAvata;
    private String realPathfile ="";
    private NetworkUtil networkUtil;
    private Retrofit retrofit;
    private JSONObject idcmtimg;
    private ShimmerLayout shimmerLayout, ShimmerLayoutReplyCmt;

    private AdapterReplyCmt adapterReplyCmt;
    private ArrayList<ReplyListCmt> arrayReplyCmt;
    private ReplyCommentRetrofit replyCommentRetrofit;

    private commentAdapter.onClickListener mListener;
    public static CommentBottomSheetDialog newInstance(String idPostsClick) {
        CommentBottomSheetDialog commentBottomSheetDialog = new CommentBottomSheetDialog();
        Bundle bundle = new Bundle();
        bundle.putString("idPostsClick", idPostsClick);
        commentBottomSheetDialog.setArguments(bundle);
        return commentBottomSheetDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        view = View.inflate(getContext(), R.layout.activity_comment, null);
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        arrayReplyCmt = new ArrayList<>();
        // join room cmt
        idPostsClick = getArguments().getString("idPostsClick", "");
        socket.emit("join room cmt", idPostsClick);
        // view
        mapingView();
        getDataLogin();
        setrecyclerView();
        OnListenerCmt();

        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = getScreenHeight()+200;
        view.setLayoutParams(params);
        bottomSheetDialog.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        // get data comment
        commentArrayList = new ArrayList<>();
        getdataComment(idPostsClick);
        //comment
        socket.on("id commentposts android", sendImg);
        socket.on("get commentposts", getCmt);

        btnBackUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                socket.emit("leave room cmt", idPostsClick);
            }
        });
        return bottomSheetDialog;
    }
    private void getDataLogin(){
        sessionManagement = getContext().getApplicationContext().getSharedPreferences("userlogin", Context.MODE_PRIVATE);
        iduser = sessionManagement.getString("id","");
        token = "Bearer "+sessionManagement.getString("token","");
    }
    private void mapingView(){
        linearRecyclerReplyCmt = (RelativeLayout) view.findViewById(R.id.linearRecyclerReplyCmt);
        rltRecyclerview = (RelativeLayout) view.findViewById(R.id.rltRecyclerview);
        ShimmerLayoutReplyCmt = (ShimmerLayout) view.findViewById(R.id.ShimmerLayoutReplyCmt);
        shimmerLayout = (ShimmerLayout) view.findViewById(R.id.shimmerLayout);
        btnCloseImgCmt = (LinearLayout) view.findViewById(R.id.btnCloseImgCmt);
        linearUploadImgCmt = (RelativeLayout) view.findViewById(R.id.linearUploadImgCmt);
        UploadImgCmt = (RoundedImageView) view.findViewById(R.id.UploadImgCmt);
        btnBackUpload = (ImageView) view.findViewById(R.id.btnBackUploadCmt);
        documentCmt = (EditText) view.findViewById(R.id.documentCmt);
        sentCmt = (ImageView) view.findViewById(R.id.sentCmt);
        PostImgCmt = (ImageView) view.findViewById(R.id.PostImgCmt);
        headertopReplyCmt = (RelativeLayout) view.findViewById(R.id.headertopReplyCmt);
        headertopcmt = (RelativeLayout) view.findViewById(R.id.headertopcmt);
        rltReplyCmt = (RelativeLayout) view.findViewById(R.id.rltReplyCmt);
        rltCmt = (RelativeLayout) view.findViewById(R.id.rltCmt);
        btnBackUploadReplyCmt = (ImageView) view.findViewById(R.id.btnBackUploadReplyCmt);
        avataUserCmt = (CircleImageView) view.findViewById(R.id.avataUserCmt);
        txtNameUsercmt = (TextView) view.findViewById(R.id.txtNameUsercmt);
        txtUserCmt = (TextView) view.findViewById(R.id.txtUserCmt);
        txtNumberLikeCmt = (TextView) view.findViewById(R.id.txtNumberLikeCmt);
        roundedImageView = (RoundedImageView) view.findViewById(R.id.roundedImageView);
        recyclerViewReplyCmt = (RecyclerView) view.findViewById(R.id.recyclerViewReplyCmt);

        btnBackUploadReplyCmt.setOnClickListener(this);
        btnCloseImgCmt.setOnClickListener(this);
        PostImgCmt.setOnClickListener(this);
        sentCmt.setOnClickListener(this);
    }
    private void setrecyclerView(){
        recyclerViewCmtdialog = (RecyclerView) view.findViewById(R.id.recyclerViewCmt);
        recyclerViewCmtdialog.setHasFixedSize(true);
        linearLayoutManagerCmt =  new LinearLayoutManager
                (getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManagerCmt.setStackFromEnd(true);
        recyclerViewCmtdialog.setLayoutManager(linearLayoutManagerCmt);
        recyclerViewCmt = recyclerViewCmtdialog;
        //
        recyclerViewReplyCmt.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerReplyCmt = new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false
        );
        recyclerViewReplyCmt.setLayoutManager(linearLayoutManagerReplyCmt);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBackUploadReplyCmt:
                backToCmt();
                break;
            case R.id.PostImgCmt:
                getfileUploadStatus();
                break;
            case R.id.sentCmt:
                checkSendCmt();
                break;
            case R.id.btnCloseImgCmt:
                UploadImgCmt.setImageDrawable(null);
                linearUploadImgCmt.setVisibility(View.GONE);
                break;
        }
    }
    // kiểm tra xem đang cmt bài viết hay đang trả lời cmt
    private void checkSendCmt(){
        if(!TextUtils.isEmpty(documentCmt.getText().toString())){
            if(rltCmt.getVisibility() == View.VISIBLE){
                sendCmt(idPostsClick, documentCmt.getText().toString());
            }else if(rltReplyCmt.getVisibility() == View.VISIBLE){
                sendReplyCmt(idPostsClick, documentCmt.getText().toString());
            }
            documentCmt.setText("");
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null){
            if(requestCode == PICK_IMAGESCMT_REQUEST){
                uriAvata = data.getData();
                realPathfile = getPathFromURI(getContext(),uriAvata);
                linearUploadImgCmt.setVisibility(View.VISIBLE);
                Picasso.get().load(uriAvata).into(UploadImgCmt);
            }
        }
    }
    private void backToCmt(){
        socket.emit("leave room cmt", idCmt);
        rltCmt.setVisibility(View.VISIBLE);
        rltReplyCmt.setVisibility(View.INVISIBLE);
        avataUserCmt.setImageDrawable(null);
        roundedImageView.setImageDrawable(null);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) roundedImageView.getLayoutParams();
        layoutParams.width = 600;
        layoutParams.height = 0;
        roundedImageView.setLayoutParams(layoutParams);
    }
    private void replyCmt(int position){
        rltCmt.setVisibility(View.INVISIBLE);

        rltReplyCmt.setVisibility(View.VISIBLE);
        idCmt = commentArrayList.get(position).getId();
        socket.emit("join room cmt", idCmt);
        // set data in main Cmt
        txtNameUsercmt.setText(commentArrayList.get(position).getIduser().getUsername());
        txtUserCmt.setText(""+commentArrayList.get(position).getDocument());
        txtNumberLikeCmt.setText(""+commentArrayList.get(position).getNumberLike().length);
        Glide.with(getContext()).load(BASE_URL+"uploads/"+commentArrayList.get(position).getIduser().getAvata())
                .into(avataUserCmt);
        if(commentArrayList.get(position).getFile().getImageComment().length()>10){
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) roundedImageView.getLayoutParams();
            layoutParams.width = 600;
            layoutParams.height = 350;
            roundedImageView.setLayoutParams(layoutParams);
            Glide.with(getContext()).load(BASE_URL+"uploads/"+commentArrayList.get(position).getFile().getImageComment())
                    .into(roundedImageView);
        }
        // set data in adapter
        getDataReplyCmt();

    }
    private void getDataReplyCmt(){
        linearRecyclerReplyCmt.setVisibility(View.INVISIBLE);
        ShimmerLayoutReplyCmt.setVisibility(View.VISIBLE);
        ShimmerLayoutReplyCmt.startShimmerAnimation();
        replyCommentRetrofit = retrofit.create(ReplyCommentRetrofit.class);
        Call<ReplyComment> listCall = replyCommentRetrofit.getReplyCmt(token, idCmt);
        listCall.enqueue(new Callback<ReplyComment>() {
            @Override
            public void onResponse(Call<ReplyComment> call, Response<ReplyComment> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, " lỗi response");
                }else{
                    ReplyComment replyComments  = response.body();
                    arrayReplyCmt.clear();
                    for (ReplyListCmt replyListCmt: replyComments.getListCmt()){
                        arrayReplyCmt.add(replyListCmt);
                    }
                    adapterReplyCmt.notifyDataSetChanged();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ShimmerLayoutReplyCmt.stopShimmerAnimation();
                        linearRecyclerReplyCmt.setVisibility(View.VISIBLE);
                        ShimmerLayoutReplyCmt.setVisibility(View.GONE);
                    }
                }, 2000);
                call.cancel();
            }

            @Override
            public void onFailure(Call<ReplyComment> call, Throwable t) {
                Log.d(TAG, " lỗi " + t.getMessage());
                call.cancel();
                ShimmerLayoutReplyCmt.stopShimmerAnimation();
                linearRecyclerReplyCmt.setVisibility(View.VISIBLE);
                ShimmerLayoutReplyCmt.setVisibility(View.GONE);
            }
        });
        Log.d(TAG,"arrayReplyCmt "+arrayReplyCmt);
        adapterReplyCmt = new AdapterReplyCmt(arrayReplyCmt, getContext());
        recyclerViewReplyCmt.setAdapter(adapterReplyCmt);
    }
    private void OnListenerCmt(){
        mListener = new commentAdapter.onClickListener() {
            @Override
            public void onCickReply(TextView btnReplyCmt, int position) {
                replyCmt(position);
            }

            @Override
            public void onClickItemCmt(TextView txtUserCmt, int position) {
                DialogComment dialogComment = new DialogComment(getContext(), commentArrayList.get(position).getId(), commentArrayList.get(position).getDocument(), txtUserCmt);
                dialogComment.show(getFragmentManager(), "dialogmenucmt");
            }
        };
    }
    private void getfileUploadStatus(){

        Intent intent = new Intent(Intent.ACTION_PICK,null);
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGESCMT_REQUEST);
    }
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    private void getdataComment(String idPosts){
        Log.d("idposstsaaaaa", ""+idPosts);

        JSONObject id = new JSONObject();
        try {
            id.put("idposts", idPosts);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("json",""+id);
        // lấy tất cả comment
        socket.emit("show commentposts", id);
        socket.on("all commentposts", allDataComment);

    }
    private Emitter.Listener allDataComment = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    shimmerLayout.startShimmerAnimation();
                    String data = args[0].toString();
                    try {
                        JSONArray jsonarray = new JSONArray(data);

                        if (jsonarray != null) {
                            for (int i=0;i<jsonarray.length();i++){
                                JSONObject jsonObject = (JSONObject) jsonarray.get(i);
                                Gson gson = new Gson();
                                Comment comment = gson.fromJson(String.valueOf(jsonObject), Comment.class);
                                commentArrayList.add(comment);
                            }
                        }
                        commentAdapter = new commentAdapter(commentArrayList,getContext(), mListener);
                        recyclerViewCmt.setAdapter(commentAdapter);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                shimmerLayout.stopShimmerAnimation();
                                rltRecyclerview.setVisibility(View.VISIBLE);
                                shimmerLayout.setVisibility(View.GONE);
                            }
                        }, 2000);
                        Log.d("dataaaaaaa"," "+ commentArrayList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    private void sendCmt(String idPosts, String document){
        // comment idposts: idpostsajax,
        //            iduser: iduser,
        //            document: data,
        JSONObject postCmt = new JSONObject();
        try {
            postCmt.put("idposts", idPosts);
            postCmt.put("iduser", iduser);
            postCmt.put("document", document);
            postCmt.put("address", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        socket.emit("post commentposts",postCmt);
    }
    private void sendReplyCmt(String idPosts, String document){
        JSONObject postReplyCmt = new JSONObject();
        try {
            postReplyCmt.put("idcmt", idCmt);
            postReplyCmt.put("idposts", idPosts);
            postReplyCmt.put("iduserreply", iduser);
            postReplyCmt.put("document", document);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("reply comment to server",postReplyCmt);
        socket.on("reply comment to client", updateReplyCmt);
    }
    private Emitter.Listener sendImg = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    idcmtimg = new JSONObject();
                    String idabc = "";
                    try {
                        idabc = jsonObject.getString("id");
                        idcmtimg.put("id", idabc);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    uploadFileCmt(idabc);
                }
            });
        }
    };
    private Emitter.Listener getCmt = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];

                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("datacmtAll");
                        if (jsonArray != null) {
                            commentArrayList.clear();
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObjecta = (JSONObject) jsonArray.get(i);
                                Gson gson = new Gson();
                                Comment comment = gson.fromJson(String.valueOf(jsonObjecta), Comment.class);
                                commentArrayList.add(comment);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    commentAdapter.notifyDataSetChanged();

                    commentAdapter = new commentAdapter(commentArrayList,getContext(), mListener);
                    recyclerViewCmt.setAdapter(commentAdapter);
                }
            });
        }
    };
    private Emitter.Listener updateReplyCmt = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    getDataReplyCmt();
                }
            });
        }
    };
    private void uploadFileCmt(String idcmt){
        Log.d(TAG,"haha  "+idcmt);
        if(!realPathfile.isEmpty()){
            File file = new File(realPathfile);
            String file_path = file.getAbsolutePath();
            Log.d(TAG, " "+ file_path);
            RequestBody requestBody = RequestBody.create(file, MediaType.parse("multipart/form-data"));
            MultipartBody.Part part = MultipartBody.Part.createFormData("image",file_path, requestBody);
            PostsRetrofit postsRetrofit = retrofit.create(PostsRetrofit.class);
            Call<Error> errorCall = postsRetrofit.postFileCmt(token, idcmt, part);
            errorCall.enqueue(new Callback<Error>() {
                @Override
                public void onResponse(Call<Error> call, Response<Error> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                    }else{
                        Error errors = response.body();
                        if(!errors.isSuccess()){
                            Toast.makeText(getContext(), "Cmt Fail", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getContext(), "Cmt new Success", Toast.LENGTH_SHORT).show();
                            Log.d(TAG,"haha  "+errors.getMsg());
                            socket.emit("id getcommentposts", idcmtimg);
                        }
                    }
                    call.cancel();
                }

                @Override
                public void onFailure(Call<Error> call, Throwable t) {
                    Toast.makeText(getContext(), "lỗi", Toast.LENGTH_SHORT).show();
                    Log.d(TAG," "+t.getMessage());
                    call.cancel();
                }
            });
        }else{
            socket.emit("id getcommentposts", idcmtimg);
        }
    }


    public static String getPathFromURI(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
