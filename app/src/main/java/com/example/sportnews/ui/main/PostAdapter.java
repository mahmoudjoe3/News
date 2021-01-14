package com.example.sportnews.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sportnews.R;
import com.example.sportnews.pojo.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.VH> {
    List<Post> posts;
    Context context;

    public PostAdapter(Context context) {
        if (posts == null) posts = new ArrayList<>();
        this.context = context;
    }

    public void setPostList(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.post_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Post post = posts.get(position);

        holder.type.setText(post.getType());
        holder.time.setText(post.getPublishedAt().split("-")[1]);
        holder.date.setText(post.getPublishedAt().split("-")[0]);
        holder.postTitle.setText(post.getTitle());
        holder.postImage.setText(post.getSectionName());
        holder.postImage.setTextColor(getMagnitudeColor(position));
        //clicks
        holder.authorName.setText(post.getAuthor().getName());
        holder.authorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAdapterClickListener != null)
                    onAdapterClickListener.onAuthorNameClick(post.getAuthor().getUrl());
            }
        });
        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAdapterClickListener != null)
                    onAdapterClickListener.onFavClick(post);
                holder.fav.setImageResource(R.drawable.ic_fav_on);
            }
        });
        holder.link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAdapterClickListener != null)
                    onAdapterClickListener.onLinkClick(post.getUrl());
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAdapterClickListener != null)
                    onAdapterClickListener.onShareClick(post);
            }
        });


    }

    private int getMagnitudeColor(int position) {
        switch (position % 5) {
            case 1:
                return context.getResources().getColor(R.color.color1);
            case 2:
                return context.getResources().getColor(R.color.color2);
            case 3:
                return context.getResources().getColor(R.color.color3);
            case 4:
                return context.getResources().getColor(R.color.color4);
            case 0:
                return context.getResources().getColor(R.color.color5);
        }
        return R.color.color5;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class VH extends RecyclerView.ViewHolder {
        TextView type, postTitle, date, time, authorName;
        TextView postImage;
        ImageButton fav, link, share;

        public VH(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type);
            postTitle = itemView.findViewById(R.id.postTitle);
            date = itemView.findViewById(R.id.postDate);
            time = itemView.findViewById(R.id.postTime);
            fav = itemView.findViewById(R.id.fav);
            link = itemView.findViewById(R.id.link);
            share = itemView.findViewById(R.id.share);
            postImage = itemView.findViewById(R.id.item_image);
            authorName = itemView.findViewById(R.id.authorName);
        }
    }

    private OnAdapterClickListener onAdapterClickListener;

    public void setOnAdapterClickListener(OnAdapterClickListener onAdapterClickListener) {
        this.onAdapterClickListener = onAdapterClickListener;
    }

    public interface OnAdapterClickListener {
        void onFavClick(Post post);

        void onShareClick(Post post);

        void onLinkClick(String url);

        void onAuthorNameClick(String url);
    }
}
