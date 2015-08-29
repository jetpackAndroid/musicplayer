package com.example.bhavik.canvas.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bhavik.canvas.Acttivities.MainActivity;
import com.example.bhavik.canvas.R;
import com.example.bhavik.canvas.Service.MusicService;

public class MusicFragment extends Fragment {
    public static String TAG = MusicFragment.class.getSimpleName();
    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.music_current_playing, container, false);

        Toast.makeText(getActivity(),"Fragment",Toast.LENGTH_LONG).show();
        return view;
    }

}
