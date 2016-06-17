package ffmpeg.raj.com.gallery.fragments;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ffmpeg.raj.com.gallery.ItemOffsetDecoration;
import ffmpeg.raj.com.gallery.R;
import ffmpeg.raj.com.gallery.adapters.PhotoGridAdapter;
import ffmpeg.raj.com.gallery.model.Photo;

/**
 * Created by root on 15/6/16.
 */
public class PhotosFragment extends Fragment {

    RecyclerView recyclerView;
    PhotoGridAdapter albumListAdapter;
    ArrayList<Photo> lstPhotos;
    View view;
    String mAlbums="";

    onPhotoSelectListener mOnPhotoSelectListener;

    public interface onPhotoSelectListener {
        public void onPhotoSelect(String selectedPhoto);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnPhotoSelectListener = (onPhotoSelectListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }
    
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            mOnPhotoSelectListener = (onPhotoSelectListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }
    public PhotosFragment() {

    }

    public static final PhotosFragment newInstance(String album)
    {
        PhotosFragment f = new PhotosFragment();
        Bundle bdl = new Bundle(2);
        bdl.putString("album", album);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlbums = getArguments().getString("album");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.frag_gallary, container, false);
        ((Toolbar)getActivity().findViewById(R.id.toolbar)).setTitle(mAlbums);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_views);
           ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setHasFixedSize(true);
        new Thread(new Runnable() {
            @Override
            public void run() {

                lstPhotos = getCameraImages();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        albumListAdapter = new PhotoGridAdapter(getActivity(), lstPhotos, new PhotoGridAdapter.TaskListener() {
                            @Override
                            public void onFinished(String response) {
                                mOnPhotoSelectListener.onPhotoSelect(response);

                            }
                        });
                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));
                        recyclerView.setAdapter(albumListAdapter);
                    }
                });

            }
        }).start();
        return view;
    }

    public ArrayList<Photo> getCameraImages() {


        ArrayList<Photo> result = new ArrayList<Photo>();

        String[] projection = { MediaStore.MediaColumns.DATA};
        String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?";
        String[] selectionArgs = {mAlbums};
        final Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                MediaStore.Images.Media.DATE_TAKEN + " desc");

        if (cursor.moveToFirst()) {
            final int dataColumn = cursor.getColumnIndexOrThrow( MediaStore.MediaColumns.DATA);
            do {

                result.add(new Photo(cursor.getString(dataColumn)));

            } while (cursor.moveToNext());
        }
        cursor.close();

        return result;
    }
}
