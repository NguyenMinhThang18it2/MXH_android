package thang.com.uptimum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import thang.com.uptimum.R;
import thang.com.uptimum.model.Friend;

import static thang.com.uptimum.util.Constants.BASE_URL;

public class AdapterFriend extends RecyclerView.Adapter<AdapterFriend.ViewHolder> {
    private ArrayList<Friend> arrayList;
    private Context context;

    public AdapterFriend(ArrayList<Friend> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
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
        Picasso.get().load(BASE_URL+"uploads/"+arrayList.get(position).getIdfriend().getAvata())
                .resize(400,200).into(holder.avataFriend);
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
