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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReceivedPacket that = (ReceivedPacket) o;

        return filePath != null ? filePath.equals(that.filePath) : that.filePath == null;
    }

    @Override
    public int hashCode() {
        return filePath != null ? filePath.hashCode() : 0;
    }
}
