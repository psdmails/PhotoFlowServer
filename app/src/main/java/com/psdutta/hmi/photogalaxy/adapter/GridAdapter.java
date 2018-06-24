package com.psdutta.hmi.photogalaxy.adapter;

/**
 * Created by PsDutta on 007, 07/06/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.psdutta.hmi.photogalaxy.R;
import com.psdutta.hmi.photogalaxy.data.BitmapGenerator;
import com.psdutta.hmi.photogalaxy.data.ReceivedPacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GridAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private Context mContext;

    private List<ReceivedPacket> mDataList = new ArrayList<>();

    // Keep all Images in array
    /*public Integer[] mThumbIds = {
            R.drawable.pic_1, R.drawable.pic_2,
            R.drawable.pic_3, R.drawable.pic_4,
            R.drawable.pic_1, R.drawable.pic_2,
            R.drawable.pic_3, R.drawable.pic_4,
            R.drawable.pic_1, R.drawable.pic_2,
            R.drawable.pic_3, R.drawable.pic_4

    };
*/
    public GridAdapter(Context c){
        mContext = c;
        inflater = LayoutInflater.from(c);
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

//        if(inflater == null)
//        {
//            inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        }
//
//        pagerLayout = (RelativeLayout) inflater.inflate(resource, container, false);
        ImageView imageView = new ImageView(mContext);
        imageView.setImageBitmap(BitmapGenerator.stringToBitMap(mDataList.get(position)
                .getEncodedBitmap()));


        //imageView.setImageResource(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(160, 160));
        return imageView;
    }


    public void setData(List<ReceivedPacket> datalist) {
        mDataList.addAll(datalist);
    }


    /*public List<Integer> getAllItems() {
        return Arrays.asList(mThumbIds);
    }*/
}