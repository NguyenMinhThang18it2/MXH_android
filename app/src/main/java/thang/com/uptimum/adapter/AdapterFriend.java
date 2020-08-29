package thang.com.uptimum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import thang.com.uptimum.R;
import thang.com.uptimum.model.Friend;
import thang.com.uptimum.model.StatusUserLogin;

import static thang.com.uptimum.util.Constants.BASE_URL;

public class AdapterFriend extends RecyclerView.Adapter<AdapterFriend.ViewHolder> {
    private ArrayList<Friend> arrayList;
    private ArrayList<StatusUserLogin> arrstatusUser;
    private Context context;

    public AdapterFriend(ArrayList<Friend> arrayList, ArrayList<StatusUserLogin> arrstatusUser, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        this.arrstatusUser = arrstatusUser;
    }

    @NonNull
    @Override
    public AdapterFriend.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_friend_mess, parent,false);
        return new AdapterFriend.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFriend.ViewHolder holder, int position) {
        for(int i = 0; i < arrstatusUser.size(); i++){
            if(arrstatusUser.get(i).getIduser().getId().equals(arrayList.get(position).getIdfriend().getId())){
                if(arrstatusUser.get(i).isStatus()){
                    holder.StatusFriend.setVisibility(View.VISIBLE);
                }else{
                    holder.StatusFriend.setVisibility(View.GONE);
                }
                arrstatusUser.remove(i);
                break;
            }
        }
        Glide.with(context).load(BASE_URL+"uploads/"+arrayList.get(position).getIdfriend().getAvata())
                .centerCrop().fitCenter().into(holder.avataFriend);
        holder.txtUserNameFriend.setText(arrayList.get(position).getIdfriend().getUsername());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView avataFriend, StatusFriend;
        private TextView txtUserNameFriend;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            StatusFriend = (CircleImageView) itemView.findViewById(R.id.StatusFriend);
            avataFriend = (CircleImageView) itemView.findViewById(R.id.avataFriends);
            txtUserNameFriend = (TextView) itemView.findViewById(R.id.txtUserNameFriend);
        }
    }
}
