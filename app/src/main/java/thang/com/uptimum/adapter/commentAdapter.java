package thang.com.uptimum.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.uptimum.Date.Timeupload;
import thang.com.uptimum.R;
import thang.com.uptimum.model.Comment;
import thang.com.uptimum.model.ReplyComment;
import thang.com.uptimum.model.ReplyListCmt;
import thang.com.uptimum.network.NetworkUtil;
import thang.com.uptimum.network.ReplyCommentRetrofit;

import static thang.com.uptimum.util.Constants.BASE_URL;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.ViewHolder> {
    private static final String TAG = "commentAdapter";
    private ArrayList<Comment> cmt;
    private ArrayList<ReplyListCmt> arrReplycmt;
    private NetworkUtil networkUtil = new NetworkUtil();
    private Retrofit retrofit = networkUtil.getRetrofit();
    private ReplyCommentRetrofit replyCommentRetrofit;
    private Context context;
    private Timeupload date = new Timeupload();
    private onClickListener mListener;
    private String token="";
    public commentAdapter(ArrayList<Comment> cmt, Context context, String token , onClickListener mListener) {
        this.cmt = cmt;
        this.context = context;
        this.mListener = mListener;
        this.token = token;
    }

    @NonNull
    @Override
    public commentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_comment, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull commentAdapter.ViewHolder holder, int position) {
        loadReplyCmt(cmt.get(position).getId(), holder);
        holder.txtTimeCmt.setText(date.time(cmt.get(position).getCreatedAt()));
        Glide.with(context).load(BASE_URL+"uploads/"+cmt.get(position).getIduser().getAvata())
                .into(holder.avataUserCmt);
//        holder.avataUserCmt.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
//        holder.layoutComment.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        holder.txtNameUsercmt.setText(cmt.get(position).getIduser().getUsername());
        holder.txtUserCmt.setText(""+cmt.get(position).getDocument());
        holder.txtNumberLikeCmt.setText(""+cmt.get(position).getNumberLike().length);
        if(cmt.get(position).getFile().getImageComment().length()>10){
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) holder.imgComment.getLayoutParams();
            layoutParams.width = 600;
            layoutParams.height = 350;
            holder.imgComment.setLayoutParams(layoutParams);
            Glide.with(context).load(BASE_URL+"uploads/"+cmt.get(position).getFile().getImageComment())
                    .into(holder.imgComment);
        }
//        holder.txtNumberLikeCmt.setText(cmt.get(position).getNumberLike().getTypeLike());
    }

    @Override
    public int getItemCount() {
        return cmt.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private CircleImageView avataUserCmt, avataUserCmtReply, imgIconLikeCmt2, imgIconLikeCmt3;
        private TextView txtNameUsercmt, txtUserCmt, txtTimeCmt, btnLike, btnReplyCmt, txtNumberLikeCmt, txtNameUsercmtReply, txtUserCmtReply, txtNumberReplyCmt;
        private RoundedImageView imgComment;
        private ConstraintLayout layoutComment;
        private RelativeLayout relativeLayout3;
        private LinearLayout linearReplyCmt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearReplyCmt = (LinearLayout) itemView.findViewById(R.id.linearReplyCmt);
            avataUserCmt = (CircleImageView) itemView.findViewById(R.id.avataUserCmt);
            avataUserCmtReply = (CircleImageView) itemView.findViewById(R.id.avataUserCmtReply);
//            imgIconLikeCmt2 = (CircleImageView) itemView.findViewById(R.id.imgIconLikeCmt2);
//            imgIconLikeCmt3 = (CircleImageView) itemView.findViewById(R.id.imgIconLikeCmt3);
            imgComment = (RoundedImageView) itemView.findViewById(R.id.roundedImageView);
            layoutComment = (ConstraintLayout) itemView.findViewById(R.id.layoutComment);
            txtNameUsercmt = (TextView) itemView.findViewById(R.id.txtNameUsercmt);
            txtUserCmt = (TextView) itemView.findViewById(R.id.txtUserCmt);
            txtTimeCmt = (TextView) itemView.findViewById(R.id.txtTimeCmt);
            btnLike = (TextView) itemView.findViewById(R.id.txtLikeCmt);
            btnReplyCmt = (TextView) itemView.findViewById(R.id.txtReplyCmt);
            txtNumberLikeCmt = (TextView) itemView.findViewById(R.id.txtNumberLikeCmt);
            txtUserCmtReply = (TextView) itemView.findViewById(R.id.txtUserCmtReply);
            txtNumberReplyCmt = (TextView) itemView.findViewById(R.id.txtNumberReplyCmt);
            txtNameUsercmtReply = (TextView) itemView.findViewById(R.id.txtNameUsercmtReply);
            relativeLayout3 = (RelativeLayout) itemView.findViewById(R.id.relativeLayout3);

            btnReplyCmt.setOnClickListener(this);
            relativeLayout3.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.txtReplyCmt:
                    mListener.onCickReply(btnReplyCmt, getAdapterPosition());
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()){
                case R.id.relativeLayout3:
                    mListener.onClickItemCmt(txtUserCmt, getAdapterPosition());
                    break;
            }
            return false;
        }
    }
    public interface onClickListener{
        void onCickReply(TextView btnReplyCmt,int position);
        void onClickItemCmt(TextView txtUserCmt, int position);
    }
    private void loadReplyCmt(String idCmt, commentAdapter.ViewHolder holder){
        arrReplycmt = new ArrayList<>();
        arrReplycmt.clear();
        replyCommentRetrofit = retrofit.create(ReplyCommentRetrofit.class);
        Call<ReplyComment> listCall = replyCommentRetrofit.getReplyCmt(token, idCmt);
        listCall.enqueue(new Callback<ReplyComment>() {
            @Override
            public void onResponse(Call<ReplyComment> call, Response<ReplyComment> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, " lỗi response");
                }else{
                    ReplyComment replyComments  = response.body();
                    arrReplycmt.clear();
                    for (ReplyListCmt replyListCmt: replyComments.getListCmt()){
                        arrReplycmt.add(replyListCmt);
                    }
                    if(arrReplycmt.size() > 0) {
                        holder.linearReplyCmt.setVisibility(View.VISIBLE);
                        holder.txtNumberReplyCmt.setText("Xem thêm " + arrReplycmt.size() + " trả lời bình luận ...");
                        Glide.with(context)
                                .load(BASE_URL + "uploads/" + arrReplycmt.get(arrReplycmt.size() - 1).getIduser().getAvata())
                                .into(holder.avataUserCmtReply);
                        holder.txtNameUsercmtReply.setText(arrReplycmt.get(arrReplycmt.size() - 1).getIduser().getUsername());
                        holder.txtUserCmtReply.setText(arrReplycmt.get(arrReplycmt.size() - 1).getDocument());
                    }
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<ReplyComment> call, Throwable t) {
                Log.d(TAG, " lỗi " + t.getMessage());
                call.cancel();
            }
        });
    }
}
