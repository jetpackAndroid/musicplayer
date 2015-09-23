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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

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
    public static ImageView playPause, stepBackward, stepForward, albumArt;
    public static SeekBar seekBar;
    Handler handler;
    public SongAdapter songAdapter;
    static int currentSongDuration;
    Songs song = null, currentPlayingSong = null;
    public ViewPager viewPager;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.single_music_fragment, container, false);
        handler = new Handler();
        songAdapter = SongAdapter.getSongAdapterInstance(MainActivity.getMainActivity(), getCompleteSongList());
        Toast.makeText(MainActivity.getMainActivity(), "Fragment Applied", Toast.LENGTH_LONG).show();

//         find elements
        playPause = (ImageView) view.findViewById(R.id.playPause);
        stepBackward = (ImageView) view.findViewById(R.id.stepBackward);
        stepForward = (ImageView) view.findViewById(R.id.stepForward);
//        albumArt = (ImageView) view.findViewById(R.id.singleMusicAlbumArt);
        seekBar = (SeekBar) view.findViewById(R.id.songSeekbar);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

//        set adapter
        pagerAdapter = new CustomPagerAdapter(getCompleteSongList(), MainActivity.getMainActivity(), currentSongIndex());
        viewPager.setCurrentItem(currentSongIndex());
//        viewPager.setBackgroundColor(Color.RED);
        viewPager.setAdapter(pagerAdapter);

//         set listeners
        playPause.setOnClickListener(SingleMusicFragment.this);
        stepBackward.setOnClickListener(SingleMusicFragment.this);
        stepForward.setOnClickListener(SingleMusicFragment.this);
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
//        else if (song != null)
//            albumArt.setImageURI(Uri.parse(song.getAlbumArtPath()));

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
            handler.postDelayed(seekBarUpdate, 100);
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        seekBarUpdater(false);
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
                playPrevious();
                break;

            case R.id.stepForward:
                playNext();
                break;

            default:
                break;
        }
    }

    public static class SeekbarHandler extends Handler {


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
            Bundle bundle = msg.getData();
            if (bundle.containsKey("CurrentSong")) {
                Songs currentSong = (Songs) bundle.getSerializable("CurrentSong");
                SingleMusicFragment.seekBar.setMax(currentSong.getDuration());
//                SingleMusicFragment.albumArt.setImageURI(Uri.parse(currentSong.getAlbumArtPath()));
            }
        }
    }
}
