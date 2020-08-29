package thang.com.uptimum.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import thang.com.uptimum.R;
import thang.com.uptimum.model.ThemeStatus;

import static thang.com.uptimum.upload.UploadPostsActivity.textareaBackground;
import static thang.com.uptimum.upload.UploadPostsActivity.Theme;
import static thang.com.uptimum.util.Constants.BASE_URL;

public class themestatusAdapter extends RecyclerView.Adapter<themestatusAdapter.ViewHolder> {
    ArrayList<ThemeStatus> themestatuses;
    Context context;

    public themestatusAdapter(ArrayList<ThemeStatus> themestatuses, Context context) {
        this.themestatuses = themestatuses;
        this.context = context;
    }

    @NonNull
    @Override
    public themestatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_themestatus, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull themestatusAdapter.ViewHolder holder, int position) {
        Log.d("position", "" + position);
        Glide.with(context).asBitmap().load(BASE_URL+"uploads/"+themestatuses.get(position).getThemestatus())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        holder.imgThemeStatus.setBackground(new BitmapDrawable(resource));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        holder.imgThemeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Màu theme", ""+themestatuses.get(position).getThemestatus());
                Theme = themestatuses.get(position).getThemestatus();
                if(textareaBackground.getVisibility() == View.VISIBLE){
                    Picasso.get().load(BASE_URL+"uploads/"+themestatuses.get(position).getThemestatus()).memoryPolicy(MemoryPolicy.NO_CACHE).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            textareaBackground.setBackground(new BitmapDrawable(bitmap));
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
                }
                if(position == 2 ||position == 3 ||position == 4 ||position == 9 ||position == 10 ||position == 11){
                    textareaBackground.setTextColor(Color.parseColor("#ffffff"));
                    textareaBackground.setHintTextColor(Color.parseColor("#ffffff"));
                }else{
                    textareaBackground.setTextColor(Color.parseColor("#505050"));
                    textareaBackground.setHintTextColor(Color.parseColor("#505050"));
                }
            }
        });
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.cleanup();
    }

    @Override
    public int getItemCount() {
        return themestatuses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View imgThemeStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThemeStatus = (View) itemView.findViewById(R.id.imgThemeStatus);
        }
        public void cleanup() {
            Picasso.get().cancelRequest(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    textareaBackground.setBackground(null);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
    }
}
