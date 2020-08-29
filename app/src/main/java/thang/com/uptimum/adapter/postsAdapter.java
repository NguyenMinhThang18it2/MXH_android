package thang.com.uptimum.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.emitter.Emitter;
import thang.com.uptimum.Date.Timeupload;

import thang.com.uptimum.Main.other.MyBounceInterpolator;
import thang.com.uptimum.R;

import thang.com.uptimum.model.Posts;
import thang.com.uptimum.model.UserLike;


import static thang.com.uptimum.Main.MainActivity.viewPager2;
import static thang.com.uptimum.Socket.SocketIO.socket;
import static thang.com.uptimum.util.Constants.BASE_URL;

public class postsAdapter extends RecyclerView.Adapter<postsAdapter.ViewHolder> {
    private static final String TAG  = "postsAdapter";
    private SimpleExoPlayer  player;
    private ArrayList<Posts> posts;
    private Context context;
    boolean check = false;
    private int currentPosition = 0, typeAction = 0;
    private SharedPreferences sessionManagement ;
    private int emotion0 = 0, emotion1 = 0, emotion2 = 0, emotion3 = 0, emotion4 = 0, emotion5 = 0, emotion6 = 0;
    private int[] emotion;
    private String iduser , nameUserLogin;

    private RecyclerviewClickListener Listener;
    private Timeupload date = new Timeupload();
    private NestedScrollView nestedScrollView;

    private float x,y;

    public postsAdapter(ArrayList<Posts> posts, Context context, RecyclerviewClickListener Listener, NestedScrollView nestedScrollView) {
        this.posts = posts;
        this.context = context;
        this.Listener = Listener;
        this.nestedScrollView = nestedScrollView;
    }
    @NonNull
    @Override
    public postsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_status, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull postsAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(BASE_URL+"uploads/"+posts.get(position).getIduser().getAvata())
                .fitCenter().centerCrop().into(holder.Avatauser);
        holder.txtTimeUpload.setText(date.time(posts.get(position).getCreatedAt()));
        holder.txtName.setText((posts.get(position).getIduser().getUsername()));

        holder.txtCmt.setText(posts.get(position).getComment()+" Bình luận");
        // kiểm tra số like và số cmt
        checkLikeAndCmt(position, holder, posts, null);
        sessionManagement = context.getApplicationContext().getSharedPreferences("userlogin",Context.MODE_PRIVATE);
        iduser = sessionManagement.getString("id","");
        nameUserLogin = sessionManagement.getString("username", "");
        Log.d(TAG , "iduser "+iduser);
        // check user có like bài viết này hay không
        for(int i = 0; i < posts.get(position).getLike().length; i++){
            if(iduser.equals(posts.get(position).getLike()[i].getIduserlike())){
                typeEmotionName(holder,posts.get(position).getLike()[i].getTypeLike());
                holder.imgbtnLike.setTag("dislike");
                break;
            }
        }
        // hiển thị loại emotion nhiều nhất
        showNumberEmotion(holder, posts.get(position).getLike());
        // Like -------------------------------------------------------
        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String likeTag = String.valueOf(holder.imgbtnLike.getTag());
                JSONObject like = new JSONObject();
                try {
                    like.put("idposts", posts.get(position).getId());
                    like.put("iduser", iduser);
                    like.put("action", likeTag);
                    like.put("typelike", "0");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("Like posts to server", like);
                if(likeTag.equals("like")){
                    final Animation animation =  AnimationUtils.loadAnimation(context,R.anim.bounce);
                    MyBounceInterpolator myBounceInterpolator = new MyBounceInterpolator(100,10);
                    animation.setInterpolator(myBounceInterpolator);
                    holder.imgbtnLike.startAnimation(animation);
                    typeEmotionName(holder, 0);
                    holder.imgbtnLike.setTag("dislike");
                    final MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.like);
                    mediaPlayer.start();
                }else if(likeTag.equals("dislike")){
                    typeEmotionName(holder, 100);
                    holder.imgbtnLike.setTag("like");
                }
            }
        });
        socket.on("Like posts to client", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = (JSONObject) args[0];
                        try {
                            String idpost = jsonObject.getString("id");
                            String numberlikeposts = jsonObject.getString("numberlikeposts");
                            JSONObject userlike = (JSONObject) jsonObject.getJSONObject("userlike");
                            Log.d(TAG, " a "+ userlike);
                            // cập nhật số lượt like của bài viết
                            Gson gson = new Gson();
                            Posts postsmodel = gson.fromJson(String.valueOf(userlike), Posts.class);
                            if(posts.get(position).getId().equals(idpost)){
                                showNumberEmotion(holder, postsmodel.getLike());
                                checkLikeAndCmt(position, holder, null, postsmodel);
                            }
//                            if(idpost.equals(posts.get(position).getId())){
//                                holder.txtNumberLike.setText(numberlikeposts);
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        // end like ------------------------------------------------------------------
        if(posts.get(position).getFile().getImage().length > 0) {
            holder.txtDocument.setText(posts.get(position).getDocument());

            ArrayList<String> UrlImg = new ArrayList<>();
            for(int i = 0; i< posts.get(position).getFile().getImage().length; i++){
                UrlImg.add(posts.get(position).getFile().getImage()[i]);
            }
            girlLayoutImg(UrlImg, holder.imgStatus, holder.rcvShowMultiImg);

        }
        else if(posts.get(position).getFile().getVideo().length()>10){
            holder.rcvShowMultiImg.setVisibility(View.GONE);
            holder.txtDocument.setText(posts.get(position).getDocument());
            holder.imgStatus.setVisibility(View.GONE);
            holder.framevideo.setVisibility(View.VISIBLE);
            String pathvideo = BASE_URL+"uploads/"+posts.get(position).getFile().getVideo();
            Log.d("testquwe", " "+pathvideo);
            Uri uri = Uri.parse(pathvideo);
            LoadControl loadControl = new DefaultLoadControl();
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            player = ExoPlayerFactory.newSimpleInstance(context,trackSelector,loadControl);
            DefaultHttpDataSourceFactory factory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(uri,factory,extractorsFactory,null,null);
            holder.videoView.setPlayer(player);
            holder.videoView.setKeepScreenOn(true);
            player.prepare(mediaSource);
            player.setPlayWhenReady(false);
            player.addListener(new Player.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, int reason) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if(playbackState == Player.STATE_BUFFERING){
                        holder.progressBar.setVisibility(View.VISIBLE);
                    }else if(playbackState == Player.STATE_READY){
                        holder.progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                }

                @Override
                public void onPlaybackSuppressionReasonChanged(int playbackSuppressionReason) {

                }

                @Override
                public void onIsPlayingChanged(boolean isPlaying) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity(int reason) {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }

                @Override
                public void onSeekProcessed() {

                }
            });

        }
        else{
            holder.rcvShowMultiImg.setVisibility(View.GONE);
            holder.txtDocument.setVisibility(View.GONE);
            holder.imgStatus.setVisibility(View.GONE);
            holder.framevideo.setVisibility(View.GONE);
            holder.textStatusBacground.setVisibility(View.VISIBLE);
            holder.textStatusBacground.setText(posts.get(position).getDocument());
            Glide.with(context).asBitmap().load(BASE_URL+"uploads/"+posts.get(position).getFile().getBackground())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            holder.textStatusBacground.setBackground(new BitmapDrawable(resource));
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
            String themesPosition = posts.get(position).getFile().getBackground();
            if(themesPosition.equals("theme-1591455100989-800497553.jpg")||themesPosition.equals("theme-1591455108514-450060551.jpg")||
                    themesPosition.equals("theme-1591455118097-266327722.jpg")||themesPosition.equals("theme-1591455433956-958748998.jpg")||
                        themesPosition.equals("theme-1591456439234-168554446.jpg")||themesPosition.equals("theme-1591456445824-173683564.jpg")){
                holder.textStatusBacground.setTextColor(Color.WHITE);
            }
        }

        for (int i = 0; i < holder.arrGif.size(); i++){
            Glide.with(context)
                    .load(holder.arrGif.get(i)).into(holder.arr.get(i));
        }

        holder.rlvEmotionReaction.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                holder.rlvEmotionReaction.onInterceptTouchEvent(event);
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        viewPager2.setUserInputEnabled(false);
                        nestedScrollView.requestDisallowInterceptTouchEvent(true);
                        // chọn ejmotion khi chạm lần đầu
                        chooseEmotion(v,event,holder);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        viewPager2.setUserInputEnabled(false);
                        nestedScrollView.requestDisallowInterceptTouchEvent(true);
                        // chạm di chuyển
                        holder.rectF = new RectF(v.getLeft(),v.getTop(),v.getRight(),v.getBottom());
                        if(!holder.rectF.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                            beforeAnimateNormalBack(holder);
                        }else{
                            chooseEmotion(v,event,holder);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        viewPager2.setUserInputEnabled(true);
                        nestedScrollView.requestDisallowInterceptTouchEvent(false);
                        // sau khi chọn ejmotion
                        chooseAnimateNormalBack(holder, position);
                        Log.d("abbcasd", "ACTION_UP " + event.getRawX()+ " "+event.getRawY());
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        viewPager2.setUserInputEnabled(true);
                        nestedScrollView.requestDisallowInterceptTouchEvent(false);
                        beforeAnimateNormalBack(holder);
                        Log.d("abbcasd", "ACTION_CANCEL " + event.getRawX()+ " "+event.getRawY());
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.cleanup();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView txtName, txtDocument, txtEmotion, txtNumberLike, txtCmt, textStatusBacground, txtTimeUpload;
//        private ZoomInImageView ;
        private ImageView imgbtnLike, volume_control,exo_play,exo_pause, imgStatus, menu;
        private PlayerView videoView;
        private FrameLayout framevideo;
        private ProgressBar progressBar;
        private RelativeLayout btnComment, btnLike, ejmotionLike, rltlayoutHideButton, rltLikeAndCmt;
        private RecyclerView rcvShowMultiImg, rcvIconStatus;
        private CircleImageView Avatauser, iconLike0, iconLike1, iconLike2;
        private LinearLayout linearStatus, linearbgrRecycler;
        //Emoition
        private RelativeLayout rlvEmotionReaction, rltemotionLike, rltemotionLove, rltemotionThuongThuong, rltemotionHaHa
                ,rltemotionWow,rltemotionSad,rltemotionAngry, rtlBoardIn, rtlBoardOut, rltCoverEmotion, EmoitionStatus;
        private ImageView emotionLike, emotionLove,emotionThuongThuong,emotionHaHa,emotionWow,emotionSad,emotionAngry;
        private View viewEmotionLike, viewEmotionLove,viewEmotionThuongThuong,viewEmotionHaHa,viewEmotionWow,viewEmotionSad,viewEmotionAngry;
        private RectF rectF, rectFLike, rectFLove, rectFThuong, rectFHaHa, rectFWow, rectFSad, rectFAngry;
        private ArrayList<ImageView> arr;
        private ArrayList<RectF> arrRectF;
        private ArrayList<Integer> arrGif;
        private ArrayList<View> arrView;
        private ArrayList<RelativeLayout> arrRel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mapingView(itemView);
            mapingEmotion(itemView);
            addArrEmotion();
            loadEmotionGif();
            volume_control.setOnClickListener(this);
            btnComment.setOnClickListener(this);
            imgStatus.setOnClickListener(this);
            linearStatus.setOnClickListener(this);
            Avatauser.setOnClickListener(this);
            menu.setOnClickListener(this);

            btnLike.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(EmoitionStatus.getVisibility() == View.VISIBLE){
//                ejmotionLike.setAnimation(AnimationUtils.loadAnimation(context, R.anim.hide_linear_ejmotion_status));


                int duration = 100;

                for (int i = 0; i < arrRel.size(); i++){
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.hide_ejmotion_status);
                    animation.setDuration(duration);
                    arrRel.get(i).setAnimation(animation);
                    arrRel.get(i).setVisibility(View.GONE);
                    duration = duration + 100;
                }
                rtlBoardIn.setAnimation(AnimationUtils.loadAnimation(context, R.anim.hide_linear_ejmotion_status));
                rtlBoardIn.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EmoitionStatus.setVisibility(View.GONE);
                    }
                },500);
            }else {
                switch (v.getId()) {
                    case R.id.volume_control:
                        turnOnOffVolume(volume_control);
                        break;
                    case R.id.imgStatus:
                        Listener.showImg(imgStatus, getAdapterPosition(), 1);
                        break;
                    case R.id.btnComment:
                        Listener.onClickComment(btnComment, getAdapterPosition(), 2);
                        break;
                    case R.id.linearStatus:
                        Listener.showStatusDetail(posts.get(getAdapterPosition()).getId());
                        break;
                    case R.id.Avatauser:
                        Listener.personalUser(posts.get(getAdapterPosition()).getIduser().getId(), getAdapterPosition());
                        break;
                    case R.id.txtusername:
                        Listener.personalUser(posts.get(getAdapterPosition()).getIduser().getId(), getAdapterPosition());
                        break;
                    case R.id.menu:
                        Listener.onclickMenu(getAdapterPosition());
                        break;
                }
            }
        }
        public void cleanup() {
            Glide.with(context).clear(imgStatus);
            Glide.with(context).clear(Avatauser);
            Glide.with(context).clear(textStatusBacground);
            textStatusBacground.setBackground(null);
            imgStatus.setImageDrawable(null);
            Avatauser.setImageDrawable(null);

            videoView.setPlayer(null);
        }
        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()){
                case R.id.btnLike:
                    EmoitionStatus.setVisibility(View.VISIBLE);
                    int duration = 300;
                    rtlBoardIn.setVisibility(View.VISIBLE);
                    rtlBoardIn.setAnimation(AnimationUtils.loadAnimation(context, R.anim.show_ejmotion_status));
                    for (int i = 0; i < arrRel.size(); i++){
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.show_ejmotion_status);
                        animation.setDuration(duration);
                        arrRel.get(i).setAnimation(animation);
                        arrRel.get(i).setVisibility(View.VISIBLE);
                        duration = duration + 100;
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
        //Touch Emotion
        private void mapingView(View itemView){
            rltLikeAndCmt = (RelativeLayout) itemView.findViewById(R.id.rltLikeAndCmt);
            linearStatus = (LinearLayout) itemView.findViewById(R.id.linearStatus);
            Avatauser = (CircleImageView) itemView.findViewById(R.id.Avatauser);
            iconLike0 = (CircleImageView) itemView.findViewById(R.id.iconLike0);
            iconLike1 = (CircleImageView) itemView.findViewById(R.id.iconLike1);
            iconLike2 = (CircleImageView) itemView.findViewById(R.id.iconLike2);
            txtTimeUpload = (TextView) itemView.findViewById(R.id.txtTimeUpload);
            rcvShowMultiImg = (RecyclerView) itemView.findViewById(R.id.rcvShowMultiImg);
            btnComment = (RelativeLayout) itemView.findViewById(R.id.btnComment);
            txtName = (TextView) itemView.findViewById(R.id.txtusername);
            txtDocument = (TextView) itemView.findViewById(R.id.txtdocument);
            txtNumberLike = (TextView) itemView.findViewById(R.id.txtNumberLike);
            txtEmotion = (TextView) itemView.findViewById(R.id.txtLike);
            txtCmt = (TextView) itemView.findViewById(R.id.txtCmt);
            textStatusBacground = (TextView) itemView.findViewById(R.id.textStatusBacground);
            imgStatus = (ImageView) itemView.findViewById(R.id.imgStatus);
            framevideo = (FrameLayout) itemView.findViewById(R.id.framevideo);
            btnLike = (RelativeLayout) itemView.findViewById(R.id.btnLike);
            imgbtnLike = (ImageView) itemView.findViewById(R.id.imgbtnLike);
            videoView = (PlayerView) itemView.findViewById(R.id.videostatus);
            volume_control = (ImageView) itemView.findViewById(R.id.volume_control);
            exo_play = (ImageView)    itemView.findViewById(R.id.exo_play);
            exo_pause = (ImageView) itemView.findViewById(R.id.exo_pause);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            menu = (ImageView) itemView.findViewById(R.id.menu);
        }
        private void mapingEmotion(View itemView){
            EmoitionStatus = (RelativeLayout) itemView.findViewById(R.id.EmoitionStatus);
            rtlBoardIn = (RelativeLayout) itemView.findViewById(R.id.rtlBoardIn);
            rtlBoardOut = (RelativeLayout) itemView.findViewById(R.id.rtlBoardOut);
            rlvEmotionReaction = (RelativeLayout) itemView.findViewById(R.id.rlvEmotionReaction);
            rltemotionLike = (RelativeLayout) itemView.findViewById(R.id.rltemotionLike);
            rltemotionLove = (RelativeLayout) itemView.findViewById(R.id.rltemotionLove);
            rltemotionThuongThuong = (RelativeLayout) itemView.findViewById(R.id.rltemotionThuongThuong);
            rltemotionHaHa = (RelativeLayout) itemView.findViewById(R.id.rltemotionHaHa);
            rltemotionWow = (RelativeLayout) itemView.findViewById(R.id.rltemotionWow);
            rltemotionSad = (RelativeLayout) itemView.findViewById(R.id.rltemotionSad);
            rltemotionAngry = (RelativeLayout) itemView.findViewById(R.id.rltemotionAngry);
            //
            emotionLike = (ImageView) itemView.findViewById(R.id.emotionLike);
            emotionLove = (ImageView) itemView.findViewById(R.id.emotionLove);
            emotionThuongThuong = (ImageView) itemView.findViewById(R.id.emotionThuongThuong);
            emotionHaHa = (ImageView) itemView.findViewById(R.id.emotionHaHa);
            emotionWow = (ImageView) itemView.findViewById(R.id.emotionWow);
            emotionSad = (ImageView) itemView.findViewById(R.id.emotionSad);
            emotionAngry = (ImageView) itemView.findViewById(R.id.emotionAngry);
            rltCoverEmotion = (RelativeLayout) itemView.findViewById(R.id.rltCoverEmotion);
            //
            viewEmotionLike = (View) itemView.findViewById(R.id.viewEmotionLike);
            viewEmotionLove = (View) itemView.findViewById(R.id.viewEmotionLove);
            viewEmotionThuongThuong = (View) itemView.findViewById(R.id.viewEmotionThuong);
            viewEmotionHaHa = (View) itemView.findViewById(R.id.viewEmotionHaHa);
            viewEmotionWow = (View) itemView.findViewById(R.id.viewEmotionWow);
            viewEmotionSad = (View) itemView.findViewById(R.id.viewEmotionSad);
            viewEmotionAngry = (View) itemView.findViewById(R.id.viewEmotionAngry);
        }
        private void addArrEmotion(){
            arr = new ArrayList<>();
            arrRectF = new ArrayList<>();
            arrView = new ArrayList<>();
            arrRel = new ArrayList<>();
            arr.clear();
            arrRectF.clear();
            arrView.clear();
            arrRel.clear();
            // add
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
        }
        private void loadEmotionGif(){
            arrGif = new ArrayList<>();
            arrGif.clear();
            arrGif.add(R.drawable.ejmotionlike);
            arrGif.add(R.drawable.ejmotionlove);
            arrGif.add(R.drawable.ejmotionthuongthuong);
            arrGif.add(R.drawable.ejmotionhahah);
            arrGif.add(R.drawable.ejmotionwow);
            arrGif.add(R.drawable.ejmotionsad);
            arrGif.add(R.drawable.ejmotionphanno);
        }

    }
    private void chooseEmotion(View v,MotionEvent  event, postsAdapter.ViewHolder holder){
        holder.arrRectF.clear();
        holder.rectFLike = new RectF(50+holder.rltemotionLike.getLeft(),v.getTop(),50+holder.rltemotionLike.getRight(),681+holder.rltemotionLike.getBottom());
        holder.rectFLove = new RectF(50+holder.rltemotionLove.getLeft(),v.getTop(),50+holder.rltemotionLove.getRight(),681+holder.rltemotionLove.getBottom());
        holder.rectFThuong = new RectF(50+holder.rltemotionThuongThuong.getLeft(),v.getTop(),50+holder.rltemotionThuongThuong.getRight(),681+holder.rltemotionThuongThuong.getBottom());
        holder.rectFHaHa = new RectF(50+holder.rltemotionHaHa.getLeft(),v.getTop(),50+holder.rltemotionHaHa.getRight(),681+holder.rltemotionHaHa.getBottom());
        holder.rectFWow = new RectF(50+holder.rltemotionWow.getLeft(),v.getTop(),50+holder.rltemotionWow.getRight(),681+holder.rltemotionWow.getBottom());
        holder.rectFSad = new RectF(50+holder.rltemotionSad.getLeft(),v.getTop(),50+holder.rltemotionSad.getRight(),681+holder.rltemotionSad.getBottom());
        holder.rectFAngry = new RectF(50+holder.rltemotionAngry.getLeft(),v.getTop(),50+holder.rltemotionAngry.getRight(),681+holder.rltemotionAngry.getBottom());

        holder.arrRectF.add(holder.rectFLike);
        holder.arrRectF.add(holder.rectFLove);
        holder.arrRectF.add(holder.rectFThuong);
        holder.arrRectF.add(holder.rectFHaHa);
        holder.arrRectF.add(holder.rectFWow);
        holder.arrRectF.add(holder.rectFSad);
        holder.arrRectF.add(holder.rectFAngry);

        Log.d("abbcasd", "arrF  " + holder.arrRectF.size());

        for (int j = 0; j < holder.arrRectF.size(); j++){
            if(holder.arrRectF.get(j).contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                selected(holder,j);

                break;
            }
        }


    }
    private void selected(postsAdapter.ViewHolder holder, int position){
        Log.d("abbcasd", "kết quả chọn  " + holder.arr.get(position));
        if (currentPosition == position) return;

        currentPosition = position;
        typeAction = 1;
        beforeAnimationChoosing(holder);
    }
    // chuyển đỗi dp sang px
    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    private void beforeAnimationChoosing(postsAdapter.ViewHolder holder){
        TransitionManager.beginDelayedTransition(holder.rtlBoardOut, new TransitionSet()
                .addTransition(new ChangeBounds()).setDuration(400));

        holder.rtlBoardIn.getLayoutParams().height = dpToPx(47);
        holder.rtlBoardIn.requestLayout();
        holder.rtlBoardOut.getLayoutParams().height = dpToPx(45);
        holder.rtlBoardOut.requestLayout();
        for (int i = 0; i < holder.arr.size(); i++ ){
            if (i == currentPosition) {
                TransitionManager.beginDelayedTransition(holder.arrRel.get(i), new TransitionSet()
                        .addTransition(new ChangeBounds()).setDuration(400));
                holder.arrRel.get(i).getLayoutParams().width = dpToPx(105);
                holder.arrRel.get(i).requestLayout();
                holder.arr.get(i).getLayoutParams().height = dpToPx(105);
                holder.arr.get(i).getLayoutParams().width = dpToPx(105);
                holder.arr.get(i).requestLayout();
            } else {
                TransitionManager.beginDelayedTransition(holder.arrRel.get(i), new TransitionSet()
                        .addTransition(new ChangeBounds()).setDuration(400));
                holder.arrRel.get(i).getLayoutParams().width = dpToPx(35);
                holder.arrRel.get(i).requestLayout();
                holder.arr.get(i).getLayoutParams().height = dpToPx(35);
                holder.arr.get(i).getLayoutParams().width = dpToPx(35);
                holder.arr.get(i).requestLayout();
                holder.arrView.get(i).getLayoutParams().width = dpToPx(35);
                holder.arrView.get(i).requestLayout();
            }
        }
    }
    private void  beforeAnimateNormalBack(postsAdapter.ViewHolder holder){
        Log.d("typelike", " "+currentPosition);
        TransitionManager.beginDelayedTransition(holder.rtlBoardIn, new TransitionSet()
                .addTransition(new ChangeBounds()).setDuration(400));
        holder.rtlBoardIn.getLayoutParams().height = dpToPx(57);
        holder.rtlBoardIn.requestLayout();
        holder.rtlBoardOut.getLayoutParams().height = dpToPx(55);
        holder.rtlBoardOut.requestLayout();
        for (int i = 0; i < holder.arr.size(); i++ ){
            TransitionManager.beginDelayedTransition(holder.arrRel.get(i), new TransitionSet()
                    .addTransition(new ChangeBounds()).setDuration(400));
            holder.arrRel.get(i).getLayoutParams().width = dpToPx(45);
            holder.arrRel.get(i).requestLayout();
            holder.arr.get(i).getLayoutParams().height = dpToPx(45);
            holder.arr.get(i).getLayoutParams().width = dpToPx(45);
            holder.arr.get(i).requestLayout();
        }
    }
    private void chooseAnimateNormalBack(postsAdapter.ViewHolder holder, int position){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            x = holder.arrRel.get(currentPosition).getX();
            y = holder.arrRel.get(currentPosition).getY();
            Path path = new Path();
            path.arcTo(-120f, -180f, holder.arrRel.get(currentPosition).getRight()-dpToPx(60), 1080f, -45f, -135f, false);
            path.setLastPoint(-70f, 80f);
            ObjectAnimator animator = ObjectAnimator.ofFloat(holder.arrRel.get(currentPosition), View.X, View.Y, path);
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(holder.arrRel.get(currentPosition), "scaleY", 1f, 0.1f);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(holder.arrRel.get(currentPosition), "scaleX", 1f, 0.1f);
            animatorSet.playTogether(scaleX, scaleY, animator);
            animatorSet.setDuration(800);
            animatorSet.start();
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    animation.removeListener(this);
                    animation.setDuration(0);
                    ((ObjectAnimator) animation).reverse();
                    // trở về size và vị trí ban đầu
                    holder.arrRel.get(currentPosition).setScaleX(1f);
                    holder.arrRel.get(currentPosition).setScaleY(1f);
                    path.lineTo(x,y);
                    ObjectAnimator animator = ObjectAnimator.ofFloat(holder.arrRel.get(currentPosition), View.X, View.Y, path);
                    animator.setDuration(0);
                    animator.start();
                    // tất cả trở về size ban đầu
                    beforeAnimateNormalBack(holder);
                }
            });
        } else {
            // Create animator without using curved path
        }
        int duration = 100;

        for (int i = 0; i < holder.arrRel.size(); i++){
            if(i != currentPosition) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.hide_ejmotion_status);
                animation.setDuration(duration);
                holder.arrRel.get(i).setAnimation(animation);
                holder.arrRel.get(i).setVisibility(View.INVISIBLE);
                duration = duration + 100;
            }
        }
        holder.rtlBoardIn.setAnimation(AnimationUtils.loadAnimation(context, R.anim.hide_linear_ejmotion_status));
        holder.rtlBoardIn.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                holder.EmoitionStatus.setVisibility(View.INVISIBLE);
            }
        },750);
        // loại emotion đã chọn
        Log.d(TAG, "a " + currentPosition);
        JSONObject likeEmotion = new JSONObject();
        String likeTagNow = String.valueOf(holder.imgbtnLike.getTag());
        String actionlike = "";
        if(likeTagNow.equals("like")){
            actionlike = "like";
        }else if(likeTagNow.equals("dislike")){
            actionlike = "updatelike";
        }
        try {
            likeEmotion.put("idposts", posts.get(position).getId());
            likeEmotion.put("iduser", iduser);
            likeEmotion.put("action", actionlike); // sữa tiếp chỗ này tao cái nha ----------------------------
            likeEmotion.put("typelike", currentPosition);// currentPosition loại 0
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("Like posts to server", likeEmotion);
        typeEmotionName(holder, currentPosition);
        holder.imgbtnLike.setTag("dislike");
        final MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.like);
        mediaPlayer.start();
    }
    // trả về kiểu emotion
    private int typeEmotionName(int typeEmotion){
        int emotion = 0;
        switch (typeEmotion){
            case 0:
                emotion = R.drawable.ic_like_png;
                break;
            case 1:
                emotion = R.drawable.ic_love_png;
                break;
            case 2:
                emotion = R.drawable.ic_care_png;
                break;
            case 3:
                emotion = R.drawable.ic_haha_png;
                break;
            case 4:
                emotion = R.drawable.ic_wow_png;
                break;
            case 5:
                emotion = R.drawable.ic_sad_png;
                break;
            case 6:
                emotion = R.drawable.ic_angry_png;
                break;
        }
        return emotion;
    }
    // trả về name của loại emotion
    private void typeEmotionName(postsAdapter.ViewHolder holder,int typeEmotion){
        String nameemotion = "";
        int emotion = 0;
        int color ;
        switch (typeEmotion){
            case 0:
                nameemotion = "Thích";
                emotion = R.drawable.icon_like_facebook;
                color = R.color.emotionLike;
                break;
            case 1:
                nameemotion = "Yêu thích";
                emotion = R.drawable.ic_love_png;
                color = R.color.emotionLove;
                break;
            case 2:
                nameemotion = "Thương thương";
                emotion = R.drawable.ic_care_png;
                color = R.color.emotionCare;
                break;
            case 3:
                nameemotion = "Haha";
                emotion = R.drawable.ic_haha_png;
                color = R.color.emotionCare;
                break;
            case 4:
                nameemotion = "Wow";
                emotion = R.drawable.ic_wow_png;
                color = R.color.emotionCare;
                break;
            case 5:
                nameemotion = "Buồn";
                emotion = R.drawable.ic_sad_png;
                color = R.color.emotionCare;
                break;
            case 6:
                nameemotion = "Phẩn nộ";
                emotion = R.drawable.ic_angry_png;
                color = R.color.emotionAngry;
                break;
            default:
                nameemotion = "Thích";
                emotion = R.drawable.icon_dislike;
                color = R.color.colorText;
        }
        holder.imgbtnLike.setImageResource(emotion);
        holder.txtEmotion.setText(nameemotion);
        holder.txtEmotion.setTextColor(ContextCompat.getColor(context, color));
    }
    // hàm này để hiển thị số lượt like của một bài viết
    private void showNumberEmotion(postsAdapter.ViewHolder holder,  UserLike[] arrUserLike){
        String txtNumberLike = "";
        for(int i = 0; i < arrUserLike.length; i++){
            if(arrUserLike[i].getIduserlike().equals(iduser)){
                if(arrUserLike.length == 1){
                    txtNumberLike = nameUserLogin;
                }else{
                    txtNumberLike = "Bạn và " + (arrUserLike.length - 1)  + " người khác";
                }
                break;
            }else{
                txtNumberLike = ""+arrUserLike.length;
            }
        }
        holder.txtNumberLike.setText(txtNumberLike);
        // số lượng loại emotion của một bài viết
        emotion0 = 0; emotion1 = 0; emotion2 = 0; emotion3 = 0; emotion4 = 0; emotion5 = 0; emotion6 = 0;
        emotion = new int[7];
        if(arrUserLike.length > 0) {
            for (int i = 0; i < arrUserLike.length; i++) {
                switch (arrUserLike[i].getTypeLike()){
                    case 0:
                        emotion0++;
                        break;
                    case 1:
                        emotion1++;
                        break;
                    case 2:
                        emotion2++;
                        break;
                    case 3:
                        emotion3++;
                        break;
                    case 4:
                        emotion4++;
                        break;
                    case 5:
                        emotion5++;
                        break;
                    case 6:
                        emotion6++;
                        break;
                }
            }
        }
        emotion[0] = emotion0;
        emotion[1] = emotion1;
        emotion[2] = emotion2;
        emotion[3] = emotion3;
        emotion[4] = emotion4;
        emotion[5] = emotion5;
        emotion[6] = emotion6;
        Log.d(TAG, "arr  "+ Arrays.toString(emotion));
        // tìm 3 loại emotion nhiều nhất
        int indexfirs = 0, indexsecond = 0, indexthird = 0,  firstNumber = 0, secondNumber = 0, thirdNumber = 0;
        for (int i = 0; i < 7; i++) {
            if(emotion[i] > firstNumber){
                firstNumber = emotion[i];
                indexfirs = i;
            }
        }
        emotion[indexfirs] = 0;
        for (int i = 0; i < 7; i++) {
            if(emotion[i] > secondNumber){
                secondNumber = emotion[i];
                indexsecond = i;
            }
        }
        emotion[indexsecond] = 0;
        for (int i = 0; i < 7; i++) {
            if(emotion[i] > thirdNumber){
                thirdNumber = emotion[i];
                indexthird = i;
            }
        }
        if(firstNumber != 0){
            holder.iconLike0.setVisibility(View.VISIBLE);
            holder.iconLike0.setImageResource(typeEmotionName(indexfirs));
        }
        if(secondNumber != 0){
            holder.iconLike1.setVisibility(View.VISIBLE);
            holder.iconLike1.setImageResource(typeEmotionName(indexsecond));
        }else {
            holder.iconLike1.setVisibility(View.GONE);
        }
        if(thirdNumber != 0){
            holder.iconLike2.setVisibility(View.VISIBLE);
            holder.iconLike2.setImageResource(typeEmotionName(indexthird));
        }else {
            holder.iconLike2.setVisibility(View.GONE);
        }
    }

    public interface RecyclerviewClickListener{
        void onClickComment(RelativeLayout btnComment, int position, int typeClick);
        void onclickMenu(int position);
        void showImg(ImageView imgShow, int position, int typeClick);
        void showStatusDetail(String idposts);
        void personalUser(String iduser, int position);
    }
    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(context, "exoplayer-codelab");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }
    private void pausePlayer (SimpleExoPlayer player) {
        player.setPlayWhenReady(false);
        player.getPlaybackState();
    }
    private void startPlayer(SimpleExoPlayer player) {
        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }
    private void turnOnOffVolume(ImageView volume_control){
        int currentvolume = (int) player.getVolume();
        Toast.makeText(context, " "+currentvolume, Toast.LENGTH_LONG).show();
        if(currentvolume == 1){
            player.setVolume(0f);
            volume_control.setImageResource(R.drawable.ic_volume_off_black_24dp);
        }else{
            player.setVolume(1);
            volume_control.setImageResource(R.drawable.ic_volume_up_black_24dp);
        }
    }
    private void girlLayoutImg(ArrayList<String> ArrUrlImg, ImageView mainImg, RecyclerView rcvImg){
        rcvImg.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL,false
        );
        rcvImg.setLayoutManager(layoutManager);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                0
        );
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0
        );
        switch (ArrUrlImg.size()){
            case 1:// px = dp * (dp/160) lượm trên mạng =))
                param1.height = 1000;
                param1.weight = 0;
                mainImg.setLayoutParams(param1);
                Glide.with(context).load(BASE_URL + "uploads/" + ArrUrlImg.get(0))
                        .fitCenter().centerCrop().into(mainImg);
                break;
            case 2:
                param1.height = 1000;
                param1.weight = 1;
                mainImg.setLayoutParams(param1);
                Glide.with(context).load(BASE_URL + "uploads/" + ArrUrlImg.get(0))
                        .fitCenter().centerCrop().into(mainImg);
                ArrUrlImg.remove(0);
                Log.d("via"," "+ArrUrlImg);
                rcvImg.setLayoutParams(param1);
                AdapterShowMultipleImg adapter = new AdapterShowMultipleImg(ArrUrlImg,context);
                rcvImg.setAdapter(adapter);
                break;
            default:
                param1.height = 1000;
                param1.weight = 1;
                mainImg.setLayoutParams(param1);
                Glide.with(context).load(BASE_URL + "uploads/" + ArrUrlImg.get(0))
                        .fitCenter().centerCrop().into(mainImg);
                ArrUrlImg.remove(0);
                param2.height = 1000;
                param2.weight = 2;
                Log.d("via"," "+ArrUrlImg);
                rcvImg.setLayoutParams(param2);
                AdapterShowMultipleImg adapter1 = new AdapterShowMultipleImg(ArrUrlImg,context);
                rcvImg.setAdapter(adapter1);
                break;
        }
    }
    private void checkLikeAndCmt(int position, postsAdapter.ViewHolder holder, ArrayList<Posts> arrposts, Posts mposts){
        int like=0, cmt =0;
        if(arrposts != null){
            like  = arrposts.get(position).getLike().length;
            cmt = arrposts.get(position).getComment();
        }else{
            like  = mposts.getLike().length;
            cmt = mposts.getComment();
        }
        if(like == 0 && cmt == 0) holder.rltLikeAndCmt.setVisibility(View.GONE);
        else holder.rltLikeAndCmt.setVisibility(View.VISIBLE);


    }
}
