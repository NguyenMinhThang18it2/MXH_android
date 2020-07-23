package thang.com.uptimum.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import thang.com.uptimum.Dialog.adapterDialog.AdapterDialogShowImgPersonal;
import thang.com.uptimum.R;

public class DialogShowImgPersonal extends DialogFragment {
    private ArrayList<String> arrayList;
    private int position;
    private ViewPager2 viewpager2ShowImgPersonal;
    private Handler handler = new Handler();

    public DialogShowImgPersonal(ArrayList<String> arrayList, int position) {
        this.arrayList = arrayList;
        this.position = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.show_img_personal, null);

        viewpager2ShowImgPersonal = (ViewPager2) customView.findViewById(R.id.viewpager2ShowImgPersonal);
        viewpager2ShowImgPersonal.setAdapter(new AdapterDialogShowImgPersonal(arrayList,getContext()));

        viewpager2ShowImgPersonal.setClipToPadding(false);
        viewpager2ShowImgPersonal.setClipChildren(false);
        viewpager2ShowImgPersonal.setOffscreenPageLimit(3);
        viewpager2ShowImgPersonal.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });
        viewpager2ShowImgPersonal.setPageTransformer(compositePageTransformer);
        viewpager2ShowImgPersonal.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 3000);
            }
        });
        builder.setView(customView);
        AlertDialog alert = builder.create();
        return alert;
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            viewpager2ShowImgPersonal.setCurrentItem(viewpager2ShowImgPersonal.getCurrentItem()+1);
        }
    };
}
