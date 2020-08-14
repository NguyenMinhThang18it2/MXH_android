package thang.com.uptimum.Main.other.Stories;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class CubeTransformerViewpager implements ViewPager2.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        if(position <= 0) page.setPivotX(page.getWidth()); //
        else page.setPivotX(0.0f);

        page.setPivotY(page.getHeight()*0.5f);

        page.setRotationY(20f * position);
    }
}
