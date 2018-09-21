package com.example.andrew.postandcomment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrew.postandcomment.model.Upload;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Andrew on 5/4/2018.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> uploadList;

    public ImageAdapter(Context context, List<Upload> uploads){
        mContext = context;
        uploadList = uploads;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Upload uploadCurrent = uploadList.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        Picasso.with(mContext).load(uploadCurrent.getImageUrl()).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.txtViewName);
            imageView = itemView.findViewById(R.id.imgViewUpload);
        }
    }
}
