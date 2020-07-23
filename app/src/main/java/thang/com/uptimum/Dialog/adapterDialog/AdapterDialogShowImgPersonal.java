package thang.com.uptimum.Dialog.adapterDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import thang.com.uptimum.R;

import static thang.com.uptimum.util.Constants.BASE_URL;

public class AdapterDialogShowImgPersonal extends RecyclerView.Adapter<AdapterDialogShowImgPersonal.Viewholder> {
    private ArrayList<String> arrayList;
    private Context context;

    public AdapterDialogShowImgPersonal(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterDialogShowImgPersonal.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_showdialog_img_pesonal, parent,false);
        return new AdapterDialogShowImgPersonal.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDialogShowImgPersonal.Viewholder holder, int position) {
        Picasso.get().load(BASE_URL+"uploads/"+arrayList.get(position)).into(holder.imgItemShowPersonal);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onViewRecycled(@NonNull Viewholder holder) {
        super.onViewRecycled(holder);
        holder.clear();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private RoundedImageView imgItemShowPersonal;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imgItemShowPersonal = (RoundedImageView) itemView.findViewById(R.id.imgItemShowPersonal);
        }
        private void clear(){
            Picasso.get().cancelRequest(imgItemShowPersonal);
            imgItemShowPersonal.setImageDrawable(null);
        }
    }
}
