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
public class MenuFragment extends Fragment implements View.OnTouchListener {

    private int typeAction = 0;
    private int currentPosition = 0;
    private View contactsview;
    private Button btnLogout;
    private SessionManagement sessionManagement;
    private RelativeLayout rlvEmotionReaction, rltemotionLike, rltemotionLove, rltemotionThuongThuong, rltemotionHaHa
            ,rltemotionWow,rltemotionSad,rltemotionAngry, rtlBoardIn, rtlBoardOut, rltCoverEmotion;
    private ImageView emotionLike, emotionLove,emotionThuongThuong,emotionHaHa,emotionWow,emotionSad,emotionAngry;
    private View viewEmotionLike, viewEmotionLove,viewEmotionThuongThuong,viewEmotionHaHa,viewEmotionWow,viewEmotionSad,viewEmotionAngry;
    private RectF rectF, rectFLike, rectFLove, rectFThuong, rectFHaHa, rectFWow, rectFSad, rectFAngry;
    private ArrayList<ImageView> arr;
    private ArrayList<RectF> arrRectF;
    private ArrayList<Integer> arrGif;
    private ArrayList<View> arrView;
    private ArrayList<RelativeLayout> arrRel;

    private int boardInCurrent = 0;
    private int boardOutCurrent = 0;
    private float emotionEnd = 0;
    private float emotionBrgin = 0;
    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contactsview = inflater.inflate(R.layout.fragment_menu, container, false);
        rtlBoardIn = (RelativeLayout) contactsview.findViewById(R.id.rtlBoardIn);
        rtlBoardOut = (RelativeLayout) contactsview.findViewById(R.id.rtlBoardOut);
        btnLogout = (Button) contactsview.findViewById(R.id.btnLogout);
        rlvEmotionReaction = (RelativeLayout) contactsview.findViewById(R.id.rlvEmotionReaction);
        rltemotionLike = (RelativeLayout) contactsview.findViewById(R.id.rltemotionLike);
        rltemotionLove = (RelativeLayout) contactsview.findViewById(R.id.rltemotionLove);
        rltemotionThuongThuong = (RelativeLayout) contactsview.findViewById(R.id.rltemotionThuongThuong);
        rltemotionHaHa = (RelativeLayout) contactsview.findViewById(R.id.rltemotionHaHa);
        rltemotionWow = (RelativeLayout) contactsview.findViewById(R.id.rltemotionWow);
        rltemotionSad = (RelativeLayout) contactsview.findViewById(R.id.rltemotionSad);
        rltemotionAngry = (RelativeLayout) contactsview.findViewById(R.id.rltemotionAngry);

        emotionLike = (ImageView) contactsview.findViewById(R.id.emotionLike);
        emotionLove = (ImageView) contactsview.findViewById(R.id.emotionLove);
        emotionThuongThuong = (ImageView) contactsview.findViewById(R.id.emotionThuongThuong);
        emotionHaHa = (ImageView) contactsview.findViewById(R.id.emotionHaHa);
        emotionWow = (ImageView) contactsview.findViewById(R.id.emotionWow);
        emotionSad = (ImageView) contactsview.findViewById(R.id.emotionSad);
        emotionAngry = (ImageView) contactsview.findViewById(R.id.emotionAngry);
        rltCoverEmotion = (RelativeLayout) contactsview.findViewById(R.id.rltCoverEmotion);

        viewEmotionLike = (View) contactsview.findViewById(R.id.viewEmotionLike);
        viewEmotionLove = (View) contactsview.findViewById(R.id.viewEmotionLove);
        viewEmotionThuongThuong = (View) contactsview.findViewById(R.id.viewEmotionThuong);
        viewEmotionHaHa = (View) contactsview.findViewById(R.id.viewEmotionHaHa);
        viewEmotionWow = (View) contactsview.findViewById(R.id.viewEmotionWow);
        viewEmotionSad = (View) contactsview.findViewById(R.id.viewEmotionSad);
        viewEmotionAngry = (View) contactsview.findViewById(R.id.viewEmotionAngry);


        arr = new ArrayList<>();
        arrRectF = new ArrayList<>();
        arrView = new ArrayList<>();
        arrRel = new ArrayList<>();

        arr.add(emotionLike);
        arr.add(emotionLove);
        arr.add(emotionThuongThuong);
        arr.add(emotionHaHa);
        arr.add(emotionWow);
        arr.add(emotionSad);
        arr.add(emotionAngry);

        arrView.add(viewEmotionLike);
        arrView.add(viewEmotionLove);
        arrView.add(viewEmotionThuongThuong);
        arrView.add(viewEmotionHaHa);
        arrView.add(viewEmotionWow);
        arrView.add(viewEmotionSad);
        arrView.add(viewEmotionAngry);

        arrRel.add(rltemotionLike);
        arrRel.add(rltemotionLove);
        arrRel.add(rltemotionThuongThuong);
        arrRel.add(rltemotionHaHa);
        arrRel.add(rltemotionWow);
        arrRel.add(rltemotionSad);
        arrRel.add(rltemotionAngry);

        rlvEmotionReaction.setOnTouchListener(this);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManagement = new SessionManagement(getActivity().getApplicationContext());
                sessionManagement.removeSession();
                LogoutSuccess();
            }
        });

        Log.d("abbcasd", "55    "+ dpToPx(55));
        Log.d("abbcasd", "57    "+ dpToPx(57)); // 156
        loadEmotionGif();
        return contactsview;
    }

    private void LogoutSuccess() {
        Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }
    private void loadEmotionGif(){
        arrGif = new ArrayList<>();
        arrGif.add(R.drawable.ejmotionlike);
        arrGif.add(R.drawable.ejmotionlove);
        arrGif.add(R.drawable.ejmotionthuongthuong);
        arrGif.add(R.drawable.ejmotionhahah);
        arrGif.add(R.drawable.ejmotionwow);
        arrGif.add(R.drawable.ejmotionsad);
        arrGif.add(R.drawable.ejmotionphanno);
        for (int i = 0; i < arrGif.size(); i++){
            Glide.with(getContext())
                    .load(arrGif.get(i)).into(arr.get(i));
        }

    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                viewPager2.setUserInputEnabled(false);
                break;
            case MotionEvent.ACTION_MOVE:
                viewPager2.setUserInputEnabled(false);
                rectF = new RectF(v.getLeft(),v.getTop(),v.getRight(),v.getBottom());
                if(!rectF.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                    beforeAnimateNormalBack();
                }else{
                    chooseEmotion(v,event);
                }
                break;
            case MotionEvent.ACTION_UP:
                viewPager2.setUserInputEnabled(true);
                chooseAnimateNormalBack();
                Log.d("abbcasd", "ACTION_UP " + event.getRawX()+ " "+event.getRawY());
                break;
            case MotionEvent.ACTION_CANCEL:
                viewPager2.setUserInputEnabled(true);
                Log.d("abbcasd", "ACTION_CANCEL " + event.getRawX()+ " "+event.getRawY());
                break;
        }
        return true;
    }
    private void chooseEmotion(View v,MotionEvent  event){
        Log.d("abbcasd", "arr  " + arr.size());
        arrRectF.clear();
        rectFLike = new RectF(50+rltemotionLike.getLeft(),v.getTop(),50+rltemotionLike.getRight(),681+rltemotionLike.getBottom());
        rectFLove = new RectF(50+rltemotionLove.getLeft(),v.getTop(),50+rltemotionLove.getRight(),681+rltemotionLove.getBottom());
        rectFThuong = new RectF(50+rltemotionThuongThuong.getLeft(),v.getTop(),50+rltemotionThuongThuong.getRight(),681+rltemotionThuongThuong.getBottom());
        rectFHaHa = new RectF(50+rltemotionHaHa.getLeft(),v.getTop(),50+rltemotionHaHa.getRight(),681+rltemotionHaHa.getBottom());
        rectFWow = new RectF(50+rltemotionWow.getLeft(),v.getTop(),50+rltemotionWow.getRight(),681+rltemotionWow.getBottom());
        rectFSad = new RectF(50+rltemotionSad.getLeft(),v.getTop(),50+rltemotionSad.getRight(),681+rltemotionSad.getBottom());
        rectFAngry = new RectF(50+rltemotionAngry.getLeft(),v.getTop(),50+rltemotionAngry.getRight(),681+rltemotionAngry.getBottom());

        arrRectF.add(rectFLike);
        arrRectF.add(rectFLove);
        arrRectF.add(rectFThuong);
        arrRectF.add(rectFHaHa);
        arrRectF.add(rectFWow);
        arrRectF.add(rectFSad);
        arrRectF.add(rectFAngry);

        Log.d("abbcasd", "arrF  " + arrRectF.size());

        for (int j = 0; j < arrRectF.size(); j++){
            if(arrRectF.get(j).contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                selected(j);
                break;
            }
        }


    }
    private void selected(int position){
        Log.d("abbcasd", "kết quả chọn  " + arr.get(position));
        if (currentPosition == position) return;

        currentPosition = position;
        typeAction = 1;
        beforeAnimationChoosing();
    }
    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    private void beforeAnimationChoosing(){
        TransitionManager.beginDelayedTransition(rtlBoardOut, new TransitionSet()
                .addTransition(new ChangeBounds()).setDuration(400));

        rtlBoardIn.getLayoutParams().height = dpToPx(47);
        rtlBoardIn.requestLayout();
        rtlBoardOut.getLayoutParams().height = dpToPx(45);
        rtlBoardOut.requestLayout();
        for (int i = 0; i < arr.size(); i++ ){
            if (i == currentPosition) {
                TransitionManager.beginDelayedTransition(arrRel.get(i), new TransitionSet()
                        .addTransition(new ChangeBounds()).setDuration(400));
                arrRel.get(i).getLayoutParams().width = dpToPx(105);
                arrRel.get(i).requestLayout();
                arr.get(i).getLayoutParams().height = dpToPx(105);
                arr.get(i).getLayoutParams().width = dpToPx(105);
                arr.get(i).requestLayout();
            } else {
                TransitionManager.beginDelayedTransition(arrRel.get(i), new TransitionSet()
                        .addTransition(new ChangeBounds()).setDuration(400));
                arrRel.get(i).getLayoutParams().width = dpToPx(35);
                arrRel.get(i).requestLayout();
                arr.get(i).getLayoutParams().height = dpToPx(35);
                arr.get(i).getLayoutParams().width = dpToPx(35);
                arr.get(i).requestLayout();
                arrView.get(i).getLayoutParams().width = dpToPx(35);
                arrView.get(i).requestLayout();
            }
        }
    }
    private void  beforeAnimateNormalBack(){
        TransitionManager.beginDelayedTransition(rtlBoardIn, new TransitionSet()
                .addTransition(new ChangeBounds()).setDuration(400));
        rtlBoardIn.getLayoutParams().height = dpToPx(57);
        rtlBoardIn.requestLayout();
        rtlBoardOut.getLayoutParams().height = dpToPx(55);
        rtlBoardOut.requestLayout();
        for (int i = 0; i < arr.size(); i++ ){
            TransitionManager.beginDelayedTransition(arrRel.get(i), new TransitionSet()
                    .addTransition(new ChangeBounds()).setDuration(400));
            arrRel.get(i).getLayoutParams().width = dpToPx(45);
            arrRel.get(i).requestLayout();
            arr.get(i).getLayoutParams().height = dpToPx(45);
            arr.get(i).getLayoutParams().width = dpToPx(45);
            arr.get(i).requestLayout();
        }
    }
    private void chooseAnimateNormalBack(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Path path = new Path();
            path.arcTo(-220f, -180f, arrRel.get(currentPosition).getRight()-dpToPx(60), 1080f, -45f, -135f, false);
            path.setLastPoint(-130f, 80f);
            ObjectAnimator animator = ObjectAnimator.ofFloat(arrRel.get(currentPosition), View.X, View.Y, path);
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(arrRel.get(currentPosition), "scaleY", 1f, 0.1f);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(arrRel.get(currentPosition), "scaleX", 1f, 0.1f);
            animatorSet.playTogether(scaleX, scaleY, animator);
            animatorSet.setDuration(1000);
            animatorSet.start();
        } else {
            // Create animator without using curved path
        }
    }
}