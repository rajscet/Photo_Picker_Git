package ffmpeg.raj.com.gallery.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ffmpeg.raj.com.gallery.R;
import ffmpeg.raj.com.gallery.model.Album;

/**
 * Created by root on 1/6/16.
 */

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.AppListAdapterHolder> {
    private ArrayList<Album> mItems;
    private Context mContext;
    private TaskListener taskListener;

    public interface TaskListener {
        public void onFinished(String response);
    }

    public AlbumListAdapter(Context context, ArrayList<Album> items, TaskListener listener) {
        mItems = items;
        mContext = context;
        this.taskListener=listener;

    }

    @Override
    public AppListAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_albums, viewGroup, false);
        AppListAdapterHolder viewHolder = new AppListAdapterHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AppListAdapterHolder shoppingListViewHolder, int position) {
        shoppingListViewHolder.bindShoppingList(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class AppListAdapterHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public ImageView imgIcon;


        public AppListAdapterHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);

            imgIcon = (ImageView) itemView.findViewById(R.id.img_thumb);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(taskListener != null) {
                        // And if it is we call the callback function on it.
                        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mContext);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putInt("index",getAdapterPosition());
                        editor.commit();
                        taskListener.onFinished(mItems.get(getAdapterPosition()).getTitle());
                    }
                }
            });

        }

        public void bindShoppingList(Album item) {
            try {
                tvTitle.setText(item.getTitle()+" ("+String.valueOf(item.getCount())+")");

                Glide.with(mContext).load(item.getImagePath()).centerCrop().into(imgIcon);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}