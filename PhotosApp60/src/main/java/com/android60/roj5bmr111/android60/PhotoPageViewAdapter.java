package com.android60.roj5bmr111.android60;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import java.util.ArrayList;

public class PhotoPageViewAdapter extends PagerAdapter{

    private ArrayList<Photo> photosToSlide;
    private Context pageViewContext;
    private LayoutInflater layoutInflater;
    private ListView tagsListView;

    public PhotoPageViewAdapter(Context pageViewContext,  ArrayList<Photo> photosToSlide){

        this.pageViewContext = pageViewContext;
        this.photosToSlide = photosToSlide;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater)pageViewContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.photo_adapter_view_pager_layout, container, false);

        // get image reference for photo display
        ImageView photoImageView = (ImageView)item_view.findViewById(R.id.photo_image_view_pager);

        // get ListView reference to list Tags
        tagsListView = (ListView)item_view.findViewById(R.id.photo_tags_listview);

        // set up on click to delete tags
        tagsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deleteTag(position);
            }
        });

        // set the adapter that helps display the tags in the LIstView
        TagsListAdapter adapter = new TagsListAdapter(pageViewContext, R.layout.center_tags, photosToSlide.get(position).getTagArrayList());
        tagsListView.setAdapter(adapter);


        Bitmap image = photosToSlide.get(position).getImage();
        photoImageView.setImageBitmap(image);
        container.addView(item_view);

        return item_view;
    }

    private void deleteTag(final int position) {
        new AlertDialog.Builder(pageViewContext)
                .setTitle("Delete Tag")
                .setMessage("Are you sure?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        photosToSlide.get(PhotoSlideShowActivity.currentViewingPhoto).getTagArrayList().remove(position);
                        TagsListAdapter adapter = new TagsListAdapter(pageViewContext, R.layout.center_tags, photosToSlide.get(PhotoSlideShowActivity.currentViewingPhoto).getTagArrayList());
                        tagsListView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }).create().show();

    }


    public ListView getCurrentListView(){
        return tagsListView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

    @Override
    public int getCount() {
        return photosToSlide.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout)object;
    }

}
