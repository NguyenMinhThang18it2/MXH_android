package thang.com.uptimum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import thang.com.uptimum.R;
import thang.com.uptimum.model.Users;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.ViewHolder> {
    private ArrayList<Users> usersArr;
    private Context context;
    private onClickItemSearch mListener;
    public AdapterSearch(ArrayList<Users> usersArr, Context context,onClickItemSearch mListener) {
        this.usersArr = usersArr;
        this.context = context;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_search, parent,false);
        return new AdapterSearch.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtNameSearch.setText(usersArr.get(position).getUsername());
        holder.imgSearch.setImageResource(R.drawable.ic_baseline_search_24);
    }

    @Override
    public int getItemCount() {
        return usersArr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtNameSearch;
        private ImageView imgSearch;
        private LinearLayout linearSearchAdapter;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameSearch = (TextView) itemView.findViewById(R.id.txtNameSearch);
            imgSearch = (ImageView) itemView.findViewById(R.id.imgSearch);
            linearSearchAdapter = (LinearLayout) itemView.findViewById(R.id.linearSearchAdapter);

            linearSearchAdapter.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.linearSearchAdapter:
                    mListener.onClick(linearSearchAdapter, getAdapterPosition());
                    break;
            }
        }
    }
    public interface onClickItemSearch{
        void onClick(LinearLayout linearSearchAdapter, int position);
    }
}
