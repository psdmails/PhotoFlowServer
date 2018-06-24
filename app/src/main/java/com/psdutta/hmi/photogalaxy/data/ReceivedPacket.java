package com.psdutta.hmi.photogalaxy.data;

public class ReceivedPacket {
    private final String filePath;
    private final String encodedBitmap;

    public ReceivedPacket(String path, String enBitmap) {
        filePath = path;
        encodedBitmap = enBitmap;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getEncodedBitmap() {
        return encodedBitmap;
    }
}
