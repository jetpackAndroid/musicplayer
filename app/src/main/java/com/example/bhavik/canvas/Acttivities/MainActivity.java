package com.example.bhavik.canvas.Acttivities;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.bhavik.canvas.CustomAdapters.CustomFragmentStatePagerAdapter;
import com.example.bhavik.canvas.CustomViews.SlidingTabLayout;
import com.example.bhavik.canvas.Fragments.SingleMusicFragment;
import com.example.bhavik.canvas.Modal.Songs;
import com.example.bhavik.canvas.R;
import com.example.bhavik.canvas.Service.MusicService;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    public static MusicService musicService;
    Intent playIntent;
    boolean musicBound = false;
    String fragmentTAG = null;
    private static MainActivity mainActivity;
    private Fragment currentFragment;
    private ViewPager viewPager;
    private FrameLayout mainContainer;
    private SlidingTabLayout musicTabs;
    private CustomFragmentStatePagerAdapter customFragmentStatePagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setMainActivity(this);

        if (playIntent == null && musicBound == false) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, this.BIND_AUTO_CREATE);
            startService(playIntent);
        }

        viewPager = (ViewPager) findViewById(R.id.mainPager);


        musicTabs = (SlidingTabLayout) findViewById(R.id.musicTabs);
        musicTabs.setDistributeEvenly(true);


        customFragmentStatePagerAdapter = new CustomFragmentStatePagerAdapter(getSupportFragmentManager());
        mainContainer = (FrameLayout) findViewById(R.id.mainContainer);

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
            viewPager.setAdapter(customFragmentStatePagerAdapter);

//            Always pass view pager after setting adapter to pager.
            musicTabs.setViewPager(viewPager);

            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicBound = false;
        }

    };

    public static MusicService getMusicService() {
        return musicService;
    }

    public static void setMusicService(MusicService musicService) {
        MainActivity.musicService = musicService;
    }

    public void applyFragment(String TAG, Bundle bundle){
        fragmentTAG = TAG;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, getFragment(TAG, bundle)).addToBackStack(null).commitAllowingStateLoss();
    }
    public Fragment getFragment(String tag, Bundle bundle){
        Fragment fragment = null;
        if(tag.equalsIgnoreCase(SingleMusicFragment.class.getSimpleName())){
            fragment = getSupportFragmentManager().findFragmentByTag(tag);
            if(fragment == null){
                viewPager.setVisibility(View.GONE);
                musicTabs.setVisibility(View.GONE);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            viewPager.setVisibility(View.VISIBLE);
            musicTabs.setVisibility(View.VISIBLE);
            getSupportActionBar().show();

            viewPager.setCurrentItem(0);
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

}
