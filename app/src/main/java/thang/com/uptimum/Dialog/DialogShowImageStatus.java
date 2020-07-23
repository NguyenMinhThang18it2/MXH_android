package thang.com.uptimum.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import thang.com.uptimum.Dialog.adapterDialog.ImgStatusViewpagerAdapter;
import thang.com.uptimum.Main.SwipeTouch.OnSwipeTouchListener;
import thang.com.uptimum.R;
import thang.com.uptimum.model.Posts;

import static thang.com.uptimum.util.Constants.BASE_URL;

public class DialogShowImageStatus extends DialogFragment {
    private LinearLayout tinShowImgDialog, buttonShowImgDialog;
    private View ViewTouch;
    private RelativeLayout menuShowImgDialog;
    private TextView txtLikeDialog,usernamedialog;
    private ImageView imgdialog;
    private int position;
    private ArrayList<Posts> posts;
    boolean check = false;
    private ViewPager2 viewpager2ShowImg;
    private List<Fragment> fragments;
    private ImgStatusViewpagerAdapter ImgStatusViewpagerAdapter;

    public DialogShowImageStatus(int position, ArrayList<Posts> posts) {
        this.position = position;
        this.posts = posts;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.show_img_status, null);
//        imgdialog = (ImageView) customView.findViewById(R.id.imgdialog);
        ViewTouch = (View) customView.findViewById(R.id.ViewTouch);
        viewpager2ShowImg = (ViewPager2) customView.findViewById(R.id.viewpager2ShowImg);
        usernamedialog = (TextView) customView.findViewById(R.id.usernamedialog);
        txtLikeDialog = (TextView) customView.findViewById(R.id.txtLikeDialog);
        menuShowImgDialog = (RelativeLayout) customView.findViewById(R.id.menuShowImgDialog);
        tinShowImgDialog = (LinearLayout) customView.findViewById(R.id.tinShowImgDialog);
        buttonShowImgDialog = (LinearLayout) customView.findViewById(R.id.buttonShowImgDialog);
        builder.setView(customView);
        tinShowImgDialog.setBackgroundResource(R.color.trans);
//        Picasso.get().load(BASE_URL+"uploads/"+posts.get(position).getFile().getImage()[0]).into(imgdialog);
        usernamedialog.setText(posts.get(position).getIduser().getUsername());
        txtLikeDialog.setText(""+posts.get(position).getLike().length);
        AlertDialog alert = builder.create();
        ViewTouch.setOnTouchListener(new OnSwipeTouchListener(getContext()){
            public void onSwipeTop() {
                alert.dismiss();
            }
            //                    public void onSwipeRight() {
//                        Toast.makeText(MyActivity.this, "right", Toast.LENGTH_SHORT).show();
//                    }
//                    public void onSwipeLeft() {
//                        Toast.makeText(MyActivity.this, "left", Toast.LENGTH_SHORT).show();
//                    }
            public void onSwipeBottom() {
                alert.dismiss();
            }
            public void onSingleTap(){
                if(!check){
                    menuShowImgDialog.animate().alpha(0.0f).setDuration(500);
                    tinShowImgDialog.animate().alpha(0.0f).setDuration(500);
                    buttonShowImgDialog.animate().alpha(0.0f).setDuration(500);
                    check = true;
                }else{
                    menuShowImgDialog.animate().alpha(1.0f).setDuration(500);
                    tinShowImgDialog.animate().alpha(1.0f).setDuration(500);
                    buttonShowImgDialog.animate().alpha(1.0f).setDuration(500);
                    check = false;
                }
            }
        });
        // viewpager2
        fragments = new ArrayList<>();
        for(int i = 0; i < posts.get(position).getFile().getImage().length; i++){
            fragments.add(new ShowImgStatusFragment(posts.get(position).getFile().getImage()[i]));
        }
        ImgStatusViewpagerAdapter = new ImgStatusViewpagerAdapter(getFragmentManager(), getLifecycle(), fragments);
        viewpager2ShowImg.setAdapter(ImgStatusViewpagerAdapter);
        return alert;
    }
}
