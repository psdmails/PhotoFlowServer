package com.psdutta.hmi.photogalaxy.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.psdutta.hmi.photogalaxy.R;
import com.psdutta.hmi.photogalaxy.data.Constants;
import com.psdutta.hmi.photogalaxy.data.DataContainer;
import com.psdutta.hmi.photogalaxy.data.ReceivedPacket;

import java.util.ArrayList;
import java.util.List;

public class SlideshowActivity extends AppCompatActivity implements FullscreenFragment.Callbacks {

    private List<ReceivedPacket> receivedPacketList = new ArrayList<>();
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_slideshow);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        receivedPacketList.addAll(DataContainer.getInstance().getmThumbnailList());
        //receivedPacketList.add(DataContainer.getInstance().getmThumbnailList().get(0));
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        SlideShowPagerAdapter slideShowPagerAdapter = new SlideShowPagerAdapter(
                this,
                getSupportFragmentManager(),
                receivedPacketList
        );
        viewPager.setAdapter(slideShowPagerAdapter);

        int startPosition = getIntent().getIntExtra(Constants.EXTRA_SELECTED_IMAGE_POS, 0);
        viewPager.setCurrentItem(startPosition);
    }

    @Override
    public void moveToNext() {
        int currentIndex = viewPager.getCurrentItem();
        int nextItem = currentIndex + 1;
        if (nextItem >= receivedPacketList.size()) {
            nextItem = 0;
        }
        viewPager.setCurrentItem(nextItem);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideStatusBar();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideStatusBar();
    }

    protected void hideStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
