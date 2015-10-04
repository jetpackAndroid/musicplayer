package com.example.bhavik.canvas.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bhavik.canvas.Acttivities.MainActivity;
import com.example.bhavik.canvas.R;

/**
 * Created by Bhavik on 10/3/2015.
 */
public class ArtistFragment extends BaseFragment {
    int fragmentPosition;
    TextView dummyTextView;

    public static AlbumFragment init(int position) {
        AlbumFragment albumFragment = new AlbumFragment();
        // supply position as a value of argument.
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION", position);
        albumFragment.setArguments(bundle);
        return albumFragment;
    }

    public ArtistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentPosition = getArguments() != null ? getArguments().getInt("val") : 20;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.album_fragment, container, false);
        Toast.makeText(MainActivity.getMainActivity(), " " + getArguments().getInt("val"), Toast.LENGTH_LONG).show();

        return view;
    }

}
