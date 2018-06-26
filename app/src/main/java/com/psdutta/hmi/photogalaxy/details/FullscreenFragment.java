package com.psdutta.hmi.photogalaxy.details;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.psdutta.hmi.photogalaxy.R;
import com.psdutta.hmi.photogalaxy.connection.PhotoGalaxyManager;
import com.psdutta.hmi.photogalaxy.data.BitmapGenerator;
import com.psdutta.hmi.photogalaxy.data.Constants;
import com.psdutta.hmi.photogalaxy.data.DataContainer;
import com.psdutta.hmi.photogalaxy.data.ReceivedPacket;
import com.psdutta.hmi.photogalaxy.event.IOnEventReceived;
import com.psdutta.hmi.photogalaxy.parser.JsonParser;

import java.util.List;

public class FullscreenFragment extends Fragment implements IOnEventReceived {

    public static Fragment getTatti(int pos) {
        FullscreenFragment fragment = new FullscreenFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.EXTRA_SELECTED_IMAGE_POS, pos);
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface Callbacks {
        void moveToNext();
    }

    private String mFullImagePath;
    private ImageView mFullScreenImageView;
    private CountDownTimer mCountDownTimer;
    private TextView mTimerView;
    private Handler handler = new Handler();
    private Callbacks mCallbacks;
    private boolean isLayoutInit;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fullscreen_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        int position = getArguments().getInt(Constants.EXTRA_SELECTED_IMAGE_POS, -1);
        if (position == -1) {
            throw new RuntimeException("Image Path should not empty");
        }
        isLayoutInit = true;
        mTimerView = (TextView) view.findViewById(R.id.timer);
        mFullScreenImageView = view.findViewById(R.id.fullscreen_image);
        DataContainer.getInstance().registerEventListener(this);
        showLowResolutionImage(position);
        startTimer();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isLayoutInit) {
            startTimer();
        } else {
            if (mCountDownTimer != null) {
                mCountDownTimer.cancel();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DataContainer.getInstance().unregisterEventListener(this);
    }

    @Override
    public void onThumbnailListReceived(List<ReceivedPacket> packetList) {

    }

    @Override
    public void onActualImageReceived(ReceivedPacket packet) {
        if (DataContainer.getInstance().hasKey(mFullImagePath.substring(0, mFullImagePath.lastIndexOf("\n")))) {
            if (mFullImagePath.equals(packet.getFilePath())) {
                showHighResolutionbImage(packet.getEncodedBitmap());
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void showLowResolutionImage(final int pos) {
        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... voids) {
                ReceivedPacket receivedPacket = DataContainer.getInstance().getSelectedThumbnail(pos);
                String encodedBitmap = receivedPacket.getEncodedBitmap();
                mFullImagePath = receivedPacket.getFilePath();
                return BitmapGenerator.stringToBitMap(encodedBitmap);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                mFullScreenImageView.setImageBitmap(bitmap);
                DataContainer dataContainer = DataContainer.getInstance();
                ReceivedPacket thumpacket = dataContainer.getSelectedThumbnail(pos);
                ReceivedPacket actualPacket = dataContainer.getActualPacket(thumpacket.getFilePath().substring(0,thumpacket.getFilePath().lastIndexOf("\n")));
                if (actualPacket != null) {
                    showHighResolutionbImage(actualPacket.getEncodedBitmap());
                } else {
                    PhotoGalaxyManager photoGalaxyManager = PhotoGalaxyManager.getInstance();
                    byte[] packet = JsonParser.prepareRequest(
                            Constants.SELECTED,
                            thumpacket.getFilePath()
                    );
                    photoGalaxyManager.sendPacket(packet);
                }

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

    private void startTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mCountDownTimer = new CountDownTimer(21000, 1000) {
            @Override
            public void onTick(final long l) {
                mTimerView.post(new Runnable() {
                    @Override
                    public void run() {
                        mTimerView.setText(String.valueOf(l / 1000));
                    }
                });
            }

            @Override
            public void onFinish() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallbacks.moveToNext();
                    }
                });
            }
        };
        mCountDownTimer.start();
    }
}

