package com.psdutta.hmi.photogalaxy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.psdutta.hmi.photogalaxy.adapter.GridAdapter;
import com.psdutta.hmi.photogalaxy.connection.PhotoGalaxyManager;
import com.psdutta.hmi.photogalaxy.data.Constants;
import com.psdutta.hmi.photogalaxy.data.DataContainer;
import com.psdutta.hmi.photogalaxy.data.ReceivedPacket;
import com.psdutta.hmi.photogalaxy.details.FullScreenImageActivity;
import com.psdutta.hmi.photogalaxy.event.IOnEventReceived;
import com.psdutta.hmi.photogalaxy.parser.JsonParser;

import java.util.List;

public class GalaxyMainActivity extends AppCompatActivity implements View.OnClickListener, IOnEventReceived {

    private static final String TAG = "GalaxyMainActivity";

    private PhotoGalaxyManager mPhotoGalaxyManager;
    private DataContainer mDataContainer;
    private ImageView mImageView;

    private GridView mGridView;
    private RelativeLayout mLoadingLayout;
    private ViewGroup mViewPagerContainer;
    //private AutoScrollViewPager mViewPager;
    private Button leftArrow;
    private Button rightArrow;
    public GridAdapter mGridAdapter;
    private ImageButton closeButton;
    private Button fullScreenButton;
    private Button loadPics;
    //private ViewpagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galaxy_main);
        mPhotoGalaxyManager = PhotoGalaxyManager.getInstance();
        //findViewById(R.id.all).setOnClickListener(this);
        //findViewById(R.id.specific).setOnClickListener(this);

        mLoadingLayout = findViewById(R.id.progress_container);
        mGridView = findViewById(R.id.grid_view);

        loadPics = findViewById(R.id.loadPhotos);

        loadPics.setOnClickListener(this);

        mImageView = findViewById(R.id.full_img);
        mDataContainer = DataContainer.getInstance();
        mDataContainer.registerEventListener(this);

        mGridAdapter = new GridAdapter(this);
        mGridView.setAdapter(mGridAdapter);

        // Implement On Item click listener
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GalaxyMainActivity.this, FullScreenImageActivity.class);
                intent.putExtra(Constants.EXTRA_SELECTED_IMAGE_POS, position);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataContainer.unregisterEventListener(this);
    }

    private void requestAllThumbs() {
        byte[] packet;
        packet = getPacket(Constants.ALL, "null");
        mPhotoGalaxyManager.sendPacket(packet);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.loadPhotos:
                requestAllThumbs();
                loadPics.setVisibility(View.GONE);
                mLoadingLayout.setVisibility(View.VISIBLE);
            default:
                break;
        }
    }

    private byte[] getPacket(String requestType, String filePath) {
        return JsonParser.prepareRequest(requestType, filePath);
    }

    @Override
    public void onThumbnailListReceived(final List<ReceivedPacket> packetList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "run() called");
                mLoadingLayout.setVisibility(View.GONE);
                mGridView.setVisibility(View.VISIBLE);

                mGridAdapter.setData(packetList);
                mGridAdapter.notifyDataSetChanged();
                /*mImageView.setImageBitmap(BitmapGenerator.stringToBitMap
                        (packetList.get(0).getEncodedBitmap()));*/
            }
        });

    }

    @Override
    public void onActualImageReceived(final ReceivedPacket packet) {

    }
}
