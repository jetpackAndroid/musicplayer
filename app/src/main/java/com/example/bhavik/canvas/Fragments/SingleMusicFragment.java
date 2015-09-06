package com.example.bhavik.canvas.Fragments;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.bhavik.canvas.Acttivities.MainActivity;
import com.example.bhavik.canvas.R;


public class SingleMusicFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = SingleMusicFragment.class.getSimpleName();
    ImageView playPause, stepBackward, stepForward;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // set listeners
        playPause.setOnClickListener(SingleMusicFragment.this);
        stepBackward.setOnClickListener(SingleMusicFragment.this);
        stepForward.setOnClickListener(SingleMusicFragment.this);



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
