package thang.com.uptimum.Main;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import thang.com.uptimum.R;
import thang.com.uptimum.login.LoginActivity;
import thang.com.uptimum.login.SessionManagement;

import static thang.com.uptimum.Main.MainActivity.viewPager2;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    private View contactsview;
    private Button btnLogout;
    private SessionManagement sessionManagement;
    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contactsview = inflater.inflate(R.layout.fragment_menu, container, false);
        btnLogout = (Button) contactsview.findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManagement = new SessionManagement(getActivity().getApplicationContext());
                sessionManagement.removeSession();
                LogoutSuccess();
            }
        });

        return contactsview;
    }

    private void LogoutSuccess() {
        Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }
}