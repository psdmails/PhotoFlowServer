package com.psdutta.hmi.photogalaxy.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.psdutta.hmi.photogalaxy.R;
import com.psdutta.hmi.photogalaxy.data.ReceivedPacket;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends BaseAdapter {

    private final LayoutInflater inflater;

    private List<ReceivedPacket> mDataList = new ArrayList<>();

    public GridAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public ReceivedPacket getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        SimpleDraweeView imageView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_home, null);
            ViewHolder viewHolder = new ViewHolder(view);
            imageView = viewHolder.imageView;
            view.setTag(viewHolder);
        } else {
            view = convertView;
            imageView = ((ViewHolder) view.getTag()).imageView;
        }

        imageView.setImageURI(Uri.parse("http://" + mDataList.get(position).getFilePath()));
        return view;
    }


    private static class ViewHolder {
        private SimpleDraweeView imageView;

        ViewHolder(View view) {
            imageView = (SimpleDraweeView) view.findViewById(R.id.image_view);
        }
    }

    public void setData(List<ReceivedPacket> datalist) {
        mDataList.addAll(datalist);
    }

}