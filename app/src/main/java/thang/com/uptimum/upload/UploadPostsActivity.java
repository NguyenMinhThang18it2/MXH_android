package thang.com.uptimum.upload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.R;
import thang.com.uptimum.adapter.AdapterSelectImage;
import thang.com.uptimum.adapter.themestatusAdapter;
import thang.com.uptimum.model.Error;
import thang.com.uptimum.model.ThemeStatus;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.PostsRetrofit;
import thang.com.uptimum.network.StoryRetrofit;
import thang.com.uptimum.network.ThemeStatusRetrofit;
import thang.com.uptimum.util.GetFilePath;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static androidx.core.content.FileProvider.getUriForFile;
import static thang.com.uptimum.util.Constants.BASE_URL;

public class UploadPostsActivity extends AppCompatActivity implements View.OnClickListener {
    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout menuUploadStatus, menuUploadStatausMinimize;
    private ImageView imgUpload, btnBackUpload;
    private VideoView videoUpload;
    private TextView btnUpload;
    private CardView btnUploadnow, btnUploadVideo, uploadCamera;
    private EditText eddocument;
    public static EditText textareaBackground;
    public static String Theme ="theme-1591455083782-85538156.jpg";
    private String documentStatus = "";
    private RecyclerView recylerTheme, rcvSelectmultiple;
    private LinearLayoutManager linearLayoutManagersTheme, linearLayoutManagerSelectMultiple;
    private FrameLayout framevideo;

    private ArrayList<ThemeStatus> arrayTheme;
    private ArrayList<Uri> uriArr;
    private themestatusAdapter adapterTheme;
    private ThemeStatusRetrofit themeStatusRetrofit;
    
    private static final int MY_CAMERA_PERMISSION_CODE = 1000;
    private static final int MY_PERMISSIONS_REQUET = 100;
    private static final int PICK_IMAGES_REQUEST = 200;
    private static final int PICK_VIDEO_REQUEST = 300;
    private static final int PICK_CAMERA_REQUEST = 400;

    private Uri uriAvata;
    private ArrayList<String> realPathfileArray;

    private NetworkUtil networkUtil;
    private PostsRetrofit postsRetrofit;
    private String realPathfile="";

    private SharedPreferences sessionManagement;
    private String id="";

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTheme();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_posts);
        imgUpload = (ImageView) findViewById(R.id.imgUpload);
        btnUpload = (TextView) findViewById(R.id.btnUpload);
        btnBackUpload = (ImageView) findViewById(R.id.btnBackUpload);
        btnUploadnow = (CardView) findViewById(R.id.btnUploadnow);
        btnUploadVideo = (CardView) findViewById(R.id.btnUploadVideo);
        uploadCamera = (CardView) findViewById(R.id.uploadCamera);
        eddocument = (EditText) findViewById(R.id.eddocument);
        videoUpload = (VideoView) findViewById(R.id.videoUpload);
        framevideo = (FrameLayout) findViewById(R.id.framevideo);
        textareaBackground = (EditText) findViewById(R.id.textareaBackground);
        menuUploadStatus = (LinearLayout) findViewById(R.id.menuUploadStatus);
        menuUploadStatausMinimize = (LinearLayout) findViewById(R.id.menuUploadStatausMinimize);
        //
        recylerTheme = (RecyclerView) findViewById(R.id.recylerTheme);
        recylerTheme.setHasFixedSize(true);
        linearLayoutManagersTheme =  new LinearLayoutManager
                (UploadPostsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recylerTheme.setLayoutManager(linearLayoutManagersTheme);
        //
        rcvSelectmultiple = (RecyclerView) findViewById(R.id.rcvSelectmultiple);
        rcvSelectmultiple. setHasFixedSize(true);
        uriArr = new ArrayList<>();
        realPathfileArray = new ArrayList<>(); // mảng này lưu đường dẫn đúng của file upload
        networkUtil = new NetworkUtil();
        //
        if(ContextCompat.checkSelfPermission(UploadPostsActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(UploadPostsActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUET);
        }
        // lấy iduser đăng nhập
        sessionManagement = UploadPostsActivity.this.getSharedPreferences("userlogin",Context.MODE_PRIVATE);
        id = sessionManagement.getString("id","");

        checkImgCamera(savedInstanceState);
        addEvents();
        loadTheme();
        final View activityRootView = findViewById(R.id.upload_new_status);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //r will be populated with the coordinates of your view that area still visible.
                activityRootView.getWindowVisibleDisplayFrame(r);

                int heightDiff = activityRootView.getRootView().getHeight() - r.height();
                if (heightDiff > 0.25*activityRootView.getRootView().getHeight()) { // if more than 25% of the screen, its probably a keyboard...
                    Log.d("keyboard", " showwwwwwwwwww");
                    menuUploadStatus.setVisibility(View.GONE); // ẩn menu
                    menuUploadStatausMinimize.setVisibility(View.VISIBLE);// hiện menu mini
                    if(realPathfile.equals("")){
                        WritestatusTheme();
                    }
                }else{
                    Log.d("keyboard", " hideeeeeeeeee aa "+Theme);
                }
            }
        });
    }

    private void loadTheme() {
        arrayTheme = new ArrayList<>();
        Retrofit retrofit = networkUtil.getRetrofit();
        themeStatusRetrofit = retrofit.create(ThemeStatusRetrofit.class);
        Call<List<ThemeStatus>> calltheme = themeStatusRetrofit.getTheme();
        calltheme.enqueue(new Callback<List<ThemeStatus>>() {
            @Override
            public void onResponse(Call<List<ThemeStatus>> call, Response<List<ThemeStatus>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(UploadPostsActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<ThemeStatus> themes = response.body();
                for(ThemeStatus theme : themes){
                    arrayTheme.add(theme);
                }
                adapterTheme = new themestatusAdapter(arrayTheme, getApplicationContext());
                recylerTheme.setAdapter(adapterTheme);
            }
            @Override
            public void onFailure(Call<List<ThemeStatus>> call, Throwable t) {
                Log.d("Lỗi themestatus", "a : "+t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBackUpload:
                finish();
                break;
            case R.id.btnUploadnow:
                getfileUploadStatus();
                break;
            case R.id.btnUpload:
                UploadStatus();
                break;
            case R.id.btnUploadVideo:
                getfileVideo();
                break;
            case R.id.uploadCamera:
                getfileCamera();
                break;
            case R.id.menuUploadStatausMinimize:
                menuUploadStatausMinimize.setVisibility(View.GONE);
                menuUploadStatus.setVisibility(View.VISIBLE);
            default:
                break;
        }
    }
    private void addEvents() {
        eddocument.setOnClickListener(this);
        btnBackUpload.setOnClickListener(this);
        btnUploadnow.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnUploadVideo.setOnClickListener(this);
        uploadCamera.setOnClickListener(this);
        menuUploadStatausMinimize.setOnClickListener(this);
    }
    private void WritestatusTheme(){
        eddocument.setVisibility(View.GONE);
        textareaBackground.setVisibility(View.VISIBLE);
        documentStatus = textareaBackground.getText().toString();
        textareaBackground.setText(documentStatus);
        if(Theme.equals("theme-1591455083782-85538156.jpg"))
        Picasso.get().load(BASE_URL+"uploads/"+Theme).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                textareaBackground.setBackground(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });// 3 4 5 10 11 12
    }
    private void getfileUploadStatus(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
//        intent.setAction(intent.ACTION_GET_CONTENT); // chỗ này +++
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent.createChooser(intent,"Sellect file img"), PICK_IMAGES_REQUEST);
    }
    private void getfileVideo(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent,"Sellect file video"), PICK_VIDEO_REQUEST);
    }
    private void getfileCamera(){
       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
           if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                   || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
               String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
               requestPermissions(permission, MY_CAMERA_PERMISSION_CODE);
           }
           else{
               openFileCamera();
           }
       }else{
           openFileCamera();
       }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openFileCamera();
                }else{
                    Toast.makeText(this, "Permission deined ....", Toast.LENGTH_SHORT).show();
                }
        }
    }
    private void openFileCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            File photoFile = createPhotoFile();
            if(photoFile!=null){
                realPathfile = photoFile.getAbsolutePath();
                Log.d("qwe123123213", " "+realPathfile);
                Uri photoUri = FileProvider.getUriForFile(UploadPostsActivity.this,"thang.com.uptimum.fileprovider",photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, 1);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ViewGroup.LayoutParams params = eddocument.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        ClipData clipData = null;
        if (resultCode == RESULT_OK && data != null && data != null){
            if(requestCode == PICK_IMAGES_REQUEST){
                if(data.getClipData() != null){

                    clipData = data.getClipData();
                    for(int i = 0; i < clipData.getItemCount(); i++){
                        uriAvata = clipData.getItemAt(i).getUri();
                        uriArr.add(uriAvata);
                        realPathfile = getPathFromURI(getApplicationContext(),uriAvata);
                        realPathfileArray.add(realPathfile);
                        Log.d("kjsdasldas", "1 "+uriArr.size());
                    }
                }else{
                    uriAvata = data.getData();
                    uriArr.add(uriAvata);
                    realPathfile = getPathFromURI(getApplicationContext(),uriAvata);
                    realPathfileArray.add(realPathfile);
                }
                if(data.getClipData() != null) {
                    if (realPathfileArray.size() > clipData.getItemCount()) // khi khởi động lại thì nó tự thêm vào một cái path "/" nên phải xóa
                        realPathfileArray.remove(0);
                }
                else {
                    if (realPathfileArray.size() > 1)
                        realPathfileArray.remove(0);
                }
                showSelectMutilFile(uriArr);
                menuUploadStatausMinimize.setVisibility(View.VISIBLE);
                framevideo.setVisibility(View.GONE);
                eddocument.setLayoutParams(params);
                menuUploadStatus.setVisibility(View.GONE);
            }
            if(requestCode == PICK_VIDEO_REQUEST){
                uriAvata = data.getData();
                Log.d("kjsdasldas", "2 "+data.getData());
                realPathfile = getPathFromURI(getApplicationContext(),uriAvata);
                realPathfileArray.add(realPathfile);
                if(realPathfileArray.size() > 1){ // khi khởi động lại thì nó tự thêm vào một cái path "/" nên phải xóa
                    realPathfileArray.remove(0);
                }
                Log.d("video", " "+realPathfileArray.size());
                imgUpload.setImageDrawable(null);
                framevideo.setVisibility(View.VISIBLE);
                eddocument.setLayoutParams(params);
                menuUploadStatus.setVisibility(View.GONE);
                videoUpload.setVideoURI(uriAvata);
                MediaController mediaController = new MediaController(UploadPostsActivity.this);
                videoUpload.setMediaController(mediaController);
                mediaController.setAnchorView(videoUpload);
                menuUploadStatausMinimize.setVisibility(View.VISIBLE);
            }
        }
    }
    private void showSelectMutilFile(ArrayList<Uri> arrayList){
        int countStaggeredGrid = 0;
        if(arrayList.size() < 2) {
            countStaggeredGrid = 1;
        } else if(arrayList.size() >= 3 && arrayList.size() % 3 == 0){
            countStaggeredGrid = 3;
        }else{
            countStaggeredGrid = 2;
        }
        rcvSelectmultiple.setLayoutManager(new StaggeredGridLayoutManager(
                countStaggeredGrid, StaggeredGridLayoutManager.VERTICAL)
        );
        AdapterSelectImage adapterSelectImage = new AdapterSelectImage(arrayList, UploadPostsActivity.this);
        rcvSelectmultiple.setAdapter(adapterSelectImage);
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("onSaveInstanceState", " "+ realPathfile);
        outState.putString("imgFromCamera", realPathfile);
    }

    private void UploadStatus(){
        Log.d("lozzaskndlasdlkasd", " "+realPathfile);
        if(!realPathfile.equals("")){
            UploadFileStatus();
        }else{
            UploadBacgroundStatus();
        }
    }
    private void UploadFileStatus(){

        // get id user

        RequestBody iduser = RequestBody.create(id, MultipartBody.FORM);
        RequestBody document = RequestBody.create(eddocument.getText().toString(), MultipartBody.FORM);

//        MultipartBody.Part mulitibodyPart =  MultipartBody.Part.createFormData("image",file_path,requestBody);

        List<MultipartBody.Part> parts = new ArrayList<>();
        Log.d("kjsdasldas", " "+ realPathfileArray.size());
        for(int i = 0; i < realPathfileArray.size(); i++){
            File file = new File(realPathfileArray.get(i));
            String file_path = file.getAbsolutePath();
            Log.d("kjsdasldas", " "+ file_path);
            RequestBody requestBody = RequestBody.create(file, MediaType.parse("multipart/form-data"));
            parts.add(MultipartBody.Part.createFormData("image",file_path,requestBody));
        }
        Log.d("file_path", " "+ parts);
        Retrofit retrofit = networkUtil.getRetrofit();
        postsRetrofit = retrofit.create(PostsRetrofit.class);

        Call<Error> call = postsRetrofit.postPossts(iduser, document, parts);
        call.enqueue(new Callback<Error>() {
            @Override
            public void onResponse(Call<Error> call, Response<Error> response) {
                Error errors = response.body();
                if(!errors.isSuccess()){
                    Toast.makeText(UploadPostsActivity.this, "Posts Fail", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(UploadPostsActivity.this, "Posts new Success", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Error> call, Throwable t) {
                Log.d("Kết quả là đây: "," "+t.getMessage());
            }
        });
    }
    private void UploadBacgroundStatus(){
        String iduser = id;
        String documentBackground = textareaBackground.getText().toString();
        String themeBacgroung = Theme;
        Retrofit retrofit = networkUtil.getRetrofit();
        postsRetrofit = retrofit.create(PostsRetrofit.class);
        Log.d("aaaaaaaaaaaaaaaaaaa", ""+id+" text = "+ textareaBackground.getText().toString()+" 123"+Theme);
        Call<Error> call = postsRetrofit.postBackground(iduser, documentBackground, themeBacgroung);
        call.enqueue(new Callback<Error>() {
            @Override
            public void onResponse(Call<Error> call, Response<Error> response) {
                Error errors = response.body();
                if(!errors.isSuccess()){
                    Toast.makeText(UploadPostsActivity.this, "Posts Fail", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(UploadPostsActivity.this, "Posts new Success", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Error> call, Throwable t) {
                Log.d("Kết quả là đây: "," "+t.getMessage());
            }
        });
    }
    private void checkImgCamera( Bundle savedInstanceState ){
        if(savedInstanceState != null) {

            realPathfile = savedInstanceState.getString("imgFromCamera");
            realPathfileArray.add(realPathfile);
            Log.d("kqwjljjljl123", "má mày nha"+realPathfile);
            Bitmap bitmap = BitmapFactory.decodeFile(realPathfile);
            imgUpload.setImageBitmap(bitmap);
            imgUpload.setVisibility(View.VISIBLE);
            framevideo.setVisibility(View.GONE);
            menuUploadStatus.setVisibility(View.GONE);
        }
    }
    private File createPhotoFile(){
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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






























