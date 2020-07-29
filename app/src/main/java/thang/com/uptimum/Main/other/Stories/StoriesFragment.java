package thang.com.uptimum.Main.other.Stories;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.shts.android.storiesprogressview.StoriesProgressView;
import thang.com.uptimum.Date.Timeupload;
import thang.com.uptimum.R;
import thang.com.uptimum.model.Story;

import static thang.com.uptimum.Main.other.Stories.ViewpagerStoriesActivity.viewPager2story;
import static thang.com.uptimum.util.Constants.BASE_URL;

public class StoriesFragment extends Fragment implements StoriesProgressView.StoriesListener {
    private ViewpagerStoriesActivity viewpagerStoriesActivity;
    private int counterPause = 0;
    private LinearLayout linearLayoutStory;
    private View view, reverse, skip;
    private StoriesProgressView storiesProgressView;
    private ImageView imgStories, btnClose;
    private int counter = 0;
    private ArrayList<Story> arrayListStory;
    private Timeupload date;
    private TextView txtTimeStory, txtusername;
    private CircleImageView userAvata;

    public StoriesFragment(ArrayList<Story> arrayListStory) {
        this.arrayListStory = arrayListStory;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        date = new Timeupload();
        view = inflater.inflate(R.layout.fragment_stories, container, false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        linearLayoutStory = (LinearLayout) view.findViewById(R.id.imgStoryhear);
        txtTimeStory = (TextView) view.findViewById(R.id.txtTimeStory);
        txtusername = (TextView) view.findViewById(R.id.txtusername);
        userAvata = (CircleImageView) view.findViewById(R.id.userAvata);

        txtTimeStory.setText(date.time(arrayListStory.get(0).getCreatedAt()));
        txtusername.setText(arrayListStory.get(0).getUsers().getUsername());
        Picasso.get().load(BASE_URL+"uploads/"+arrayListStory.get(0).getUsers().getAvata())
                .resize(100,100).into(userAvata);
         // <- start progress
        imgStories = (ImageView) view.findViewById(R.id.imageStories);
        Picasso.get().load(BASE_URL+"uploads/"+arrayListStory.get(0).getFile()[counter]).into(imgStories);
        btnClose = (ImageView) view.findViewById(R.id.btnClose);
        reverse = (View) view.findViewById(R.id.reverse);
        skip = (View) view.findViewById(R.id.skip);

        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(counter == 0){
                    viewPager2story.setCurrentItem(getItem(-1),true);
                }
                storiesProgressView.reverse();
            }
        });
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
        return view;
    }

    @Override
    public void onNext() {

        if(counter < arrayListStory.get(0).getFile().length){
            ++counter;
            Picasso.get().load(BASE_URL+"uploads/"+arrayListStory.get(0).getFile()[counter]).into(imgStories);
        }
    }

    @Override
    public void onPrev() {
        if(counter>0){
            --counter;
            Picasso.get().load(BASE_URL+"uploads/"+arrayListStory.get(0).getFile()[counter]).into(imgStories);
        }
    }

    @Override
    public void onComplete() {
        viewPager2story.setCurrentItem(getItem(1),true);
    }

    @Override
    public void onDestroy() {
//        storiesProgressView.destroy();
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
