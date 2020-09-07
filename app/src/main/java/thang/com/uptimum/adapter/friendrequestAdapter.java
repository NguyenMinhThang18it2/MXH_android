package thang.com.uptimum.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import thang.com.uptimum.Date.Timeupload;
import thang.com.uptimum.R;
import thang.com.uptimum.model.ListNotification;

import static thang.com.uptimum.util.Constants.BASE_URL;

public class friendrequestAdapter extends RecyclerView.Adapter<friendrequestAdapter.ViewHolder>{
    private ArrayList<ListNotification> listFriendReq;
    private Context context;
    private Timeupload date = new Timeupload();
    private onClickItemFriendRequest mListener;
    public friendrequestAdapter(ArrayList<ListNotification> listFriendReq, Context context, onClickItemFriendRequest mListener) {
        this.listFriendReq = listFriendReq;
        this.context = context;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public friendrequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_friendrequest, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull friendrequestAdapter.ViewHolder holder, int position) {
        holder.txtTimeFriendReq.setText(date.time(listFriendReq.get(position).getCreateAt()));
        Glide.with(context).load(BASE_URL+"uploads/"+listFriendReq.get(position).getIduserNotify().getAvata())
                .fitCenter().centerCrop().into(holder.avataFriendReq);
        String documentNotify = "<b>" + listFriendReq.get(position).getIduserNotify().getUsername() + "</b> "
                +"<font color=#505050> đã gửi cho bạn lời mời kết bạn </font><b></b> ";
        holder.txtFriendReq.setText(Html.fromHtml(documentNotify));
    }

    @Override
    public int getItemCount() {
        int number = 0;
        if(listFriendReq.size() > 3)
            number = 3;
        else number = listFriendReq.size();
        return number;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CircleImageView avataFriendReq;
        private TextView txtFriendReq, txtTimeFriendReq;
        private LinearLayout btnAcceptFriendReq, btnDeleteFriendReq;
        private ImageView imgMenuFriendReq;
        private RelativeLayout RelativeNotify;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avataFriendReq = (CircleImageView) itemView.findViewById(R.id.avataFriendReq);
            txtFriendReq = (TextView) itemView.findViewById(R.id.txtFriendReq);
            txtTimeFriendReq = (TextView) itemView.findViewById(R.id.txtTimeFriendReq);
            btnAcceptFriendReq = (LinearLayout) itemView.findViewById(R.id.btnAcceptFriendReq);
            btnDeleteFriendReq = (LinearLayout) itemView.findViewById(R.id.btnDeleteFriendReq);
            imgMenuFriendReq = (ImageView) itemView.findViewById(R.id.imgMenuFriendReq);
            RelativeNotify = (RelativeLayout) itemView.findViewById(R.id.RelativeNotify);

            RelativeNotify.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.RelativeNotify:
                    mListener.onClick(getAdapterPosition());
                    break;
            }
        }
    }
    public  interface onClickItemFriendRequest{
        void onClick(int position);
    }
}
