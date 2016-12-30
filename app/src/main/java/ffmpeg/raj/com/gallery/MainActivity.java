package ffmpeg.raj.com.gallery;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

import ffmpeg.raj.com.gallery.adapters.SelectedPhotoAdapter;
import ffmpeg.raj.com.gallery.fragments.AlbumFragment;
import ffmpeg.raj.com.gallery.fragments.PhotosFragment;

public class MainActivity extends AppCompatActivity implements AlbumFragment.onAlbumSelectListener,PhotosFragment.onPhotoSelectListener {

    AlbumFragment listFragment;
    RecyclerView recyclerView;
    SelectedPhotoAdapter selectedPhotoAdapter;
    ArrayList<String> lstSelectedPhotos;
    Toolbar toolbar;
    @Override
    public void onSelect(String albumName) {
        FragmentManager fm = getFragmentManager();

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.displayList, PhotosFragment.newInstance(albumName));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
         toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Gallery");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("index",0);
        editor.commit();
        listFragment=new AlbumFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.displayList, listFragment);
        Log.e("Hii", "ok123");
        fragmentTransaction.commit();

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((Fragment) getFragmentManager().findFragmentById(R.id.displayList) instanceof PhotosFragment)
                {

                    getFragmentManager().popBackStack();
                    toolbar.setTitle("Gallery");
                }
                else
                    finish();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.rv_views);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setHasFixedSize(true);
        lstSelectedPhotos = new ArrayList<String>();
        selectedPhotoAdapter = new SelectedPhotoAdapter(this, lstSelectedPhotos, new SelectedPhotoAdapter.SelectListner() {
            @Override
            public void onFinished(String response,int position) {
                lstSelectedPhotos.remove(response);
                selectedPhotoAdapter.notifyDataSetChanged();
                if(position-1>=0)
                recyclerView.scrollToPosition(position-1);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(selectedPhotoAdapter);
    }

    public void onBackPressed()
    {
        // Catch back action and pops from backstack
        // (if you called previously to addToBackStack() in your transaction)
        if (getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
            toolbar.setTitle("Gallery");
        }
        // Default action on back pressed
        else super.onBackPressed();
    }

    @Override
    public void onPhotoSelect(String selectedPhoto) {
        if(!lstSelectedPhotos.contains(selectedPhoto)) {
            lstSelectedPhotos.add(selectedPhoto);
            selectedPhotoAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(lstSelectedPhotos.size() - 1);
        }
    }
}
