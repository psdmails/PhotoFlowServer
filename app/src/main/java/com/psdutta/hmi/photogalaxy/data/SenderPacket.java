package com.psdutta.hmi.photogalaxy.data;

public class SenderPacket {
    private final String mRequestType;
    private final String mUri;

    public SenderPacket(String requestType, String uri) {
        mRequestType = requestType;
        mUri = uri;
    }

    public String getRequestType() {
        return mRequestType;
    }

    public String getUri() {
        return mUri;
    }
}
