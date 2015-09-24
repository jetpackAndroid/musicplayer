package com.example.bhavik.canvas.CustomAdapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.bhavik.canvas.Modal.Songs;
import com.example.bhavik.canvas.R;

import java.util.ArrayList;

/**
 * Created by Bhavik on 9/20/2015.
 */
public class CustomPagerAdapter extends PagerAdapter {
    ArrayList<Songs> mSongsArrayList;
    LayoutInflater inflater;
    Context context;
    int currentSongIndex;

    public CustomPagerAdapter(ArrayList<Songs> songsArrayList, Context context, int currentSongIndex) {
        mSongsArrayList = songsArrayList;
        this.context = context;
        this.currentSongIndex = currentSongIndex;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return mSongsArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView albumArtImageView;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.view_pager_item, container, false);

        albumArtImageView = (ImageView) itemView.findViewById(R.id.songAlbumArt);
        albumArtImageView.setImageURI(Uri.parse(mSongsArrayList.get(position).getAlbumArtPath()));

        container.addView(itemView);

        return itemView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view.equals(object));
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
