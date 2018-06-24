package com.psdutta.hmi.photogalaxy.queue;

import com.psdutta.hmi.photogalaxy.connection.ConnectionManager;
import com.psdutta.hmi.photogalaxy.event.EventManager;

public class PacketQueueManager{
    private static final Object LOCK_OBJECT = new Object();
    private static PacketQueueManager mPacketQueueManager;
    private ConnectionManager mConnectionManager;
    private PacketRequestQueueHandler mPacketRequestQueueHandler;

    private PacketQueueManager() {
        mPacketRequestQueueHandler = new PacketRequestQueueHandler(EventManager.getInstance());
    }

    public static PacketQueueManager getInstance() {
        if (mPacketQueueManager == null) {
            synchronized (LOCK_OBJECT) {
                if (mPacketQueueManager == null) {
                    mPacketQueueManager = new PacketQueueManager();
                }
            }
        }
        return mPacketQueueManager;
    }

    public void setConnectManager(ConnectionManager connectManager){
        mConnectionManager = connectManager;
    }

    public void addPacketIntoRequestQueue(byte[] packet){
        mPacketRequestQueueHandler.addRequestIntoQueue(packet);
    }

    public void sendPacket(byte[] packet){
        mConnectionManager.sendPacket(packet);
    }
}
