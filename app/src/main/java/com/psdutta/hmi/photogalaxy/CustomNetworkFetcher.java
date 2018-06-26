package com.psdutta.hmi.photogalaxy;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.producers.Consumer;
import com.facebook.imagepipeline.producers.NetworkFetcher;
import com.facebook.imagepipeline.producers.ProducerContext;
import com.psdutta.hmi.photogalaxy.data.DataContainer;
import com.psdutta.hmi.photogalaxy.data.ReceivedPacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class CustomNetworkFetcher implements NetworkFetcher<FetchRequest> {

    private DataContainer dataContainer = DataContainer.getInstance();

    @Override
    public FetchRequest createFetchState(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
        return new FetchRequest(consumer, producerContext);
    }

    @Override
    public void fetch(FetchRequest fetchState, Callback callback) {
        Log.d("TTTT", "fetch: called");

        try {
            Uri fileUri = fetchState.getUri();


            String filePath = fileUri.toString();
            filePath = filePath.substring(7);
            Log.d("TTTT", "Uri: " + fileUri + " Path: " + filePath);
            ReceivedPacket receivedPacket = dataContainer.getSelectedPacket(filePath);
            if (receivedPacket != null) {
                byte[] encodeByte = Base64.decode(receivedPacket.getEncodedBitmap(), Base64.DEFAULT);
                InputStream targetStream = new ByteArrayInputStream(encodeByte);
                callback.onResponse(targetStream, encodeByte.length);
            } else {
                callback.onFailure(new Throwable("Error decoding image"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            callback.onFailure(new Throwable("Error decoding image"));
        }
    }

    @Override
    public boolean shouldPropagate(FetchRequest fetchState) {
        return false;
    }

    @Override
    public void onFetchCompletion(FetchRequest fetchState, int byteSize) {

    }

    @Nullable
    @Override
    public Map<String, String> getExtraMap(FetchRequest fetchState, int byteSize) {
        return null;
    }

}
