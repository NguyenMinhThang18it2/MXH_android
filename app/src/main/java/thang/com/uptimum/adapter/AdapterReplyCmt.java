package thang.com.uptimum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import thang.com.uptimum.R;
import thang.com.uptimum.model.ReplyListCmt;

import static thang.com.uptimum.util.Constants.BASE_URL;

public class AdapterReplyCmt extends RecyclerView.Adapter<AdapterReplyCmt.ViewHolder> {
    private ArrayList<ReplyListCmt> arrayList;
    private Context context;

    public AdapterReplyCmt(ArrayList<ReplyListCmt> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterReplyCmt.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_reply_comment, parent,false);
        return new AdapterReplyCmt.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterReplyCmt.ViewHolder holder, int position) {
        Glide.with(context).load(BASE_URL+"uploads/"+arrayList.get(position).getIduser().getAvata()).centerCrop().fitCenter().into(holder.avataUserCmt);
        holder.txtNameUsercmt.setText(arrayList.get(position).getIduser().getUsername());
        holder.txtUserCmt.setText(arrayList.get(position).getDocument());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView avataUserCmt;
        private TextView txtNameUsercmt, txtUserCmt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avataUserCmt = (CircleImageView) itemView.findViewById(R.id.avataUserCmt);
            txtNameUsercmt = (TextView) itemView.findViewById(R.id.txtNameUsercmt);
            txtUserCmt = (TextView) itemView.findViewById(R.id.txtUserCmt);
        }
    }
}
