//package thang.com.uptimum.Main.other;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.app.FragmentManager;
//import android.content.ContentUris;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.res.Resources;
//import android.database.Cursor;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.DocumentsContract;
//import android.provider.MediaStore;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.VideoView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.cardview.widget.CardView;
//
//
//import com.google.android.material.bottomsheet.BottomSheetBehavior;
//import com.google.android.material.bottomsheet.BottomSheetDialog;
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
//import com.squareup.picasso.Picasso;
//
//import java.io.File;
//import java.net.URISyntaxException;
//
//import io.socket.client.IO;
//import io.socket.client.Socket;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import thang.com.uptimum.R;
//import thang.com.uptimum.model.Error;
//import thang.com.uptimum.network.NetworkUtil;
//import thang.com.uptimum.network.PostsRetrofit;
//
//import static thang.com.uptimum.util.Constants.BASE_URL;
//
//
//public class CommentActivity extends BottomSheetDialogFragment {
//    private BottomSheetBehavior bottomSheetBehavior;
//    public static CommentActivity newInstance() {
//        return new CommentActivity();
//    }
//    public void onStart() {
//        super.onStart();
////        bottomSheetBehavior.setHideable(false);
////        bottomSheetBehavior.setPeekHeight(1000);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//    }
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        BottomSheetDialog dialog = new BottomSheetDialog(getContext(),R.style.BottomSheetDialogTheme);
////        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
//        View view = View.inflate(getContext(), R.layout.activity_comment, null);
//        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.height = getScreenHeight() + 200;
//        view.setLayoutParams(params);
//        dialog.setContentView(view);
//        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
//        return dialog;
//    }
//
//    public static int getScreenHeight() {
//        return Resources.getSystem().getDisplayMetrics().heightPixels;
//    }
//}
