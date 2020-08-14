package thang.com.uptimum.Main.other.Stories;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.shts.android.storiesprogressview.StoriesProgressView;
import thang.com.uptimum.Date.Timeupload;
import thang.com.uptimum.R;
import thang.com.uptimum.adapter.AdapterEmotionLike;
import thang.com.uptimum.model.Story;

import static thang.com.uptimum.util.Constants.BASE_URL;

public class StoriesFragment extends Fragment implements StoriesProgressView.StoriesListener {
    private static String TAG = "StoriesFragment";
    private ViewpagerStoriesActivity viewpagerStoriesActivity;
    private int counterPause = 0;
    private LinearLayout linearLayoutStory, linearStoriesBottom;
    private RelativeLayout RelativeStoriesInfor;
    private View view, reverse, skip;
    private StoriesProgressView storiesProgressView;
    private ImageView imgStories, btnClose;
    private int counter = 0;
    private ArrayList<Story> arrayListStory;
    private Timeupload date;
    private TextView txtTimeStory, txtusername;
    private CircleImageView userAvata;
    private Palette palette;
    private ViewPager2 viewPager2story;
    private RecyclerView reycStoriesEjmotion;
    private ArrayList<Integer> arrEmotion;
    private AdapterEmotionLike adapterEmotionLike;
    private AdapterEmotionLike.onClickEjmotionStatus mListener;
    long pressTime = 0L;
    long limit = 500L;

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    RelativeStoriesInfor.animate().alpha(0.0f).setDuration(500);
                    linearStoriesBottom.animate().alpha(0.0f).setDuration(500);
                    Log.d(TAG,"ACTION_DOWN");
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    Log.d(TAG,"ACTION_UP");
                    RelativeStoriesInfor.animate().alpha(1.0f).setDuration(500);
                    linearStoriesBottom.animate().alpha(1.0f).setDuration(500);
                    return limit < now - pressTime;
                case MotionEvent.ACTION_CANCEL:
                    long now1 = System.currentTimeMillis();
                    storiesProgressView.resume();
                    Log.d(TAG,"ACTION_CANCEL");
                    RelativeStoriesInfor.animate().alpha(1.0f).setDuration(500);
                    linearStoriesBottom.animate().alpha(1.0f).setDuration(500);
                    return limit < now1 - pressTime;
            }
            return true;
        }
    };

    public StoriesFragment(ArrayList<Story> arrayListStory, ViewPager2 viewPager2story) {
        this.arrayListStory = arrayListStory;
        this.viewPager2story = viewPager2story;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        date = new Timeupload();
        view = inflater.inflate(R.layout.fragment_stories, container, false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mapingView();
        setDataStories();
        recyclerviewEjmotion();

        return view;
    }
    private void mapingView(){
        linearLayoutStory = (LinearLayout) view.findViewById(R.id.imgStoryhear);
        txtTimeStory = (TextView) view.findViewById(R.id.txtTimeStory);
        txtusername = (TextView) view.findViewById(R.id.txtusername);
        userAvata = (CircleImageView) view.findViewById(R.id.userAvata);
        linearStoriesBottom = (LinearLayout) view.findViewById(R.id.linearStoriesBottom);
        RelativeStoriesInfor = (RelativeLayout) view.findViewById(R.id.RelativeStoriesInfor);
        reycStoriesEjmotion = (RecyclerView) view.findViewById(R.id.reycStoriesEjmotion);
    }
    private void setDataStories(){
        txtTimeStory.setText(date.time(arrayListStory.get(0).getCreatedAt()));
        txtusername.setText(arrayListStory.get(0).getUsers().getUsername());
        Picasso.get().load(BASE_URL+"uploads/"+arrayListStory.get(0).getUsers().getAvata())
                .resize(100,100).into(userAvata);
        // <- start progress
        imgStories = (ImageView) view.findViewById(R.id.imageStories);
        Glide.with(getContext()).asBitmap().load(BASE_URL+"uploads/"+arrayListStory.get(0).getFile()[counter])
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imgStories.setImageBitmap(resource);
                        palette = Palette.from(resource).generate();
                        Palette.Swatch swatch = palette.getMutedSwatch();
                        Palette.Swatch swatch1 = palette.getLightMutedSwatch();
                        if(swatch != null && swatch1 != null){
                            int colors[] = {swatch1.getRgb(),swatch.getRgb()};
                            GradientDrawable gradientDrawable = new GradientDrawable(
                                    GradientDrawable.Orientation.TOP_BOTTOM, colors);
                            linearLayoutStory.setBackground(gradientDrawable);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        btnClose = (ImageView) view.findViewById(R.id.btnClose);
        reverse = (View) view.findViewById(R.id.reverse);
        skip = (View) view.findViewById(R.id.skip);
        reverse.setOnTouchListener(onTouchListener);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter == 0){
                    viewPager2story.setCurrentItem(getItem(-1),true);
                }
                storiesProgressView.reverse();
            }
        });
        skip.setOnTouchListener(onTouchListener);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
    private void recyclerviewEjmotion(){
        reycStoriesEjmotion.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false);
        reycStoriesEjmotion.setLayoutManager(layoutManager);
        arrEmotion = new ArrayList<>();
        arrEmotion.add(R.drawable.ic_like_png);
        arrEmotion.add(R.drawable.ic_love_png);
        arrEmotion.add(R.drawable.ic_care_png);
        arrEmotion.add(R.drawable.ic_haha_png);
        arrEmotion.add(R.drawable.ic_wow_png);
        arrEmotion.add(R.drawable.ic_sad_png);
        arrEmotion.add(R.drawable.ic_angry_png);
        Log.d(TAG, " a " + arrEmotion.size());

        adapterEmotionLike = new AdapterEmotionLike(arrEmotion, getContext(), mListener);
        reycStoriesEjmotion.setAdapter(adapterEmotionLike);
    }
    @Override
    public void onNext() {

        if(counter < arrayListStory.get(0).getFile().length){
            ++counter;
            Glide.with(getContext()).asBitmap().load(BASE_URL+"uploads/"+arrayListStory.get(0).getFile()[counter])
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imgStories.setImageBitmap(resource);
                            palette = Palette.from(resource).generate();
                            Palette.Swatch swatch = palette.getMutedSwatch();
                            Palette.Swatch swatch1 = palette.getLightMutedSwatch();
                            if(swatch != null && swatch1 != null){
                                int colors[] = {swatch1.getRgb(),swatch.getRgb()};
                                GradientDrawable gradientDrawable = new GradientDrawable(
                                        GradientDrawable.Orientation.TOP_BOTTOM, colors);
                                linearLayoutStory.setBackground(gradientDrawable);
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }
    }

    @Override
    public void onPrev() {
        if(counter>0){
            --counter;
            Glide.with(getContext()).asBitmap().load(BASE_URL+"uploads/"+arrayListStory.get(0).getFile()[counter])
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imgStories.setImageBitmap(resource);
                            palette = Palette.from(resource).generate();
                            Palette.Swatch swatch = palette.getMutedSwatch();
                            Palette.Swatch swatch1 = palette.getLightMutedSwatch();
                            if(swatch != null && swatch1 != null){
                                int colors[] = {swatch1.getRgb(),swatch.getRgb()};
                                GradientDrawable gradientDrawable = new GradientDrawable(
                                        GradientDrawable.Orientation.TOP_BOTTOM, colors);
                                linearLayoutStory.setBackground(gradientDrawable);
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }
    }

    @Override
    public void onComplete() {
        storiesProgressView.destroy();
        viewPager2story.setCurrentItem(getItem(1),true);
    }

    @Override
    public void onDestroy() {
        storiesProgressView.destroy();
        super.onDestroy();
    }
    @Override
    public void onStart() {
        playStorie();
        counter=0;
        storiesProgressView.startStories(counter);
        super.onStart();
    }
    @Override
    public void onResume() {
        playStorie();
        counter = counterPause;
        storiesProgressView.startStories(counter);
        super.onResume();
    }

    @Override
    public void onPause() {
        counterPause = counter;
        Log.d("ảnh hiện tại", " "+ counterPause);
        super.onPause();
    }

    private int getItem(int i) {
        int number =  viewPager2story.getCurrentItem()+i;
        if(number<0){
            return 0;
        }else{
            return number;
        }
    }
    private void playStorie(){
        storiesProgressView = (StoriesProgressView) view.findViewById(R.id.stories);
        storiesProgressView.setStoriesCount(arrayListStory.get(0).getFile().length); // <- set stories
        storiesProgressView.setStoryDuration(3000L); // <- set a story duration
        storiesProgressView.setStoriesListener(this); // <- set listener
    }
}
