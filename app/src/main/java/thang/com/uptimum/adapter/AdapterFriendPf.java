package thang.com.uptimum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import thang.com.uptimum.R;
import thang.com.uptimum.model.Friend;

import static thang.com.uptimum.util.Constants.BASE_URL;

public class AdapterFriendPf extends RecyclerView.Adapter<AdapterFriendPf.ViewHolder> {
    private ArrayList<Friend> arrayList;
    private Context context;
    private OnclickRecycelListener mListener;

    public AdapterFriendPf(ArrayList<Friend> arrayList, Context context, OnclickRecycelListener mListener) {
        this.arrayList = arrayList;
        this.context = context;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public AdapterFriendPf.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_friend_profile, parent,false);
        return new AdapterFriendPf.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFriendPf.ViewHolder holder, int position) {
        Glide.with(context).load(BASE_URL+"uploads/"+arrayList.get(position).getIdfriend().getAvata())
                .centerCrop().fitCenter().into(holder.rimgvAvataFriendPf);
        holder.txtNameFriendPf.setText(arrayList.get(position).getIdfriend().getUsername());
    }

    @Override
    public int getItemCount() {
        if(arrayList.size() <= 3) {
            return arrayList.size();
        }
        else if(arrayList.size() > 3 && arrayList.size() < 6){
            return 3;
        }else if(arrayList.size() >= 6){
            return 6;
        }else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private RoundedImageView rimgvAvataFriendPf;
        private TextView txtNameFriendPf;
        private LinearLayout linearFriendPf;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearFriendPf = (LinearLayout) itemView.findViewById(R.id.linearFriendPf);
            rimgvAvataFriendPf = (RoundedImageView) itemView.findViewById(R.id.rimgvAvataFriendPf);
            txtNameFriendPf = (TextView) itemView.findViewById(R.id.txtNameFriendPf);

            linearFriendPf.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.linearFriendPf:
                    mListener.onclick(getAdapterPosition());
                    break;
            }
        }
    }
    public interface OnclickRecycelListener{
        void onclick(int position);
    }
}
