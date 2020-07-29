package thang.com.uptimum.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import thang.com.uptimum.R;

import static thang.com.uptimum.util.Constants.BASE_URL;

public class AdapterEjmotionLike extends RecyclerView.Adapter<AdapterEjmotionLike.ViewHolder> {
    private ArrayList<Integer> arrGif;
    private Context context;
    private NestedScrollView nestedScrollView;
    private onClickEjmotionStatus ejmotionListener;

    public AdapterEjmotionLike(ArrayList<Integer> arrGif, Context context, onClickEjmotionStatus ejmotionListener
            , NestedScrollView nestedScrollView) {
        this.arrGif = arrGif;
        this.context = context;
        this.ejmotionListener = ejmotionListener;
        this.nestedScrollView = nestedScrollView;
    }

    @NonNull
    @Override
    public AdapterEjmotionLike.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_icon_status, parent,false);
        return new AdapterEjmotionLike.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEjmotionLike.ViewHolder holder, int position) {
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget (holder.ImgiconStatus);
        Glide.with(context)
                .load(arrGif.get(position)).into(imageViewTarget);
        holder.linearImgiconStatus.startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_ejmotion_status));
    }

    @Override
    public int getItemCount() {
        return arrGif.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, View.OnClickListener{
        private Rect rect;
        private ImageView ImgiconStatus;
        private RelativeLayout linearImgiconStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ImgiconStatus = (ImageView) itemView.findViewById(R.id.ImgiconStatus);
            linearImgiconStatus = (RelativeLayout) itemView.findViewById(R.id.linearImgiconStatus);
            Log.d("ádkasjld", " "+ ImgiconStatus.getGlobalVisibleRect(rect));

//            ImgiconStatus.setOnClickListener(this);
            linearImgiconStatus.setOnTouchListener(this);
        }
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        nestedScrollView.requestDisallowInterceptTouchEvent(true);
                        Log.d("abbcasd", "ACTION_DOWN " + event.getRawX()+ " "+event.getRawY());
                        ImgiconStatus.getLayoutParams().height = 190;
                        ImgiconStatus.getLayoutParams().width = 190;
                        ImgiconStatus.requestLayout();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        nestedScrollView.requestDisallowInterceptTouchEvent(true);
                        Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                        if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                            Log.d("abbcasd", "ACTION_MOVE " + event.getRawX()+ " "+event.getRawY());
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        nestedScrollView.requestDisallowInterceptTouchEvent(false);
                        ImgiconStatus.getLayoutParams().height = 135;
                        ImgiconStatus.getLayoutParams().width = 135;
                        ImgiconStatus.requestLayout();
                        Log.d("abbcasd", "ACTION_UP " + event.getRawX()+ " "+event.getRawY());
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        nestedScrollView.requestDisallowInterceptTouchEvent(false);
                        ImgiconStatus.getLayoutParams().height = 135;
                        ImgiconStatus.getLayoutParams().width = 135;
                        ImgiconStatus.requestLayout();
                        Log.d("abbcasd", "ACTION_CANCEL " + event.getRawX()+ " "+event.getRawY());
                        break;
                }
                return true;
            }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ImgiconStatus:
                    Log.d("ádkasjld"," " + getAdapterPosition());
                    ejmotionListener.onclick(getAdapterPosition());
                    break;
            }
        }
    }
    public interface onClickEjmotionStatus{
        void onclick(int position);
    }
}
