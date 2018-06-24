package com.psdutta.hmi.photogalaxy.event;

import com.psdutta.hmi.photogalaxy.data.ReceivedPacket;

import java.util.List;

public interface IOnEventReceived {
    void onThumbnailListReceived(List<ReceivedPacket> packetList);
    void onActualImageReceived(ReceivedPacket packet);
}
