package com.android60.roj5bmr111.android60;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class PhotoSlideShowActivity extends AppCompatActivity {

    public static final int TAG_REQUEST_CODE = 11;
    public static final String PHOTO_INDEX = "photo Index";
    public static final String ALBUM_WITH_PHOTO = "album Photo is in";
    private ViewPager photoViewPager;
    private PhotoPageViewAdapter adapter;
    private Owner androidOwner;
    private Album currentAlbumToViewPhotos;
    public static int currentViewingPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);

        androidOwner = Owner.getAndroidOwner();
        currentAlbumToViewPhotos = androidOwner.getAlbumObject(getIntent().getExtras().getString(ShowAlbumActivity.CURRENT_ALBUM_TO_VIEW_PHOTOS));

        final int clickedPosition = getIntent().getExtras().getInt(ShowAlbumActivity.CURRENT_PHOTO_TO_VIEW);
        currentViewingPhoto = clickedPosition;

        photoViewPager = (ViewPager)findViewById(R.id.photo_view_pager);


        photoViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                saveData();
            }

            @Override
            public void onPageSelected(int position) {
                currentViewingPhoto = position;
                saveData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // used to set clicked position from album
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                photoViewPager.setCurrentItem(clickedPosition);
                saveData();
            }
        });

        adapter = new PhotoPageViewAdapter(this, currentAlbumToViewPhotos.getPhotoArrayList());
        photoViewPager.setAdapter(adapter);
    }

    @Override
    public void onPause(){
        super.onPause();
        saveData();
    }
    @Override
    public void onStop(){
        super.onStop();
        saveData();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        saveData();
    }


    public void saveData(){

        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(MainActivity.fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);


            oos.writeObject(androidOwner);
            oos.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // create menu to add Tag
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.photo_activity_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.addTagAction:
                getNewTag();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getNewTag() {

        String currentAlbumName = currentAlbumToViewPhotos.getAlbumName();

        Bundle bundle = new Bundle();
        bundle.putString(ALBUM_WITH_PHOTO, currentAlbumName);
        bundle.putInt(PHOTO_INDEX, currentViewingPhoto);
        Intent addTagIntent = new Intent(this, PhotoTagActivity.class);
        addTagIntent.putExtras(bundle);
        startActivityForResult(addTagIntent, TAG_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == PhotoTagActivity.RESULT_TAGS_ADDED){

            // used to set clicked position from album
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    photoViewPager.setCurrentItem(currentViewingPhoto);
                }
            });

            this.adapter = new PhotoPageViewAdapter(this, currentAlbumToViewPhotos.getPhotoArrayList());
            photoViewPager.setAdapter(adapter);


        }
    }
}
