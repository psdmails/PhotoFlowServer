package com.psdutta.hmi.photogalaxy.queue;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.psdutta.hmi.photogalaxy.event.EventManager;

class PacketRequestQueueHandler {
    private Handler mHandler;
    private EventManager mEventManager;

    PacketRequestQueueHandler(EventManager eventManager) {
        mEventManager = eventManager;
        createHandlerThread();
    }

    private void createHandlerThread() {
        HandlerThread thread = new HandlerThread(PacketRequestQueueHandler.class.getSimpleName());
        thread.start();
        mHandler = new Handler(thread.getLooper(), new ResponseCallback());
    }

    private class ResponseCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(Message message) {
            mEventManager.sendRequestEvent((byte[]) message.obj);
            return false;
        }
    }

    void addRequestIntoQueue(byte[] packet) {
        Message message = Message.obtain();
        message.obj = packet;
        mHandler.sendMessage(message);
    }
}
