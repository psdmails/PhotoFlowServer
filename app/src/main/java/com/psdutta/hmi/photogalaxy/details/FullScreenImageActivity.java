package com.psdutta.hmi.photogalaxy.details;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.psdutta.hmi.photogalaxy.R;
import com.psdutta.hmi.photogalaxy.connection.PhotoGalaxyManager;
import com.psdutta.hmi.photogalaxy.data.BitmapGenerator;
import com.psdutta.hmi.photogalaxy.data.Constants;
import com.psdutta.hmi.photogalaxy.data.DataContainer;
import com.psdutta.hmi.photogalaxy.data.ReceivedPacket;
import com.psdutta.hmi.photogalaxy.event.IOnEventReceived;
import com.psdutta.hmi.photogalaxy.parser.JsonParser;

import java.util.List;

public class FullScreenImageActivity extends Activity implements IOnEventReceived {

    private ImageView mFullScreenImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        int position = getIntent().getIntExtra(Constants.EXTRA_SELECTED_IMAGE_POS, -1);
        if (position == -1) {
            throw new RuntimeException("Image Path should not empty");
        }
        mFullScreenImageView = findViewById(R.id.fullscreen_image);
        DataContainer.getInstance().registerEventListener(this);
        showLowResolutionImage(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataContainer.getInstance().unregisterEventListener(this);
    }

    @Override
    public void onThumbnailListReceived(List<ReceivedPacket> packetList) {

    }

    @Override
    public void onActualImageReceived(ReceivedPacket packet) {
        if (packet.getEncodedBitmap() != null) {
            showHighResolutionbImage(packet.getEncodedBitmap());
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void showLowResolutionImage(final int pos) {
        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... voids) {
                String encodedBitmap = DataContainer.getInstance().getSelectedThumbnail(pos).getEncodedBitmap();
                return BitmapGenerator.stringToBitMap(encodedBitmap);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                mFullScreenImageView.setImageBitmap(bitmap);

                PhotoGalaxyManager photoGalaxyManager = PhotoGalaxyManager.getInstance();
                byte[] packet = JsonParser.prepareRequest(
                        Constants.SELECTED,
                        DataContainer.getInstance().getSelectedThumbnail(pos).getFilePath()
                );
                photoGalaxyManager.sendPacket(packet);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void showHighResolutionbImage(final String encodedBitmap) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                return BitmapGenerator.stringToBitMap(encodedBitmap);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                mFullScreenImageView.setImageBitmap(bitmap);
            }
        }.execute();
    }
}
