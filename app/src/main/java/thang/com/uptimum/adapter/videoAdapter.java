package thang.com.uptimum.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
//import com.zolad.zoominimageview.ZoomInImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;
import thang.com.uptimum.Date.Timeupload;
import thang.com.uptimum.R;
import thang.com.uptimum.model.Comment;
import thang.com.uptimum.model.Posts;

import static thang.com.uptimum.Socket.SocketIO.socket;
import static thang.com.uptimum.util.Constants.BASE_URL;

public class videoAdapter extends RecyclerView.Adapter<videoAdapter.ViewHolder>{
    private RecyclerView recyclerViewCmt;
    private BottomSheetBehavior bottomSheetBehavior;
    boolean flag = false;
    public SimpleExoPlayer player;
    ArrayList<Posts> postsvideo;
    Context context;
    private Timeupload date = new Timeupload();
    private commentAdapter.onClickListener mListener;
    public videoAdapter(ArrayList<Posts> postsvideo, Context context) {
        this.postsvideo = postsvideo;
        this.context = context;
    }

    public videoAdapter() {
    }

    @NonNull
    @Override
    public videoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_status, parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtTimeUpload.setText(date.time(postsvideo.get(position).getCreatedAt()));
        holder.imgStatus.setVisibility(View.GONE);
        holder.framevideo.setVisibility(View.VISIBLE);
        holder.txtName.setText((postsvideo.get(position).getIduser().getUsername()));
        holder.txtDocument.setText(postsvideo.get(position).getDocument());
        holder.txtLike.setText(""+postsvideo.get(position).getLike().length);
        holder.txtCmt.setText(postsvideo.get(position).getComment()+" Bình luận");
        //
        SharedPreferences sessionManagement = context.getApplicationContext().getSharedPreferences("userlogin",Context.MODE_PRIVATE);
        String iduser = sessionManagement.getString("id","");
        Log.d("ttahdsjahskjdas" , " "+iduser);
        for(int i = 0; i < postsvideo.get(position).getLike().length; i++){
            if(iduser.equals(postsvideo.get(position).getLike()[i].getIduserlike())){
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
                    like.put("idposts", postsvideo.get(position).getId());
                    like.put("iduser", iduser);
                    like.put("action", likeTag);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("Like posts to server", like);
                if(likeTag.equals("like")){
                    holder.imgbtnLike.setImageResource(R.drawable.ic_facebook_blue_like_24);
                    holder.imgbtnLike.setTag("dislike");
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
                            if(idpost.equals(postsvideo.get(position).getId())){
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
        holder.rcvShowMultiImg.setVisibility(View.GONE);
        String pathvideo = BASE_URL+"uploads/"+postsvideo.get(position).getFile().getVideo();
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
        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.setPlayWhenReady(false);
//                Intent intent = new Intent(context.getApplicationContext(), PersonalActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
            }
        });
//        holder.btnFullscreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(false){
//                    holder.btnFullscreen.setImageResource(R.drawable.ic_fullscreen_black_24dp);
//                    setRequestedOrientation();
//                    flag = false;
//                }else{
//                    holder.btnFullscreen.setImageResource(R.drawable.ic_fullscreen_exit_black_24dp);
//                    // đặt màng hình
//                    setRequestedOrientation();
//                    flag = true;
//                }
//            }
//        });
        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getRootView().getContext(),R.style.BottomSheetDialogTheme);
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View view = layoutInflater.inflate(R.layout.activity_comment, null);

                RecyclerView recyclerViewCmtdialog = (RecyclerView) view.findViewById(R.id.recyclerViewCmt);
                recyclerViewCmtdialog.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManagerCmt =  new LinearLayoutManager
                        (v.getRootView().getContext(), LinearLayoutManager.VERTICAL, true);
                recyclerViewCmtdialog.setLayoutManager(linearLayoutManagerCmt);
                recyclerViewCmt = recyclerViewCmtdialog;
                ImageView btnBackUpload = (ImageView) view.findViewById(R.id.btnBackUpload);
                ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.height = getScreenHeight() + 200;
                view.setLayoutParams(params);
                bottomSheetDialog.setContentView(view);
                bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                bottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                bottomSheetDialog.show();

                getdataComment(postsvideo.get(position).getId());
                btnBackUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

            }
        });
        OnClickListenerCmt();
    }

    @Override
    public int getItemCount() {
        return postsvideo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName, txtDocument, txtLike, txtCmt, txtTimeUpload;
        private ImageView imgbtnLike, volume_controlview,exo_play,exo_pause,btnFullscreen, imgStatus;
        private RelativeLayout btnLike, btnComment;
        private PlayerView videoView;
        private FrameLayout framevideo;
        private ProgressBar progressBar;
        private RecyclerView rcvShowMultiImg;
//        private ZoomInImageView ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTimeUpload = (TextView) itemView.findViewById(R.id.txtTimeUpload);
            rcvShowMultiImg = (RecyclerView) itemView.findViewById(R.id.rcvShowMultiImg);
            imgStatus = (ImageView) itemView.findViewById(R.id.imgStatus);
            txtName = (TextView) itemView.findViewById(R.id.txtusername);
            txtDocument = (TextView) itemView.findViewById(R.id.txtdocument);
            txtLike = (TextView) itemView.findViewById(R.id.txtLike);
            txtCmt = (TextView) itemView.findViewById(R.id.txtCmt);
            framevideo = (FrameLayout) itemView.findViewById(R.id.framevideo);
            btnLike = (RelativeLayout) itemView.findViewById(R.id.btnLike);
            imgbtnLike = (ImageView) itemView.findViewById(R.id.imgbtnLike);
            videoView = (PlayerView) itemView.findViewById(R.id.videostatus);
            volume_controlview = (ImageView) itemView.findViewById(R.id.volume_control);
            exo_play = (ImageView)    itemView.findViewById(R.id.exo_play);
            exo_pause = (ImageView) itemView.findViewById(R.id.exo_pause);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            btnFullscreen = (ImageView) itemView.findViewById(R.id.btn_fullscreen);
            btnComment = (RelativeLayout) itemView.findViewById(R.id.btnComment);
        }
    }
    private void pausePlayer(){
        player.setPlayWhenReady(false);
        player.getPlaybackState();
    }
    private void startPlayer(){
        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }
    private void getdataComment(String idPosts){
        Log.d("idposstsaaaaa", ""+idPosts);

        JSONObject id = new JSONObject();
        try {
            id.put("idposts", idPosts);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("json",""+id);
        socket.emit("show commentposts", id);
        socket.on("all commentposts", allDataComment);

    }
    private Emitter.Listener allDataComment = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ArrayList<Comment> commentArrayList = new ArrayList<>();
                    String data = args[0].toString();
                    try {
                        JSONArray jsonarray = new JSONArray(data);
                        if (jsonarray != null) {
                            for (int i=0;i<jsonarray.length();i++){
                                JSONObject jsonObject = (JSONObject) jsonarray.get(i);
                                Gson gson = new Gson();
                                Comment comment = gson.fromJson(String.valueOf(jsonObject), Comment.class);
                                commentArrayList.add(comment);
                            }
                        }
                        commentAdapter commentAdapter = new commentAdapter(commentArrayList,context.getApplicationContext(), null, mListener);
                        recyclerViewCmt.setAdapter(commentAdapter);
                        Log.d("dataaaaaaa"," "+ commentArrayList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    private void OnClickListenerCmt(){
        mListener = new commentAdapter.onClickListener() {
            @Override
            public void onCickReply(TextView btnReplyCmt, int position) {

            }

            @Override
            public void onClickItemCmt(TextView txtUserCmt, int position) {

            }

        };
    }
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
