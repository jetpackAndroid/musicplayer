package com.example.bhavik.canvas.CustomAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.bhavik.canvas.Acttivities.MainActivity;
import com.example.bhavik.canvas.Fragments.AlbumFragment;
import com.example.bhavik.canvas.Fragments.ArtistFragment;
import com.example.bhavik.canvas.Fragments.MusicFragment;
import com.example.bhavik.canvas.Fragments.PlaylistFragment;
import com.example.bhavik.canvas.Fragments.RecetlyPlayedFragment;

/**
 * Created by Bhavik on 10/2/2015.
 */
public class CustomFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private MainActivity mActivity;

    public CustomFragmentStatePagerAdapter(FragmentManager fm, MainActivity activity) {
        super(fm);
        mActivity = activity;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
//                MusicFragment will be called i.e All Music in a list will be shown.
                return MusicFragment.init(position, mActivity);
            case 1:
//                Album
                return AlbumFragment.init(position);
            case 2:
//                Artist
                return ArtistFragment.init(position);
            case 3:
//                Playlist
                return PlaylistFragment.init(position);
            case 4:
//                Recently Added
                return RecetlyPlayedFragment.init(position);


        }
//        return MusicFragment.init(position, mActivity);

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Songs";
            case 1:
                return "Albums";
            case 2:
//                Artist
                return "Artist";
            case 3:
//                Playlist
                return "Playlist";
            case 4:
//                Recently Added
                return "Recently Played";
        }
        return null;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return 5;
    }

}
