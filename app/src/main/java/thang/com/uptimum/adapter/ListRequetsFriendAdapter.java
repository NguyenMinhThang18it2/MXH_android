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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import thang.com.uptimum.Date.Timeupload;
import thang.com.uptimum.R;
import thang.com.uptimum.model.ListNotification;

import static thang.com.uptimum.util.Constants.BASE_URL;

public class ListRequetsFriendAdapter extends RecyclerView.Adapter<ListRequetsFriendAdapter.ViewHolder> {
    private ArrayList<ListNotification> listFriendReqArr;
    private Context context;
    private Timeupload date = new Timeupload();
    private onClickListFriend mListener;
    public ListRequetsFriendAdapter(ArrayList<ListNotification> listFriendReqArr, Context context, onClickListFriend mListener) {
        this.listFriendReqArr = listFriendReqArr;
        this.context = context;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ListRequetsFriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_friend_request_list, parent, false);
        return new ListRequetsFriendAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListRequetsFriendAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(BASE_URL+"uploads/"+listFriendReqArr.get(position).getIduserNotify().getAvata())
                .fitCenter().centerCrop().into(holder.avataFriendReq);
        holder.txtFriendReq.setText(listFriendReqArr.get(position).getIduserNotify().getUsername());
        holder.txtTimeFriendReq.setText(date.time(listFriendReqArr.get(position).getCreateAt()));

    }

    @Override
    public int getItemCount() {
        return listFriendReqArr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView avataFriendReq;
        private TextView txtFriendReq, txtTimeFriendReq, txtAccept;
        private LinearLayout btnAcceptFriendReq, btnDeleteFriendReq, linearButton, linearAccept;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avataFriendReq = (CircleImageView) itemView.findViewById(R.id.avataFriendReq);
            txtFriendReq = (TextView) itemView.findViewById(R.id.txtFriendReq);
            txtTimeFriendReq = (TextView) itemView.findViewById(R.id.txtTimeFriendReq);
            btnAcceptFriendReq = (LinearLayout) itemView.findViewById(R.id.btnAcceptFriendReq);
            btnDeleteFriendReq = (LinearLayout) itemView.findViewById(R.id.btnDeleteFriendReq);
            linearButton = (LinearLayout) itemView.findViewById(R.id.linearButton);
            linearAccept = (LinearLayout) itemView.findViewById(R.id.linearAccept);
            txtAccept = (TextView) itemView.findViewById(R.id.txtAccept);

            btnAcceptFriendReq.setOnClickListener(this);
            btnDeleteFriendReq.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnAcceptFriendReq:
                    mListener.onClickAccept(getAdapterPosition(), linearButton, linearAccept, txtTimeFriendReq, txtAccept);
                    break;
                case R.id.btnDeleteFriendReq:
                    mListener.onClickDelete(getAdapterPosition(), linearButton, linearAccept, txtTimeFriendReq, txtAccept);
                    break;
            }
        }
    }
    public interface onClickListFriend{
        void onClickAccept(int position, LinearLayout linearButton, LinearLayout linearAccept, TextView txtTimeFriendReq, TextView txtAccept);
        void onClickDelete(int position, LinearLayout linearButton, LinearLayout linearAccept, TextView txtTimeFriendReq, TextView txtAccept);
    }
}
