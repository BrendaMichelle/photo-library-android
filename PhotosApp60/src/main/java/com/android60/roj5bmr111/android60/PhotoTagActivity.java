package com.android60.roj5bmr111.android60;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class PhotoTagActivity extends AppCompatActivity {

    public static final int RESULT_TAGS_ADDED = 8008;
    private EditText locationTagInputField;
    private EditText personTagInputField;
    private Owner androidOwner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tags_activity);

        locationTagInputField = findViewById(R.id.location_value_field);
        personTagInputField = findViewById(R.id.person_value_field);


    }

    public void confirmTagInput(View view){
        String locationTagValue = locationTagInputField.getText().toString().trim();
        String personTagValue = personTagInputField.getText().toString().trim();

        // show error when no input detected
        if(locationTagValue.equals("") && personTagValue.equals("")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("No input detected");
            builder.setMessage("At least one field must be filled to confirm");
            builder.setPositiveButton("Yes", null).create().show();
            return;
        }

        // get the owner instance
        androidOwner = Owner.getAndroidOwner();

        // get album that current photo is in
        Album albumPhotoIsIn = androidOwner.getAlbumObject(getIntent().getExtras().getString(PhotoSlideShowActivity.ALBUM_WITH_PHOTO));

        Photo currentPhoto = albumPhotoIsIn.getPhotoArrayList().get(getIntent().getExtras().getInt(PhotoSlideShowActivity.PHOTO_INDEX));

        if(!locationTagValue.equals("")){

            currentPhoto.addTag("Location", locationTagValue.trim());
        }

        if(!personTagValue.equals("")){

            currentPhoto.addTag("Person", personTagValue.trim());
        }

        setResult(RESULT_TAGS_ADDED);
        finish();
    }

    public void cancelTagInput(View view){
        setResult(RESULT_CANCELED);
        finish();
    }
}
