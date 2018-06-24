package com.psdutta.hmi.photogalaxy.event;


public class EventManager {
    private static final Object LOCK_OBJECT = new Object();
    private static EventManager mEventManager;
    private RequestEventHandler mRequestEventHandler;

    private EventManager() {
        mRequestEventHandler = new RequestEventHandler();
    }

    public static EventManager getInstance() {
        if (mEventManager == null) {
            synchronized (LOCK_OBJECT) {
                if (mEventManager == null) {
                    mEventManager = new EventManager();
                }
            }
        }
        return mEventManager;
    }

    public void sendRequestEvent(byte[] packet) {
        mRequestEventHandler.processEvent(packet);
    }

}
