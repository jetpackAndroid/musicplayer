package com.example.bhavik.canvas.Fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bhavik.canvas.Acttivities.MainActivity;
import com.example.bhavik.canvas.CustomAdapters.SongAdapter;
import com.example.bhavik.canvas.Modal.Songs;
import com.example.bhavik.canvas.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MusicFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    public static String TAG = MusicFragment.class.getSimpleName();
    Intent playIntent;
    boolean musicBound = false;
    private ArrayList<Songs> songList;
    private ListView songsView;
    View mView;
    MainActivity mActivity;
    public SongAdapter songAdapter;
//    private MusicController controller;
    private boolean paused=true, playbackPaused=false;
//    passSongList passSongList;
//    public interface passSongList{
//        void completeSongList(ArrayList<Songs> songList);
//    }
    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mActivity =(MainActivity) activity;
        }
        catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + "must implement passSongList");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.music_current_playing, container, false);
        mView = view;

        songsView = (ListView) view.findViewById(R.id.song_list);

        if (savedInstanceState == null)
        {
            songList = new ArrayList<>();
            getSongList();
            Collections.sort(songList, new Comparator<Songs>() {
                @Override
                public int compare(Songs songs, Songs t1) {
                    return songs.getTitle().compareTo(t1.getTitle());
                }
            });
        }
        else {
            songList = (ArrayList) savedInstanceState.getSerializable("SavedInstance");
        }
        songAdapter = new SongAdapter(MainActivity.getMainActivity(), songList, this );
        songsView.setAdapter(songAdapter);
//        songsView.setOnItemClickListener(this);
//        setController();
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        int songPosition = Integer.parseInt(view.getTag(position).toString());
        if (checkMusicIsPlaying()) {
            stopMusic();
        }
        Songs selectedSong = songList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Song", selectedSong);
        Log.d("demo", "position is " + position + "selected song model is " + selectedSong);
        MainActivity.getMainActivity().passMusicList(songList);
        MainActivity.getMainActivity().passSelectedSongPosition(position);
        playMusic();
        MainActivity.getMainActivity().applyFragment(SingleMusicFragment.TAG, bundle);
    }


    public void getSongList(){
//        We  can have _ID, ARTIST, TITLE, DISPLAY_NAME, DATA, ALBUM_ID, DURATION
//        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        ContentResolver contentResolver = MainActivity.getMainActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = contentResolver.query(musicUri,null,null,null,null);
        if(musicCursor != null && musicCursor.moveToFirst()){
            int titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int albumID = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int artistColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);
            int duration = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            do{
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                long thisAlbumID = musicCursor.getLong(albumID);
                String thisArtist = musicCursor.getString(artistColumn);
                String albumArtPath = "";
                int songDuration = musicCursor.getInt(duration);

                final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
                Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, thisAlbumID);
                String uri = albumArtUri.toString();
                songList.add(new Songs(thisId, thisTitle, thisArtist, uri, songDuration));
            }while(musicCursor.moveToNext());
        }
        musicCursor.close();
    }

    @Override
    public void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        paused = false;
//        MainActivity.getMainActivity().passMusicList(songList);
    }

    @Override
    public void onStop() {
//        controller.hide();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("SavedInstance", songList);
        outState.putAll(outState);
    }



    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */


//    @Override
//    public int getDuration() {
//        if(MainActivity.musicService != null && musicBound && MainActivity.musicService.isPng())
//            return MainActivity.musicService.getDur();
//        else
//            return 0;
//    }
//
//    @Override
//    public int getCurrentPosition() {
//        if(MainActivity.musicService != null && musicBound && MainActivity.musicService.isPng())
//            return MainActivity.musicService.getPosn();
//        else
//            return 0;
//    }
//
//    @Override
//    public void seekTo(int pos) {
//        MainActivity.musicService.seek(pos);
//    }
//


}
