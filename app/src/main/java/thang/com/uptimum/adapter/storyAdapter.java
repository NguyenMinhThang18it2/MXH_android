package thang.com.uptimum.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import thang.com.uptimum.Main.other.ViewpagerStoriesActivity;
import thang.com.uptimum.R;
import thang.com.uptimum.model.Story;

import static thang.com.uptimum.util.Constants.BASE_URL;

public class storyAdapter extends RecyclerView.Adapter<storyAdapter.ViewHolder> {
    ArrayList<Story> story;
    Context context;

    public storyAdapter(ArrayList<Story> story, Context context) {
        this.story = story;
        this.context = context;
    }

    @NonNull
    @Override
    public storyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_story,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull storyAdapter.ViewHolder holder, int position) {
        Picasso.get().load(BASE_URL+"uploads/"+story.get(position).getFile()[0])
                .memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.imgStory);
        Picasso.get().load(BASE_URL+"uploads/"+story.get(position).getUsers().getAvata())
                .memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.imgUserStory);
        holder.txtUsername.setText(story.get(position).getUsers().getUsername());
        holder.imgStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewpagerStoriesActivity.class);
                intent.putExtra("numberClickStory", position+1);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return story.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.cleanup();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgUserStory;
        private RoundedImageView imgStory;
        private TextView txtUsername;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgStory = (RoundedImageView) itemView.findViewById(R.id.imgStory);
            imgUserStory = (ImageView) itemView.findViewById(R.id.imgUserStory);
            txtUsername = (TextView) itemView.findViewById(R.id.usernaemstory);

        }
        public void cleanup() {
            Picasso.get().cancelRequest(imgStory);
            Picasso.get().cancelRequest(imgUserStory);
            imgStory.setImageDrawable(null);
            imgUserStory.setBackground(null);
        }
    }
}
