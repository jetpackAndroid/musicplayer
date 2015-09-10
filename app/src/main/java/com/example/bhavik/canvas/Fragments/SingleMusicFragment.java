package com.example.bhavik.canvas.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.bhavik.canvas.Acttivities.MainActivity;
import com.example.bhavik.canvas.Modal.Songs;
import com.example.bhavik.canvas.R;


public class SingleMusicFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = SingleMusicFragment.class.getSimpleName();
    ImageView playPause, stepBackward, stepForward, albumArt;
    SeekBar seekBar;

    Songs song = null;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.single_music_fragment, container, false);
        Toast.makeText(MainActivity.getMainActivity(), "Fragment Applied", Toast.LENGTH_LONG).show();
        // find elements
        playPause = (ImageView) view.findViewById(R.id.playPause);
        stepBackward = (ImageView) view.findViewById(R.id.stepBackward);
        stepForward = (ImageView) view.findViewById(R.id.stepForward);
        albumArt = (ImageView) view.findViewById(R.id.singleMusicAlbumArt);
        seekBar = (SeekBar) view.findViewById(R.id.songSeekbar);
        // set listeners
        playPause.setOnClickListener(SingleMusicFragment.this);
        stepBackward.setOnClickListener(SingleMusicFragment.this);
        stepForward.setOnClickListener(SingleMusicFragment.this);

        if (song != null)
            albumArt.setImageURI(song.getAlbumArtPath());

        seekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY));
        return view;
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

                if (MainActivity.getMainActivity().checkIsPlaying()) {
                    MainActivity.getMainActivity().pauseMusic();
                    playPause.setBackgroundResource(R.drawable.play_button_img);
                } else {
                    playPause.setBackgroundResource(R.drawable.pause_button_img);
                    MainActivity.getMainActivity().playMusic();
                }
                Drawable drawable = playPause.getBackground();
                String draw = drawable.toString();
                if (draw.equalsIgnoreCase("mediaplay4x")) {
                    playPause.setBackgroundResource(R.drawable.mediaplay4x);
                }
                break;

            case R.id.stepBackward:
                break;

            case R.id.stepForward:
                break;

            default:
                break;
        }
    }
}
