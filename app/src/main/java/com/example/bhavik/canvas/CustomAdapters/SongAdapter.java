package com.example.bhavik.canvas.CustomAdapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bhavik.canvas.Modal.Songs;
import com.example.bhavik.canvas.R;

import java.util.ArrayList;


/**
 * Created by Bhavik on 8/22/2015.
 */
public final class SongAdapter extends BaseAdapter {

    private ArrayList<Songs> songsList;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private static SongAdapter songAdapterInstance = null;

    private SongAdapter(Context context, ArrayList<Songs> theList) {
        this.songsList = theList;
        mContext = context;
        this.layoutInflater = ( LayoutInflater )context. getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static SongAdapter getSongAdapterInstance(Context context, ArrayList<Songs> theList) {
        if (songAdapterInstance == null) {
            songAdapterInstance = new SongAdapter(context, theList);
        }
        return songAdapterInstance;
    }

    @Override

    public int getCount() {
        return songsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder{
        TextView songTitle, artistName;
        ImageView imageView;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        ViewHolder holder;
        if(view == null){
            vi =  layoutInflater.inflate(R.layout.music_list,null);
            holder = new ViewHolder();

            holder.songTitle = (TextView) vi.findViewById(R.id.songTitle);
            holder.artistName = (TextView) vi.findViewById(R.id.artistName);
            holder.imageView = (ImageView) vi.findViewById(R.id.albumArt);

            vi.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) vi.getTag();
        }
        if(songsList.size() > 0 ){
            Songs currentSong = songsList.get(i);
            holder.songTitle.setText(currentSong.getTitle());
            holder.artistName.setText(currentSong.getArtist());
            holder.imageView.setImageURI(Uri.parse(currentSong.getAlbumArtPath()));
//            final int b = i;
//            vi.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mMusicFragment.onItemClick(null, view, b, -1);
//                }
//            });
//            holder.songTitle.setTag(i);
            holder.artistName.setTag(i);
        }
        return vi;
    }
}
