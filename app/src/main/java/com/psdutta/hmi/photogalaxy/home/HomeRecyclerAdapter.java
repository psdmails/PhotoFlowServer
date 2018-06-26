package com.psdutta.hmi.photogalaxy.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.psdutta.hmi.photogalaxy.GalaxyMainActivity;
import com.psdutta.hmi.photogalaxy.R;
import com.psdutta.hmi.photogalaxy.data.Constants;
import com.psdutta.hmi.photogalaxy.data.ReceivedPacket;
import com.psdutta.hmi.photogalaxy.details.FullScreenImageActivity;
import com.psdutta.hmi.photogalaxy.details.SlideShowPagerAdapter;
import com.psdutta.hmi.photogalaxy.details.SlideshowActivity;

import java.util.List;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder> {

    private Activity context;
    private List<ReceivedPacket> mPackets;
    private LayoutInflater mLayoutInflater;

    HomeRecyclerAdapter(Context context, List<ReceivedPacket> mPackets) {
        this.context = (Activity) context;
        this.mPackets = mPackets;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HomeRecyclerAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_home, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeRecyclerAdapter.HomeViewHolder holder, int position) {
        holder.mPhotoView.setImageURI(Uri.parse("http://" + mPackets.get(position).getFilePath()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SlideshowActivity.class);
                intent.putExtra(Constants.EXTRA_SELECTED_IMAGE_POS, holder.getAdapterPosition());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPackets.size();
    }

    static class HomeViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPhotoView;

        HomeViewHolder(View itemView) {
            super(itemView);
            mPhotoView = itemView.findViewById(R.id.image_view);
        }

    }

}
