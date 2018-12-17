package com.android60.roj5bmr111.android60;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class TagsListAdapter extends ArrayAdapter {

    private Context adapterContext;
    private int adapterResource;
    private int lastPosition = -1;

    private static class ViewHolder{
        TextView photoTags;
    }

    public TagsListAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.adapterContext = context;
        this.adapterResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get Photo information
       String tagsString = getItem(position).toString();

        // create view result for showing animation
        final View result;

        // ViewHolder object
        ViewHolder holder;

        if(convertView == null){

            LayoutInflater inflater = LayoutInflater.from(adapterContext);
            convertView = inflater.inflate(adapterResource, parent, false);

            holder = new ViewHolder();
            holder.photoTags = convertView.findViewById(R.id.tags_view);

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


        holder.photoTags.setText(tagsString);

        return convertView;
    }
}
