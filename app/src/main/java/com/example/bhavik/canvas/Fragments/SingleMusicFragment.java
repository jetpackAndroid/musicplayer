package com.example.bhavik.canvas.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.bhavik.canvas.Action.action;
import com.example.bhavik.canvas.Acttivities.MainActivity;
import com.example.bhavik.canvas.CustomAdapters.CustomPagerAdapter;
import com.example.bhavik.canvas.CustomAdapters.SongAdapter;
import com.example.bhavik.canvas.Modal.Songs;
import com.example.bhavik.canvas.R;

/**
 * I am starting SeekBar updation in OnResume and stoping it in OnStop(), Just handle runnable.
 */
public class SingleMusicFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = SingleMusicFragment.class.getSimpleName();
    public static ImageView playPause, stepBackward, stepForward;
    public static SeekBar seekBar;
    Handler handler;
    public SongAdapter songAdapter;
    public static TextView songDetails, songDuration;
    static int currentSongDuration;
    Songs song = null, currentPlayingSong = null;
    public static ViewPager viewPager;
    public CustomPagerAdapter pagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        if (args.containsKey("Song")) {
            song = (Songs) args.getSerializable("Song");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        seekBarUpdater(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.single_music_fragment, container, false);
        MainActivity.getMainActivity().getSupportActionBar().hide();

        // Play song here instead of MusicFragment to avoid nullpointer in service for albumart issue.
        playMusic();

        handler = new Handler();
        songAdapter = SongAdapter.getSongAdapterInstance(MainActivity.getMainActivity(), getCompleteSongList());

//         find elements
        playPause = (ImageView) view.findViewById(R.id.playPause);
        stepBackward = (ImageView) view.findViewById(R.id.stepBackward);
        stepForward = (ImageView) view.findViewById(R.id.stepForward);
        seekBar = (SeekBar) view.findViewById(R.id.songSeekbar);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        songDetails = (TextView) view.findViewById(R.id.songDetail);
        songDuration = (TextView) view.findViewById(R.id.songDuration);
//        set adapter
        pagerAdapter = new CustomPagerAdapter(getCompleteSongList(), MainActivity.getMainActivity(), currentSongIndex());
        viewPager.setCurrentItem(currentSongIndex());
        viewPager.setAdapter(pagerAdapter);

//         set listeners
        playPause.setOnClickListener(SingleMusicFragment.this);
        stepBackward.setOnClickListener(SingleMusicFragment.this);
        stepForward.setOnClickListener(SingleMusicFragment.this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("demo", "onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
                setMusicAtSwipedPosition(position);
                playPause.setImageDrawable(null);
                playPause.setBackgroundResource(R.drawable.pause_button_img);
//                songDetails.setText(getCurrentPlayingSong().getTitle() + " / " + getCurrentPlayingSong().getArtist());
//                songDuration.setText(getCurrentMusicPosition() + " / " + getMusicDuration());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("demo", "onPageScrollStateChanged");
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int mProgress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekToPosition(mProgress);
            }
        });

        if (checkMusicIsPlaying())
            setSongConstraints();

        seekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY));
        return view;
    }

    public void setSongConstraints() {
        currentPlayingSong = getCurrentPlayingSong();
        if (currentPlayingSong != null) {
//            albumArt.setImageURI(Uri.parse(currentPlayingSong.getAlbumArtPath()));
            currentSongDuration = getMusicDuration();
        }
    }

    private void seekBarUpdater(boolean runCallBack) {
        if (runCallBack)
            handler.postDelayed(seekBarUpdate, 100);
        else
            handler.removeCallbacks(seekBarUpdate);
    }

    public Runnable seekBarUpdate = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(getCurrentMusicPosition());
            if (checkMusicIsPlaying()) {
                int eMinutes = getCurrentMusicPosition() / 60000;
                float eSeconds = getCurrentMusicPosition() % 60000;
                int tMinutes = getMusicDuration() / 60000;
                float tSeconds = getMusicDuration() % 60000;
                songDuration.setText(eMinutes + ":" + eSeconds + " / " + tMinutes + ":" + tSeconds);
            }
            handler.postDelayed(seekBarUpdate, 100);
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        seekBarUpdater(false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.playPause:

                if (checkMusicIsPlaying()) {
                    pauseMusic();
                    //setImageDrawable null to avoid imposing of one image on another.
                    playPause.setImageDrawable(null);
                    playPause.setBackgroundResource(R.drawable.play_button_img);

                } else {
                    //setImageDrawable null to avoid imposing of one image on another.
                    playPause.setImageDrawable(null);
                    playPause.setBackgroundResource(R.drawable.pause_button_img);
                    startMusic();
                }
                break;

            case R.id.stepBackward:
                if (currentSongIndex() > 0)
                    playPrevious();
                break;

            case R.id.stepForward:
                // TODO Put condition if current song index is equal to last song then it should not
                playNext();
                break;

            default:
                break;
        }
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
                    SingleMusicFragment.seekBar.setMax(getCurrentPlayingSong().getDuration());
                    songDetails.setText(getCurrentPlayingSong().getTitle() + " / " + getCurrentPlayingSong().getArtist());
                    songDetails.setSelected(true);
                    SingleMusicFragment.viewPager.setCurrentItem(currentSongIndex());
                    break;
            }

        }
    }
}
