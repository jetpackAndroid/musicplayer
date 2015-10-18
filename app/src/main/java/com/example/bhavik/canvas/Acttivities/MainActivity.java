package com.example.bhavik.canvas.Acttivities;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.bhavik.canvas.Action.action;
import com.example.bhavik.canvas.CustomAdapters.CustomFragmentStatePagerAdapter;
import com.example.bhavik.canvas.CustomViews.SlidingTabLayout;
import com.example.bhavik.canvas.Fragments.SingleMusicFragment;
import com.example.bhavik.canvas.Modal.Songs;
import com.example.bhavik.canvas.R;
import com.example.bhavik.canvas.Service.MusicService;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    public static MusicService musicService;
    Intent playIntent;
    boolean musicBound = false;
    String fragmentTAG = null;
    private static MainActivity mainActivity;
    private ViewPager songTypeViewPager;
    public FrameLayout mainContainer;
    private SlidingTabLayout musicTabs;
    private CustomFragmentStatePagerAdapter customFragmentStatePagerAdapter;
    public static SlidingUpPanelLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setMainActivity(MainActivity.this);

        if (playIntent == null && musicBound == false) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, this.BIND_AUTO_CREATE);
            startService(playIntent);
        }


        MainActivity.getMainActivity().mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        songTypeViewPager = (ViewPager) findViewById(R.id.mainPager);

        musicTabs = (SlidingTabLayout) findViewById(R.id.musicTabs);
        musicTabs.setDistributeEvenly(true);

        customFragmentStatePagerAdapter = new CustomFragmentStatePagerAdapter(getSupportFragmentManager(), MainActivity.getMainActivity());
//        mainContainer = (FrameLayout) findViewById(R.id.mainContainer);

        mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("slidePanel", "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelExpanded(View panel) {
                Log.i("slidePanel", "onPanelExpanded");
            }

            @Override
            public void onPanelCollapsed(View panel) {
                Log.i("slidePanel", "onPanelCollapsed");
            }

            @Override
            public void onPanelAnchored(View panel) {
                Log.i("slidePanel", "onPanelAnchored");
            }

            @Override
            public void onPanelHidden(View panel) {
                Log.i("slidePanel", "onPanelHidden");
            }
        });

//        TextView t = (TextView) findViewById(R.id.main);
//        t.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//            }
//        });
//
//        t = (TextView) findViewById(R.id.name);
//        t.setText(Html.fromHtml(getString(R.string.hello)));
//        Button f = (Button) findViewById(R.id.follow);
//        f.setText(Html.fromHtml(getString(R.string.follow)));
//        f.setMovementMethod(LinkMovementMethod.getInstance());
//        f.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse("http://www.twitter.com/umanoapp"));
//                startActivity(i);
//            }
//        });

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    public ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)iBinder;
            //get service
            musicService = binder.getService();

            MainActivity.setMusicService(musicService);
            // Start Main fragment after service connection. Just to pass song list otherwise Fragment will be called first then service,
            // which gives NULL pointer Exception
//            MusicFragment musicFragment = new MusicFragment();
//            getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, musicFragment, MusicFragment.TAG).addToBackStack(null).commit();
            songTypeViewPager.setAdapter(customFragmentStatePagerAdapter);

//            Always pass view pager after setting adapter to pager.
            musicTabs.setViewPager(songTypeViewPager);

            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicBound = false;
        }

    };

    public void onSlidingHeaderClick(View view) {
        if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

        }
    }

    public static MusicService getMusicService() {
        return musicService;
    }

    public static void setMusicService(MusicService musicService) {
        MainActivity.musicService = musicService;
    }

    public void applyFragment(String TAG, Bundle bundle){
        fragmentTAG = TAG;
        songTypeViewPager.setVisibility(View.GONE);
        musicTabs.setVisibility(View.GONE);
        mainContainer.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.mainContainer, getFragment(TAG, bundle)).addToBackStack(SingleMusicFragment.TAG).commitAllowingStateLoss();
    }
    public Fragment getFragment(String tag, Bundle bundle){
        Fragment fragment = null;
        if(tag.equalsIgnoreCase(SingleMusicFragment.class.getSimpleName())){
            fragment = getSupportFragmentManager().findFragmentByTag(tag);
            if(fragment == null){

                fragment = new SingleMusicFragment();
            }
            if(bundle != null){
                fragment.setArguments(bundle);
            }
        }

        return fragment;
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
//        stopService(playIntent);
//        musicService = null;
        super.onDestroy();
        unbindService(musicConnection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.demo, menu);
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (mLayout != null) {
            if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN) {
                item.setTitle(R.string.action_show);
            } else {
                item.setTitle(R.string.action_hide);
            }
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.action_shuffle:
                musicService.setShuffle();
                break;
            case R.id.action_end:
                stopService(playIntent);
                musicService = null;
                System.exit(0);
                break;
            case R.id.action_toggle: {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                        item.setTitle(R.string.action_show);
                    } else {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        item.setTitle(R.string.action_hide);
                    }
                }
                return true;
            }
            case R.id.action_anchor: {
                if (mLayout != null) {
                    if (mLayout.getAnchorPoint() == 1.0f) {
                        mLayout.setAnchorPoint(0.7f);
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                        item.setTitle(R.string.action_anchor_disable);
                    } else {
                        mLayout.setAnchorPoint(1.0f);
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        item.setTitle(R.string.action_anchor_enable);
                    }
                }
                return true;
            }
        }

//        noinspection SimplifiableIfStatement
//        if (id == R.id.action_shuffle) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        MainActivity.mainActivity = mainActivity;
    }

    // Calling from MusicFragment to pass song list to service.
    public void passMusicList(ArrayList<Songs> songList){
        musicService.setMusicList(songList);
    }

    public void passSelectedSongPosition(int position) {
        musicService.setSong(position);
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStack();

//        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//            super.onBackPressed();
//        } else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
//            viewPager.setVisibility(View.VISIBLE);
//            musicTabs.setVisibility(View.VISIBLE);
//            getSupportActionBar().show();
//
//            viewPager.setCurrentItem(0);
//        }
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            songTypeViewPager.setVisibility(View.VISIBLE);
            musicTabs.setVisibility(View.VISIBLE);
//            mainContainer.setVisibility(View.GONE);
            getSupportActionBar().show();
            songTypeViewPager.setCurrentItem(0);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                songTypeViewPager.setVisibility(View.VISIBLE);
                musicTabs.setVisibility(View.VISIBLE);
                getSupportActionBar().show();
                songTypeViewPager.setCurrentItem(0);
            } else
                super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    /**
     * Music Events
     */

    public boolean checkIsPlaying() {
        return MainActivity.musicService.isPng();
    }

    public void pauseMusic() {
        MainActivity.musicService.pausePlayer();
    }

    public class SeekbarHandler extends Handler {


        /**
         * Default constructor associates this handler with the {@link Looper} for the
         * current thread.
         * <p/>
         * If this thread does not have a looper, this handler won't be able to receive messages
         * so an exception is thrown.
         */
        public SeekbarHandler() {

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case action.PLAY_SONG:
                    Log.d("test", "test");
//                    SingleMusicFragment.seekBar.setMax(getCurrentPlayingSong().getDuration());
//                    songDetails.setText(getCurrentPlayingSong().getTitle() + " / " + getCurrentPlayingSong().getArtist());
//                    songDetails.setSelected(true);
//                    SingleMusicFragment.viewPager.setCurrentItem(currentSongIndex());
                    break;
            }

        }
    }
}
