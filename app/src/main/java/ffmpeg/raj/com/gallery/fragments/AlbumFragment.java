package ffmpeg.raj.com.gallery.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ffmpeg.raj.com.gallery.R;
import ffmpeg.raj.com.gallery.adapters.AlbumListAdapter;
import ffmpeg.raj.com.gallery.model.Album;

/**
 * Created by root on 15/6/16.
 */
public class AlbumFragment extends Fragment {

    RecyclerView recyclerView;
    AlbumListAdapter albumListAdapter;
    ArrayList<Album> lstAlbums;
    View view;
    onAlbumSelectListener mOnAlbumSelectListener;
    Activity mActivity;

    public interface onAlbumSelectListener {
        public void onSelect(String albumName);
    }

    public AlbumFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnAlbumSelectListener = (onAlbumSelectListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.frag_gallary, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_views);
        recyclerView.setHasFixedSize(true);
        new Thread(new Runnable() {
            @Override
            public void run() {

                lstAlbums = getBucketNames();

                    mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        albumListAdapter = new AlbumListAdapter(getActivity(), lstAlbums, new AlbumListAdapter.TaskListener() {
                            @Override
                            public void onFinished(String albumName) {
                                mOnAlbumSelectListener.onSelect(albumName);
                            }
                        });
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                                LinearLayoutManager.VERTICAL, false));
                        recyclerView.setAdapter(albumListAdapter);
                        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
                        if(sharedPreferences.getInt("index",0)>0)
                        recyclerView.scrollToPosition(sharedPreferences.getInt("index",0));


                    }
                });

            }
        }).start();
        return view;
    }

    private ArrayList<Album> getBucketNames() {
        ArrayList<Album> result = new ArrayList<Album>();
        String[] projection = new String[]{
                "Distinct " + MediaStore.Images.Media.BUCKET_DISPLAY_NAME

        };

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cur = getActivity().getContentResolver().query(images,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                null        // Ordering
        );

        String bucketName;
        if (cur.moveToFirst()) {

            int bucketColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            do {
                bucketName = cur.getString(bucketColumn);
                result.add(photoCountByAlbum(bucketName));
            } while (cur.moveToNext());

        }
        return result;
    }

    private Album photoCountByAlbum(String bucketName) {
        Album album=new Album();
        try {

            album.setTitle(bucketName);

            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            String searchParams = null;
            String bucket = bucketName;

            searchParams = "bucket_display_name = \"" + bucket + "\"";


            Cursor mPhotoCursor = mActivity.getContentResolver().query(

                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.MediaColumns.DATA},

                    searchParams, null, orderBy + " DESC");

            if(null!=mPhotoCursor) {
                if (mPhotoCursor.getCount() > 0) {

                    album.setCount(mPhotoCursor.getCount());
                    if(mPhotoCursor.moveToFirst())
                    album.setImagePath(mPhotoCursor.getString(0));

                }

                mPhotoCursor.close();
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return album;

    }
}
