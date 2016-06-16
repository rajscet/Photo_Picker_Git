package ffmpeg.raj.com.gallery.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ffmpeg.raj.com.gallery.R;

/**
 * Created by root on 1/6/16.
 */

public class SelectedPhotoAdapter extends RecyclerView.Adapter<SelectedPhotoAdapter.AppListAdapterHolder> {
    private ArrayList<String> mItems;
    private Context mContext;
    private SelectListner onSelectListner;

    public interface SelectListner {
        public void onFinished(String response, int position);
    }

    public SelectedPhotoAdapter(Context context, ArrayList<String> items, SelectListner listener) {
        mItems = items;
        mContext = context;
        this.onSelectListner =listener;

    }

    @Override
    public AppListAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo_select, viewGroup, false);
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

        public ImageView imgIcon,imgClose;


        public AppListAdapterHolder(View itemView) {
            super(itemView);

            imgIcon = (ImageView) itemView.findViewById(R.id.img_photo);
            imgClose= (ImageView) itemView.findViewById(R.id.img_close);

            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onSelectListner != null) {
                        // And if it is we call the callback function on it.
                        onSelectListner.onFinished(mItems.get(getAdapterPosition()),getAdapterPosition());
                    }
                }
            });

        }
        public void bindShoppingList(String item) {
            try {

                Glide.with(mContext).load(item).centerCrop().into(imgIcon);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}