package com.android60.roj5bmr111.android60;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

public class SearchListAdapter extends ArrayAdapter<Photo> {

    private static final String TAG = "SearchListAdapter";
    private int adapterResource;
    private int lastPosition = -1;
    private Context adapterContext;

    private static class ViewHolder{
        TextView photoCaption;
        ImageView photoView;
    }

    public SearchListAdapter(@NonNull Context context, int resource, @NonNull List<Photo> objects) {
        super(context, resource, objects);
        this.adapterContext = context;
        this.adapterResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // get Photo information
        String caption = Objects.requireNonNull(getItem(position)).getCaption();
        Bitmap image = Objects.requireNonNull(getItem(position)).getImage();

        // create view result for showing animation
        final View result;

        // ViewHolder object
        ViewHolder holder;

        if(convertView == null){

            LayoutInflater inflater = LayoutInflater.from(adapterContext);
            convertView = inflater.inflate(adapterResource, parent, false);

            holder = new ViewHolder();
            holder.photoCaption = convertView.findViewById(R.id.photo_title);
            holder.photoView = convertView.findViewById(R.id.photo_image_view);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(adapterContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;


        holder.photoCaption.setText(caption);
        holder.photoView.setImageBitmap(image);

        return convertView;
    }
}
