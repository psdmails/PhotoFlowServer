package com.psdutta.hmi.photogalaxy.parser;

import com.psdutta.hmi.photogalaxy.data.Constants;
import com.psdutta.hmi.photogalaxy.data.ReceivedPacket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    public static List<ReceivedPacket> parseAllThumbnail(JSONObject object) {
        final List<ReceivedPacket> list = new ArrayList<>();
        try {
            JSONArray array = object.getJSONArray(Constants.IMAGE_DATA);
            for (int i = 0; i < array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
                list.add(new ReceivedPacket(row.getString(Constants.FILE_PATH), row.getString
                        (Constants.IMAGE_DATA)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ReceivedPacket parseImage(JSONObject object) {
        ReceivedPacket packet = null;
        try {
            JSONObject jsonObject =  object.getJSONObject(Constants.DATA);
            JSONArray jsonArray = jsonObject.getJSONArray(Constants.IMAGE_DATA);
            StringBuilder imageData = new StringBuilder();
            for(int i= 0; i<jsonArray.length(); i++){
                imageData.append(jsonArray.get(i));
            }
            packet = new ReceivedPacket(jsonObject.getString(Constants.FILE_PATH), imageData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return packet;
    }

    public static byte[] prepareRequest(String requestType, String filePath){
        JSONObject object = new JSONObject();
        try {
            object.put(Constants.REQUEST_TYPE,requestType);
            object.put(Constants.FILE_PATH,filePath);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return (object.toString()).getBytes();
    }
}
