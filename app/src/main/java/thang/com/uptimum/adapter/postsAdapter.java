package thang.com.uptimum.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
//import com.zolad.zoominimageview.ZoomInImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

import io.socket.emitter.Emitter;
import thang.com.uptimum.Main.SwipeTouch.OnSwipeTouchListener;

import thang.com.uptimum.R;

import thang.com.uptimum.model.Posts;


import static thang.com.uptimum.Socket.SocketIO.socket;
import static thang.com.uptimum.util.Constants.BASE_URL;

public class postsAdapter extends RecyclerView.Adapter<postsAdapter.ViewHolder> {

    private SimpleExoPlayer  player;
    private ArrayList<Posts> posts;
    private Context context;
    boolean check = false;
            //
    private SharedPreferences sessionManagement ;
    private String iduser ;

    private RecyclerviewClickListener Listener;

    public postsAdapter(ArrayList<Posts> posts, Context context, RecyclerviewClickListener Listener) {
        this.posts = posts;
        this.context = context;
        this.Listener = Listener;
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

        holder.txtName.setText((posts.get(position).getIduser().getUsername()));
        holder.txtLike.setText(""+posts.get(position).getLike().length);
        holder.txtCmt.setText(posts.get(position).getComment()+" Bình luận");

        sessionManagement = context.getApplicationContext().getSharedPreferences("userlogin",Context.MODE_PRIVATE);
        iduser = sessionManagement.getString("id","");
        Log.d("ttahdsjahskjdas" , " "+iduser);
        for(int i = 0; i < posts.get(position).getLike().length; i++){
            if(iduser.equals(posts.get(position).getLike()[i].getIduserlike())){
                holder.imgbtnLike.setImageResource(R.drawable.ic_facebook_blue_like_24);
                holder.imgbtnLike.setTag("dislike");
            }
        }
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("Like posts to server", like);
                if(likeTag.equals("like")){
                    holder.imgbtnLike.setImageResource(R.drawable.ic_facebook_blue_like_24);
                    holder.imgbtnLike.setTag("dislike");
                    final MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.like);
                    mediaPlayer.start();
                }else if(likeTag.equals("dislike")){
                    holder.imgbtnLike.setImageResource(R.drawable.ic_facebook_like_24);
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
                            String userlike = jsonObject.getString("userlike");
                            if(idpost.equals(posts.get(position).getId())){
                                holder.txtLike.setText(numberlikeposts);
                            }
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
            Picasso.get().load(BASE_URL+"uploads/"+posts.get(position).getFile().getBackground()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    holder.textStatusBacground.setBackground(new BitmapDrawable(bitmap));
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    Log.d("onBitmapFailed", " "+ e.getMessage());
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
            String themesPosition = posts.get(position).getFile().getBackground();
            if(themesPosition.equals("theme-1591455100989-800497553.jpg")||themesPosition.equals("theme-1591455108514-450060551.jpg")||
                    themesPosition.equals("theme-1591455118097-266327722.jpg")||themesPosition.equals("theme-1591455433956-958748998.jpg")||
                        themesPosition.equals("theme-1591456439234-168554446.jpg")||themesPosition.equals("theme-1591456445824-173683564.jpg")){
                holder.textStatusBacground.setTextColor(Color.WHITE);
            }
        }
//        holder.imgStatus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
//                LayoutInflater layoutInflater = LayoutInflater.from(context);
//
//                LinearLayout tinShowImgDialog,buttonShowImgDialog;
//                RelativeLayout menuShowImgDialog;
//                TextView txtLikeDialog,usernamedialog;
//
//                View customView = layoutInflater.inflate(R.layout.show_img_status, null);
//                ImageView imgdialog = (ImageView) customView.findViewById(R.id.imgdialog);
//                usernamedialog = (TextView) customView.findViewById(R.id.usernamedialog);
//                txtLikeDialog = (TextView) customView.findViewById(R.id.txtLikeDialog);
//                menuShowImgDialog = (RelativeLayout) customView.findViewById(R.id.menuShowImgDialog);
//                tinShowImgDialog = (LinearLayout) customView.findViewById(R.id.tinShowImgDialog);
//                buttonShowImgDialog = (LinearLayout) customView.findViewById(R.id.buttonShowImgDialog);
//                builder.setView(customView);
//                tinShowImgDialog.setBackgroundResource(R.color.trans);
//                Picasso.get().load(BASE_URL+"uploads/"+posts.get(position).getFile().getImage()[0]).into(imgdialog);
//                usernamedialog.setText(posts.get(position).getIduser().getUsername());
//                txtLikeDialog.setText(""+posts.get(position).getLike().length);
//                AlertDialog alert = builder.create();
//                alert.show();
//                imgdialog.setOnTouchListener(new OnSwipeTouchListener(v.getRootView().getContext()){
//                    public void onSwipeTop() {
//                        alert.dismiss();
//                    }
//                    //                    public void onSwipeRight() {
////                        Toast.makeText(MyActivity.this, "right", Toast.LENGTH_SHORT).show();
////                    }
////                    public void onSwipeLeft() {
////                        Toast.makeText(MyActivity.this, "left", Toast.LENGTH_SHORT).show();
////                    }
//                    public void onSwipeBottom() {
//                        alert.dismiss();
//                    }
//                    public void onSingleTap(){
//                        if(!check){
//                            menuShowImgDialog.animate().alpha(0.0f).setDuration(500);
//                            tinShowImgDialog.animate().alpha(0.0f).setDuration(500);
//                            buttonShowImgDialog.animate().alpha(0.0f).setDuration(500);
//                            check = true;
//                        }else{
//                            menuShowImgDialog.animate().alpha(1.0f).setDuration(500);
//                            tinShowImgDialog.animate().alpha(1.0f).setDuration(500);
//                            buttonShowImgDialog.animate().alpha(1.0f).setDuration(500);
//                            check = false;
//                        }
//                    }
//                });
//            }
//        });

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

    public class ViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView txtName, txtDocument, txtLike, txtCmt, textStatusBacground;
//        private ZoomInImageView ;
        private ImageView imgbtnLike, volume_control,exo_play,exo_pause, imgStatus;
        private PlayerView videoView;
        private FrameLayout framevideo;
        private ProgressBar progressBar;
        private RelativeLayout btnComment, btnLike;
        private RecyclerView rcvShowMultiImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rcvShowMultiImg = (RecyclerView) itemView.findViewById(R.id.rcvShowMultiImg);
            btnComment = (RelativeLayout) itemView.findViewById(R.id.btnComment);
            txtName = (TextView) itemView.findViewById(R.id.txtusername);
            txtDocument = (TextView) itemView.findViewById(R.id.txtdocument);
            txtLike = (TextView) itemView.findViewById(R.id.txtLike);
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

            volume_control.setOnClickListener(this);
            btnComment.setOnClickListener(this);
            imgStatus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


            switch (v.getId()){
                case R.id.volume_control:
                    turnOnOffVolume(volume_control);
                    break;
                case R.id.imgStatus:
                    Listener.showImg(imgStatus ,getAdapterPosition(), 1);
                    break;
                case R.id.btnComment:
                    Listener.onClickComment(btnComment, getAdapterPosition(), 2);
                    break;
            }
        }
        public void cleanup() {
            Picasso.get().cancelRequest(imgStatus);
            Picasso.get().cancelRequest(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    textStatusBacground.setBackground(null);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
            imgStatus.setImageDrawable(null);
            videoView.setPlayer(null);
            player.release();
            player.addVideoListener(null);
        }
    }
    public interface RecyclerviewClickListener{
        void onClickComment(RelativeLayout btnComment, int position, int typeClick);
        void showImg(ImageView imgShow, int position, int typeClick);
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
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0
        );
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0
        );
        switch (ArrUrlImg.size()){
            case 1:// px = dp * (dpi/160) lượm trên mạng =))
                param1.height = 1000;
                param1.weight = 0;
                mainImg.setLayoutParams(param1);
                Picasso.get().load(BASE_URL + "uploads/" + ArrUrlImg.get(0))
                        .memoryPolicy(MemoryPolicy.NO_CACHE).into(mainImg);
                break;
            case 2:
                param1.height = 1000;
                param1.weight = 1;
                mainImg.setLayoutParams(param1);
                Picasso.get().load(BASE_URL + "uploads/" + ArrUrlImg.get(0))
                        .memoryPolicy(MemoryPolicy.NO_CACHE).into(mainImg);
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
                Picasso.get().load(BASE_URL + "uploads/" + ArrUrlImg.get(0))
                        .memoryPolicy(MemoryPolicy.NO_CACHE).into(mainImg);
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
}
