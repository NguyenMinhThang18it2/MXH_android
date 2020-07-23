package thang.com.uptimum.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import thang.com.uptimum.R;

public class AdapterSelectImage extends RecyclerView.Adapter<AdapterSelectImage.ViewHolder> {
    private ArrayList<Uri> uriArr;
    private Context context;

    public AdapterSelectImage(ArrayList<Uri> uriArr, Context context) {
        this.uriArr = uriArr;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterSelectImage.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_select_multiple_file, parent,false);
        return new AdapterSelectImage.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSelectImage.ViewHolder holder, int position) {
        Log.d("selecte", "a  "+ position + "  "+ uriArr.get(position));
        Picasso.get().load(uriArr.get(position)).into(holder.SelectFileMultiple);
        holder.btnCloseMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.SelectFileMultiple.setImageDrawable(null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return uriArr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView SelectFileMultiple, btnCloseMultiple;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            SelectFileMultiple = (ImageView) itemView.findViewById(R.id.SelectFileMultiple);
            btnCloseMultiple = (ImageView) itemView.findViewById(R.id.btnCloseMultiple);
        }
    }
}
