package com.psdutta.hmi.photogalaxy.details;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.psdutta.hmi.photogalaxy.data.Constants;
import com.psdutta.hmi.photogalaxy.data.ReceivedPacket;
import com.psdutta.hmi.photogalaxy.event.IOnEventReceived;

import java.util.List;

public class SlideShowPagerAdapter extends FragmentStatePagerAdapter
    implements IOnEventReceived {

    private Context mContext;
    private List<ReceivedPacket> mPacketList;

    public SlideShowPagerAdapter(Context context, FragmentManager fm, List<ReceivedPacket> list) {
        super(fm);
        mContext = context;
        this.mPacketList = list;

    }

    @Override
    public Fragment getItem(int position) {
        return FullscreenFragment.getTatti(position);
    }

    @Override
    public int getCount() {
        return mPacketList.size();
    }

    @Override
    public void onThumbnailListReceived(List<ReceivedPacket> packetList) {
        mPacketList.addAll(packetList);
        notifyDataSetChanged();
    }

    @Override
    public void onActualImageReceived(ReceivedPacket packet) {

    }
}
