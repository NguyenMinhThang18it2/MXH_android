package thang.com.uptimum.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import java.util.ArrayList;

import thang.com.uptimum.R;

public class AdapterEmotionLike extends RecyclerView.Adapter<AdapterEmotionLike.ViewHolder> {
    private ArrayList<Integer> arrEmotion;
    private Context context;
    private onClickEjmotionStatus ejmotionListener;

    public AdapterEmotionLike(ArrayList<Integer> arrEmotion, Context context, onClickEjmotionStatus ejmotionListener) {
        this.arrEmotion = arrEmotion;
        this.context = context;
        this.ejmotionListener = ejmotionListener;
    }

    @NonNull
    @Override
    public AdapterEmotionLike.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_icon_status, parent,false);
        return new AdapterEmotionLike.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEmotionLike.ViewHolder holder, int position) {
        Glide.with(context)
                .load(arrEmotion.get(position)).into(holder.ImgiconStatus);
    }

    @Override
    public int getItemCount() {
        return arrEmotion.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView ImgiconStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ImgiconStatus = (ImageView) itemView.findViewById(R.id.ImgiconStatus);
            ImgiconStatus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
    public interface onClickEjmotionStatus{
        void onclick(int position);
    }
}
