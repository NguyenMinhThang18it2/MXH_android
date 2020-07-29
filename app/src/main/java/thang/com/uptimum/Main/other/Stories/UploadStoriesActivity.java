package thang.com.uptimum.Main.other.Stories;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
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
import thang.com.uptimum.model.Error;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.StoryRetrofit;
import thang.com.uptimum.upload.UploadPostsActivity;

public class UploadStoriesActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "UploadStoriesActivity";
    private static final int PICK_IMAGESSTR_REQUEST = 441;
    private TextView UploadStories;
    private Button btnSelectImg;
    private RecyclerView rcvShowStorie;
    private Toolbar toolbar;

    private NetworkUtil networkUtil;
    private Retrofit retrofit;

    private Uri uriAvata;
    private StoryRetrofit storyRetrofit;
    private String realPathfile ="";

    private SharedPreferences sessionManagement;
    private String id="";

    private ArrayList<Uri> uriArr;
    private ArrayList<String> realPathfileArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_stories);
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        uriArr = new ArrayList<>();
        realPathfileArray = new ArrayList<>();

        UploadStories = (TextView) findViewById(R.id.UploadStories);
        btnSelectImg = (Button) findViewById(R.id.btnSelectImgStories);
        rcvShowStorie = (RecyclerView) findViewById(R.id.rcvShowStorie);
        rcvShowStorie.setHasFixedSize(true);

        toolbar = (Toolbar) findViewById(R.id.toolbarUploadStories);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tạo tin mới");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sessionManagement = this.getSharedPreferences("userlogin",Context.MODE_PRIVATE);
        id = sessionManagement.getString("id","");

        btnSelectImg.setOnClickListener(this);
        UploadStories.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSelectImgStories:
                Toast.makeText(this, "bababab", Toast.LENGTH_SHORT).show();
                selectImgGallery();
                break;
            case R.id.UploadStories:
                postStories();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data != null){
            ClipData clipData = null;
            if(requestCode == PICK_IMAGESSTR_REQUEST){
                if(data.getClipData() != null){
                    Log.d(TAG," "+ data.getClipData());
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
        rcvShowStorie.setLayoutManager(new StaggeredGridLayoutManager(
                countStaggeredGrid, StaggeredGridLayoutManager.VERTICAL)
        );
        AdapterSelectImage adapterSelectImage = new AdapterSelectImage(arrayList, this);
        rcvShowStorie.setAdapter(adapterSelectImage);
    }
    private void selectImgGallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
//        intent.setAction(intent.ACTION_GET_CONTENT); // chỗ này +++
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent.createChooser(intent,"Sellect file img"), PICK_IMAGESSTR_REQUEST);
    }
    private void postStories(){
        List<MultipartBody.Part> parts = new ArrayList<>();
        Log.d(TAG, " "+ realPathfileArray.size());
        for(int i = 0; i < realPathfileArray.size(); i++){
            File file = new File(realPathfileArray.get(i));
            String file_path = file.getAbsolutePath();
            Log.d(TAG, " "+ file_path);
            RequestBody requestBody = RequestBody.create(file, MediaType.parse("multipart/form-data"));
            parts.add(MultipartBody.Part.createFormData("image",file_path,requestBody));
        }
        storyRetrofit = retrofit.create(StoryRetrofit.class);

        Call<Error> errorCall = storyRetrofit.postStory(id,parts);
        errorCall.enqueue(new Callback<Error>() {
            @Override
            public void onResponse(Call<Error> call, Response<Error> response) {
                Error errors = response.body();
                if(!errors.isSuccess()){
                    Toast.makeText(UploadStoriesActivity.this, "Posts Fail", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(UploadStoriesActivity.this, "Posts new Success", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Error> call, Throwable t) {
                Log.d(TAG," "+t.getMessage());
            }
        });
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
