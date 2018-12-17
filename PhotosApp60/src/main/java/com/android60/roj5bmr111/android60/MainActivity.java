package com.android60.roj5bmr111.android60;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.io.FileOutputStream;
import java.io.*;

public class MainActivity extends AppCompatActivity {

    public static final int SHOW_ALBUM_REQUEST = 69;
    public static final int SEARCH_PHOTOS_REQUEST = 1337;
    private ArrayAdapter<Album> adapter;
    private Owner androidOwner;
    private MenuItem item;
    private ListView mainActivityView;
    public static final String CURRENT_ALBUM_KEY = "current album";
    static final String fileName = "userdata.ser";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        androidOwner = retrieveData(this);

        if(androidOwner == null) {
            androidOwner = Owner.getAndroidOwner();
        }
        else{
            Owner.setAndroidOwnerInstance(androidOwner);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityView = findViewById(R.id.main_activity_view);

        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, androidOwner.getAlbumList());

        // logic that listens when an album is clicked to open
        mainActivityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showAlbum(position);
            }
        });

        mainActivityView.setAdapter(adapter);
        registerForContextMenu(mainActivityView);

        // load from memory the photos if any
        // but maybe in a different method?
        // use androidOwner's album list to populate it

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
            FileOutputStream fos = getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);


            oos.writeObject(androidOwner);
            oos.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public Owner retrieveData(Context context){

        Owner o;

        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            o = (Owner) ois.readObject();

            for(Album a: o.getAlbumList()) {
                for (Photo p : a.photos_arraylist) {
                    setImageBitMap(p);
                }
            }

            fis.close();
            ois.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return o;
    }


    public void setImageBitMap(Photo photoToConvert) {

        Uri imageUri = Uri.parse(photoToConvert.getStringUri());

        // declare a stream to read the image data from the SD card
        InputStream inputStream;

        // getting an input stream based on the Uri of the image
        try {
            inputStream = getContentResolver().openInputStream(imageUri);

            // get a bitmap from the stream
            Bitmap image = BitmapFactory.decodeStream(inputStream);

            photoToConvert.setImage(image);

        } catch (FileNotFoundException e) {
            // show a message to user to indicate image is unavailable
            Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
        }
    }



    private void showAlbum(int position) {

        String albumNameToView = androidOwner.getAlbumList().get(position).getAlbumName();

        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_ALBUM_KEY, albumNameToView);
        Intent viewAlbumIntent = new Intent(this, ShowAlbumActivity.class);
        viewAlbumIntent.putExtras(bundle);
        startActivityForResult(viewAlbumIntent, SHOW_ALBUM_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            // processed successfully at this point
            if(requestCode == SHOW_ALBUM_REQUEST) {
                // Album view was good, update android object
                this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, androidOwner.getAlbumList());
                mainActivityView.setAdapter(adapter);
            }
        }
    }

    // create the subMenu for long click when activity is called
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;

        if(v.getId() == R.id.main_activity_view){

            menu.setHeaderTitle(androidOwner.getAlbumList().get(info.position).getAlbumName());
            String[] subMenuItems = getResources().getStringArray(R.array.albumSubMenu);

            for(int subMenuIndex = 0 ; subMenuIndex < subMenuItems.length ; subMenuIndex++){
                menu.add(Menu.NONE, subMenuIndex, subMenuIndex, subMenuItems[subMenuIndex]);
            }
        }
    }

    // listens for long click on listview album object to either rename or delete
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case 0:
                this.item = item;
                renameAlbum();
                return true;
            case 1:
                this.item = item;
                deleteAlbum();
                return true;
            case 2:
                this.item = item;
                search();
            default:
                return super.onContextItemSelected(item);
        }
    }

    // call dialogue method to delete album
    private void deleteAlbum() {

        deleteAlbumDialogue();
        saveData();


    }

    // call dialogue method to take input
    private void renameAlbum() {

        renameAlbumDialogue();
        saveData();


    }
    //@drawable/ic_search_black_24dp
    private void search(){
        searchForPhotosDialogue();

    }

    public void searchForPhotosDialogue(){
        System.out.println("We are about to search for photos!");
    }



    // opens dialogue to be granted permission to delete specified album
    public void deleteAlbumDialogue(){

        new AlertDialog.Builder(this)
                .setTitle("Delete Album")
                .setMessage("Are you sure?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

                        androidOwner.deleteAlbum(androidOwner.getAlbumList().get(info.position).getAlbumName());

                        adapter.notifyDataSetChanged();
                        saveData();
                    }
                }).create().show();
    }


    // creates dialogue to take new album name input
    private void renameAlbumDialogue() {

        final EditText inputText = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Rename Album")
                .setMessage("Enter new Album name")
                .setView(inputText)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String newName = inputText.getText().toString().trim();

                        if(newName.equals("") || newName.charAt(0) == ' '){
                            Toast.makeText(MainActivity.this, "Improper format", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

                        if(androidOwner.searchForAlbum(newName) != -1){
                            Toast.makeText(MainActivity.this, "Duplicate name found", Toast.LENGTH_SHORT).show();
                        }
                        else{

                            androidOwner.getAlbumList().get(info.position).renameAlbumTo(newName);
                            adapter.notifyDataSetChanged();
                        }
                    }

                }).create().show();
    }


    // creates the menu to add new albums
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_options, menu);
        return super.onCreateOptionsMenu(menu);
    }


    // Catches the menu option clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addAlbumAction:
                addNewAlbum();
                return true;
            case R.id.searchPhotosAction:
                searchForPhotos();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void searchForPhotos() {
        if(androidOwner == null || androidOwner.getAlbumList().size() == 0){
            cannotSearchDialogue();
            return;
        }

        Intent searchPhotosIntent = new Intent(this, SearchPhotosActivity.class);
        startActivityForResult(searchPhotosIntent, SEARCH_PHOTOS_REQUEST);
    }

    private void cannotSearchDialogue() {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("No Album available to search on.")
                .setPositiveButton("Gotcha", null)
                .create()
                .show();
    }


    // call method to make new album to the listView
    private void addNewAlbum() {

        addNewAlbumDialogue();
    }

    // Pop dialogue to get input from user to make new album
    private void addNewAlbumDialogue() {

        final EditText inputText = new EditText(this);
        Toast.makeText(this, "New album", Toast.LENGTH_LONG).show();

        new AlertDialog.Builder(this)
                .setTitle("New Album")
                .setMessage("Enter new Album name")
                .setView(inputText)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String newAlbumName = inputText.getText().toString().trim();

                        if(newAlbumName.equals("") || newAlbumName.charAt(0) == ' '){
                            incorrectFormat();
                        }
                        else{

                            if(androidOwner.createAlbum(newAlbumName) == null){
                                Toast.makeText(MainActivity.this, "Duplicate album name found", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                saveData();
                                adapter.notifyDataSetChanged();
                            }

                        }
                    }

                    private void incorrectFormat() {
                        Toast.makeText(MainActivity.this, "Improper format", Toast.LENGTH_LONG).show();
                    }

                }).create().show();
    }
}