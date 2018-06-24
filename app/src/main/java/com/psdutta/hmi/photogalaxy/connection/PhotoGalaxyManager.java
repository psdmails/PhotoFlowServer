package com.psdutta.hmi.photogalaxy.connection;

import com.psdutta.hmi.photogalaxy.data.DataContainer;
import com.psdutta.hmi.photogalaxy.queue.PacketQueueManager;

public class PhotoGalaxyManager{
    private static final Object LOCK_OBJECT = new Object();
    private ConnectionManager mConnectionManager;
    private PacketQueueManager mPacketQueueManager;
    private static PhotoGalaxyManager mPhotoGalaxyManager;

    private PhotoGalaxyManager() {
        mPacketQueueManager = PacketQueueManager.getInstance();
        mConnectionManager = new ConnectionManager(mPacketQueueManager);
        DataContainer.getInstance().registerClientConnectedListener(mConnectionManager);
        mPacketQueueManager.setConnectManager(mConnectionManager);
    }

    public static PhotoGalaxyManager getInstance() {
        if (mPhotoGalaxyManager == null) {
            synchronized (LOCK_OBJECT) {
                if (mPhotoGalaxyManager == null) {
                    mPhotoGalaxyManager = new PhotoGalaxyManager();
                }
            }
        }
        return mPhotoGalaxyManager;
    }

    public void sendPacket(byte[] packet){
        mPacketQueueManager.sendPacket(packet);
    }
}
