package com.android60.roj5bmr111.android60;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchPhotosActivity extends AppCompatActivity {

    public static final String TAG_TYPE_LOCATION = "Location";
    public static final String TAG_TYPE_PERSON = "Person";
    private Owner androidOwner;
    private ListView searchPhotoListView;
    private SearchListAdapter adapter;
    private Album searchResultAlbum = new Album("PhotoSearchResult");
    private EditText locationField;
    private EditText personField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_photos_layout);

        androidOwner = Owner.getAndroidOwner();

        searchPhotoListView = findViewById(R.id.photoSearchList);
        locationField = findViewById(R.id.search_location_field);
        personField = findViewById(R.id.search_person_field);

        this.adapter = new SearchListAdapter(this, R.layout.search_adapter_view_layout, searchResultAlbum.getPhotoArrayList());

        searchPhotoListView.setAdapter(adapter);
    }

    // search photos with both tags
    public void andButtonClicked(View view) {
        searchResultAlbum.getPhotoArrayList().clear();

        String parseLocationField = locationField.getText().toString().trim();
        String parsePersonField = personField.getText().toString().trim();

        if(parseLocationField.equals("") && parsePersonField.equals("")){
            noInput();
            adapter.notifyDataSetChanged();
            return;
        }


        for(Album searchAlbum : androidOwner.getAlbumList()){
            for (Photo searchPhoto : searchAlbum.getPhotoArrayList()){
                if(searchPhoto.compareTags(TAG_TYPE_LOCATION, parseLocationField) && searchPhoto.compareTags(TAG_TYPE_PERSON, parsePersonField)){
                    String captionTags = searchPhoto.getTagValues();
                    searchPhoto.setCaption(captionTags);
                    searchResultAlbum.addPhoto(searchPhoto);
                }
            }
        }

        adapter.notifyDataSetChanged();

        if(searchResultAlbum.getPhotoArrayList().size() == 0){
            Toast.makeText(this, "No search result found", Toast.LENGTH_SHORT).show();
        }
    }


    // search photos with either tags
    public void orButtonClicked(View view) {
        searchResultAlbum.getPhotoArrayList().clear();

        String parseLocationField = locationField.getText().toString().trim();
        String parsePersonField = personField.getText().toString().trim();

        if(parseLocationField.equals("") && parsePersonField.equals("")){
            noInput();
            adapter.notifyDataSetChanged();
            return;
        }

        for(Album searchAlbum : androidOwner.getAlbumList()){
            for (Photo searchPhoto : searchAlbum.getPhotoArrayList()){
                if(searchPhoto.compareTags(TAG_TYPE_LOCATION, parseLocationField) || searchPhoto.compareTags(TAG_TYPE_PERSON, parsePersonField)){
                    String captionTags = searchPhoto.getTagValues();
                    searchPhoto.setCaption(captionTags);
                    searchResultAlbum.addPhoto(searchPhoto);
                }
            }
        }

        adapter.notifyDataSetChanged();

        if(searchResultAlbum.getPhotoArrayList().size() == 0){
            Toast.makeText(this, "No search result found", Toast.LENGTH_SHORT).show();
        }

    }

    private void noInput() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("No input detected");
        builder.setMessage("At least one field must be filled to confirm.");
        builder.setPositiveButton("Got it", null).create().show();
    }

    public void invalidInput(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Invalid Input");
        builder.setMessage("No symbols or beginning with empty spaces allowed.");
        builder.setPositiveButton("Got it", null).create().show();
        return;
    }
}
