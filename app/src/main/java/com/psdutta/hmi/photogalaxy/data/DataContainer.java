package com.psdutta.hmi.photogalaxy.data;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;

import com.psdutta.hmi.photogalaxy.connection.IClientConnection;
import com.psdutta.hmi.photogalaxy.event.IOnEventReceived;

import java.util.ArrayList;
import java.util.List;

public class DataContainer   {
    private static final String TAG = DataContainer.class.getSimpleName();
    private static final Object LOCK_OBJECT = new Object();
    private static final int MSG_ALL_THUMBNAILS = 1000;
    private static final int MSG_SELECTED_IMAGE = 1001;
    private static final int MSG_SET_IP_ADDRESS = 1002;
    private static DataContainer mDataContainer;
    private List<IOnEventReceived> mIOnEventReceivedList = new ArrayList<>();
    private IClientConnection mIClientConnection;
    private final List<ReceivedPacket> mThumbnailList = new ArrayList<>();
    private final List<ReceivedPacket> mActualImageList = new ArrayList<>();
    private Handler mHandler;
    private String mIpAddress;

    private DataContainer() {


        createHandlerThread();
    }

    private void createHandlerThread() {
        HandlerThread thread = new HandlerThread(DataContainer.class.getSimpleName());
        thread.start();
        mHandler = new Handler(thread.getLooper(), new ResponseCallback());
    }

    public static DataContainer getInstance() {
        if (mDataContainer == null) {
            synchronized (LOCK_OBJECT) {
                if (mDataContainer == null) {
                    mDataContainer = new DataContainer();
                }
            }
        }
        return mDataContainer;
    }

    public ReceivedPacket getSelectedThumbnail(int index){
        synchronized (mThumbnailList) {
            return mThumbnailList.get(index);
        }
    }

    public void setThumbnailList(List<ReceivedPacket> list) {
        Message message = Message.obtain();
        message.what = MSG_ALL_THUMBNAILS;
        message.obj = list;
        mHandler.sendMessage(message);
    }

    public void setIpAddress(String ipAddress) {
        Message message = Message.obtain();
        message.what = MSG_SET_IP_ADDRESS;
        message.obj = ipAddress;
        mHandler.sendMessage(message);
    }

    public void registerEventListener(IOnEventReceived listener) {
        if (!mIOnEventReceivedList.contains(listener)) {
            mIOnEventReceivedList.add(listener);
        }
    }
    public void unregisterEventListener(IOnEventReceived listener) {
        mIOnEventReceivedList.remove(listener);
    }

    public void registerClientConnectedListener(IClientConnection listener) {
        mIClientConnection = listener;
    }

    public void setActualImageIntoList(ReceivedPacket packet) {
        Message message = Message.obtain();
        message.what = MSG_SELECTED_IMAGE;
        message.obj = packet;
        mHandler.sendMessage(message);
    }

    private class ResponseCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case MSG_ALL_THUMBNAILS:
                    synchronized (mThumbnailList) {
                        final List<ReceivedPacket> list = (List<ReceivedPacket>) message.obj;
                        if (list != null) {
                            mThumbnailList.addAll(list);
                            Log.d(TAG, "handleMessage() called with:  list data: ");
                            for (int i = 0, n=mThumbnailList.size(); i <n ; i++) {
                                Log.d(TAG, "item count: " + i +" getFilePath: " + mThumbnailList
                                        .get(i)
                                        .getFilePath() + "  \n \n and encoded bitmap: "
                                        +mThumbnailList.get(i)
                                        .getEncodedBitmap() );
                            }
                        }
                        else {
                            Log.d(TAG, "handleMessage() called with:list == null");
                        }
                        if(mIOnEventReceivedList != null){
                            for (IOnEventReceived listener: mIOnEventReceivedList) {
                                listener.onThumbnailListReceived(list);
                            }
                        }

                    }
                    break;
                case MSG_SELECTED_IMAGE:
                    //todo ps
                    ReceivedPacket packet = (ReceivedPacket) message.obj;
                    if (packet != null) {mActualImageList.add(packet);
                    }

                    if(mIOnEventReceivedList != null){
                        for (IOnEventReceived listener: mIOnEventReceivedList) {
                            listener.onActualImageReceived(packet);
                        }
                    }

                    break;
                case MSG_SET_IP_ADDRESS:
                    String ip = (String)message.obj;
                    if( mIpAddress == null || !mIpAddress.equals(ip)) {
                        mIpAddress = ip;
                        mIClientConnection.onClientConnected(mIpAddress);
                    }
                    break;
                default:
                    break;

            }
            return false;
        }
    }


}
