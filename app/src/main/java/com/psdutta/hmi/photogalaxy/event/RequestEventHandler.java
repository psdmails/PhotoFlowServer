package com.psdutta.hmi.photogalaxy.event;

import android.util.Log;

import com.psdutta.hmi.photogalaxy.data.Constants;
import com.psdutta.hmi.photogalaxy.data.DataContainer;
import com.psdutta.hmi.photogalaxy.data.ReceivedPacket;
import com.psdutta.hmi.photogalaxy.parser.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

class RequestEventHandler {

    private DataContainer mDataContainer;

    RequestEventHandler() {
        mDataContainer = DataContainer.getInstance();
    }

    void processEvent(byte[] packet) {
        String key = "";
        String filePath = "", bitmapString = "";
        JSONObject jsonObject = null;
        try {
            String packetString = new String(packet);
            Log.d("TTTT", "Packet Strin: " + packetString);
            jsonObject = new JSONObject(new String(packet));
            key = jsonObject.getString(Constants.REQUEST_TYPE);
            filePath = jsonObject.getString(Constants.FILE_PATH);
            bitmapString = jsonObject.getString(Constants.IMAGE_DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (key) {
            case Constants.SELECTED:
                ReceivedPacket receivedPacket = new ReceivedPacket(filePath, bitmapString);
                mDataContainer.setActualImageIntoList(receivedPacket);
                break;
            case Constants.ALL:
                mDataContainer.setThumbnailList(JsonParser.parseAllThumbnail(jsonObject));
                break;
            default:
                break;
        }
    }
}
