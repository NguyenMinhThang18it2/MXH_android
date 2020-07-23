package thang.com.uptimum.Main.other.Personal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import thang.com.uptimum.R;
import thang.com.uptimum.adapter.AdapterFriendPf;

import static thang.com.uptimum.util.Constants.BASE_URL;

public class AdapterShowImgPersonal extends RecyclerView.Adapter<AdapterShowImgPersonal.ViewHolder> {
    private ArrayList<String> arrayList;
    private Context context;
    private OnClickImgPersonalListener listener;

    public AdapterShowImgPersonal(ArrayList<String> arrayList, Context context, OnClickImgPersonalListener listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdapterShowImgPersonal.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_show_img_personal, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterShowImgPersonal.ViewHolder holder, int position) {
        Picasso.get().load(BASE_URL+"uploads/"+arrayList.get(position))
                .resize(200,200).into(holder.ImgPersonal);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView ImgPersonal;
        private LinearLayout linearShowImgPersonal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ImgPersonal = (ImageView) itemView.findViewById(R.id.ImgPersonal);
            linearShowImgPersonal = (LinearLayout) itemView.findViewById(R.id.linearShowImgPersonal);

            ImgPersonal.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ImgPersonal:
                    listener.onclik(ImgPersonal,getAdapterPosition());
                    break;
            }
        }
    }
    public interface OnClickImgPersonalListener{
        void onclik(ImageView imageView, int position);
    }
}
