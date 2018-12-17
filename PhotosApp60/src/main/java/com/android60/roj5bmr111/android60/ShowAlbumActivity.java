package com.android60.roj5bmr111.android60;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import android.content.Context;


public class ShowAlbumActivity extends AppCompatActivity {

    private static final int IMAGE_GALLERY_REQUEST = 420;
    private static final String TAG = "ShowAlbumActivity";
    public static final int CONTEXT_MENU_DELETE_PHOTO = 0;
    public static final int CONTEXT_MENU_MOVE_PHOTO = 1;
    public static final String CURRENT_ALBUM_TO_VIEW_PHOTOS = "current Photos to view";
    public static final int SHOW_PHOTOS_REQUEST = 1234;
    public static final String CURRENT_PHOTO_TO_VIEW = "Clicked on image";
    private Album currentAlbumView;
    private Owner androidOwner;
    private MenuItem item;
    private ListView albumView;
    private PhotoListAdapter adapter;
    private int indexSkipped;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_activity);
        Log.d(TAG, "OnCreate: Album View.");

        // reference the album List View from album view layout
        albumView = findViewById(R.id.album_view);
        // get android singleton object
        androidOwner = Owner.getAndroidOwner();

        // get current album object that user opened
        currentAlbumView = androidOwner.getAlbumObject(getIntent().getExtras().getString(MainActivity.CURRENT_ALBUM_KEY));

        this.adapter = new PhotoListAdapter(this, R.layout.album_adapter_view_layout, currentAlbumView.getPhotoArrayList());

        albumView.setAdapter(adapter);

        albumView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPhotoPager(position);
            }
        });

        registerForContextMenu(albumView);
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


    private void showPhotoPager(int clickedPositon) {

        String currentAlbumName = currentAlbumView.getAlbumName();

        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_ALBUM_TO_VIEW_PHOTOS, currentAlbumName);
        bundle.putInt(CURRENT_PHOTO_TO_VIEW, clickedPositon);
        Intent photoViewIntent = new Intent(this, PhotoSlideShowActivity.class);
        photoViewIntent.putExtras(bundle);
        startActivityForResult(photoViewIntent, SHOW_PHOTOS_REQUEST);
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    // creates the sub menu when a user long clicks on an item
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;

        if(v.getId() == R.id.album_view){
            menu.setHeaderTitle(currentAlbumView.getPhotoArrayList().get(info.position).fileName);
            menu.add(menu.NONE, CONTEXT_MENU_DELETE_PHOTO, 0, R.string.delete_photo);
            menu.add(menu.NONE, CONTEXT_MENU_MOVE_PHOTO, 1, R.string.move_photo);
        }
    }

    // decides what to do when user picks one of the sub menus
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case CONTEXT_MENU_DELETE_PHOTO:
                this.item = item;
                deletePhoto();
                return true;
            case CONTEXT_MENU_MOVE_PHOTO:
                this.item = item;
                movePhoto();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deletePhoto() {
        deletePhotoDialogue();
    }

    private void movePhoto() {
        movePhotoDialogue();
    }

    private void deletePhotoDialogue() {

        Toast.makeText(this, "Delete photo Dialogue", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Delete Photo");
        builder.setMessage("Are you sure?");
        builder.setNegativeButton("No", null);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

                    Album albumToUse = androidOwner.getAlbumObject(currentAlbumView.getAlbumName());

                    albumToUse.deletePhoto(albumToUse.getPhotoArrayList().get(info.position).getFileName());

                    adapter.notifyDataSetChanged();
                }
            }).create().show();
    }

    private void movePhotoDialogue() {

        Toast.makeText(this, "Move photo dialogue", Toast.LENGTH_SHORT).show();

        if (androidOwner.getAlbumList().size() == 1){
            Toast.makeText(this, "Only one album exists", Toast.LENGTH_SHORT).show();
            return;
        }

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose album to move to");

        // add a list
        String[] albumList = new String[androidOwner.getAlbumList().size() - 1];

        indexSkipped = 0;

        for(int albumListIndex = 0, realAlbumListIndex = 0 ; realAlbumListIndex < androidOwner.getAlbumList().size() ; albumListIndex++, realAlbumListIndex++){

            if(androidOwner.getAlbumList().get(realAlbumListIndex).equals(currentAlbumView)){
                indexSkipped = albumListIndex;
                albumListIndex--;
            }
            else{
                albumList[albumListIndex] = androidOwner.getAlbumList().get(realAlbumListIndex).getAlbumName();
            }
        }

        builder.setItems(albumList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                Photo photoToMove = currentAlbumView.getPhotoArrayList().get(info.position);

                if(indexSkipped - which > 0){

                    androidOwner.movePhotoTo(photoToMove, currentAlbumView, androidOwner.getAlbumList().get(which));

                    Toast.makeText(ShowAlbumActivity.this, "Album is: " + androidOwner.getAlbumList().get(which), Toast.LENGTH_SHORT).show();
                }
                else{

                    androidOwner.movePhotoTo(photoToMove, currentAlbumView, androidOwner.getAlbumList().get(which + 1));

                    Toast.makeText(ShowAlbumActivity.this, "Album is: " + androidOwner.getAlbumList().get(which + 1), Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    // create add photo menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.album_activity_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // add functionality to add photo button menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.addPhotoAction:
                getNewPhoto();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // use the image gallery to find a photo
    private void getNewPhoto() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_GALLERY_REQUEST);
    }

    // add a new photo given by user
    private void addNewPhoto(Uri imageUri) {


        // declare a stream to read the image data from the SD card
        InputStream inputStream;

        // getting an input stream based on the Uri of the image
        try {
            inputStream = getContentResolver().openInputStream(imageUri);

            // get a bitmap from the stream
            Bitmap image = BitmapFactory.decodeStream(inputStream);

            File photoFile = new File(imageUri.getPath());
            String path = photoFile.getAbsolutePath();
            String fileName = getPathFromFile(imageUri);
            String caption = fileName;
            String stringUri = imageUri.toString();
            currentAlbumView.addPhoto(fileName, caption, path, stringUri, image);
            adapter.notifyDataSetChanged();

        } catch (FileNotFoundException e) {
            // show a message to user to indicate image is unavailable
            Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
        }
    }

    // method used to extract the exact file name from given uri

    private String getPathFromFile(Uri imageUri){

        String filename = "File not found";

        String[] column = {MediaStore.MediaColumns.DISPLAY_NAME};

        ContentResolver contentResolver = getApplicationContext().getContentResolver();

        Cursor cursor = contentResolver.query(imageUri, column, null, null, null);

        if(cursor != null){

            try{
                if(cursor.moveToFirst()) {
                    filename = cursor.getString(0);
                }
            }
            catch (Exception e) {

            }
        }

        return filename;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            // processed successfully at this point
            if(requestCode == IMAGE_GALLERY_REQUEST){
                // came back from image gallery

                // the address of the image on the SD card
                Uri imageUri = data.getData();

                addNewPhoto(imageUri);
            }
        }
    }
}