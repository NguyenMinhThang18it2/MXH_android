package thang.com.uptimum.adapter;

import android.content.Context;
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

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import thang.com.uptimum.R;
import thang.com.uptimum.model.Comment;

import static thang.com.uptimum.util.Constants.BASE_URL;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.ViewHolder> {
    private ArrayList<Comment> cmt;
    private Context context;

    public commentAdapter(ArrayList<Comment> cmt, Context context) {
        this.cmt = cmt;
        this.context = context;
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
//        Picasso.get().load(BASE_URL+"uploads/"+cmt.get(position).getFile().getImage()).into(holder.avataUserCmt);
        holder.avataUserCmt.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        holder.layoutComment.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        holder.txtNameUsercmt.setText(cmt.get(position).getIduser().getUsername());
        holder.txtUserCmt.setText(""+cmt.get(position).getDocument());
        holder.txtNumberLikeCmt.setText(""+cmt.get(position).getNumberLike().length);
        if(cmt.get(position).getFile().getImageComment().length()>10){
            Picasso.get().load(BASE_URL+"uploads/"+cmt.get(position).getFile().getImageComment()).into(holder.imgComment);
        }
//        holder.txtNumberLikeCmt.setText(cmt.get(position).getNumberLike().getTypeLike());
    }

    @Override
    public int getItemCount() {
        return cmt.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView avataUserCmt, imgIconLikeCmt2, imgIconLikeCmt3;
        private TextView txtNameUsercmt, txtUserCmt, txtTimeCmt, btnLike, btnReplyCmt, txtNumberLikeCmt;
        private RoundedImageView imgComment;
        private ConstraintLayout layoutComment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avataUserCmt = (CircleImageView) itemView.findViewById(R.id.avataUserCmt);
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
        }
    }
}
