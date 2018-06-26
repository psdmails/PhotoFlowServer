package com.psdutta.hmi.photogalaxy.home;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.psdutta.hmi.photogalaxy.R;
import com.psdutta.hmi.photogalaxy.connection.PhotoGalaxyManager;
import com.psdutta.hmi.photogalaxy.data.Constants;
import com.psdutta.hmi.photogalaxy.data.DataContainer;
import com.psdutta.hmi.photogalaxy.data.ReceivedPacket;
import com.psdutta.hmi.photogalaxy.event.IOnEventReceived;
import com.psdutta.hmi.photogalaxy.parser.JsonParser;

import java.util.ArrayList;
import java.util.List;

import static android.os.Process.killProcess;
import static android.os.Process.myPid;

public class HomeActivity extends AppCompatActivity implements IOnEventReceived {

    private ProgressBar mProgressBar;
    private Button mGetAllButtons;
    private HomeRecyclerAdapter mRecyclerAdapter;
    private List<ReceivedPacket> mReceivedPackets = new ArrayList<>();
    private PhotoGalaxyManager mPhotoGalaxyManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                killProcess(myPid());
            }
        });

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Here are your photos!");
        }

        mPhotoGalaxyManager = PhotoGalaxyManager.getInstance();
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mGetAllButtons = (Button) findViewById(R.id.get_all_pics);
        findViewById(R.id.get_all_pics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllPics();
            }
        });
        DataContainer.getInstance().registerEventListener(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //recyclerView.addItemDecoration(new DividerItemDecorator(10, 10));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerAdapter = new HomeRecyclerAdapter(this, mReceivedPackets);
        recyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataContainer.getInstance().unregisterEventListener(this);
    }

    private void getAllPics() {
        mGetAllButtons.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        byte[] packet = JsonParser.prepareRequest(Constants.ALL, "null");
        mPhotoGalaxyManager.sendPacket(packet);

    }

    @Override
    public void onThumbnailListReceived(final List<ReceivedPacket> packetList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
                mReceivedPackets.addAll(packetList);
                mRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onActualImageReceived(final ReceivedPacket packet) {

    }
}
