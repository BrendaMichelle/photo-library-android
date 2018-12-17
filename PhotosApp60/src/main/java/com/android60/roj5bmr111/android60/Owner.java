package com.android60.roj5bmr111.android60;

import java.io.Serializable;
import java.util.ArrayList;
import android.content.Context;
import java.io.*;


public class Owner implements Serializable{

    private static final long serialVersionUID = 5091741063678483241L;
    private ArrayList<Album> album_list;
    private static final String fileName = "userdata";


    /**
     * Private constructor which constructs the one Owner object
     * which makes it a singleton
     */
    private Owner(){

        album_list = new ArrayList<Album>();
    }

    private static Owner androidOwnerInstance = new Owner();

    public static Owner getAndroidOwner() {

        return androidOwnerInstance;
    }

    public static void setAndroidOwnerInstance(Owner setOwner){
        androidOwnerInstance = setOwner;
    }

    /**
     * This method retrieves a user's Album object from a list if it exists.
     * @param albumName This is the album for which we're looking for.
     * @return Album object if it exists, null otherwise.
     */
    public Album getAlbumObject(String albumName) {
        int index = this.searchForAlbum(albumName);
        if(index >= 0) {
            return album_list.get(index);
        }

        return null;
    }

    /**
     * This method returns the array list of albums
     * @return an array list containing the albums
     *
     */
    public ArrayList<Album> getAlbumList(){

        return album_list;
    }


    /**
     * This method moves a photo from one album to another.
     * @param pic The photo to be moved.
     * @param fromAlbum The album from which the photo is being removed.
     * @param toAlbum The album to which to move the photo.
     *
     */
    public void movePhotoTo(Photo pic, Album fromAlbum, Album toAlbum) {
        if (toAlbum.addPhoto(pic)) {
            fromAlbum.deletePhoto(pic.fileName);
        }
    }
    /**
     * This method copies a photo from one album to another.
     * @param pic The photo we wish to copy
     * @param toAlbum the destination of the photo
     *
     */
    public void copyPhotoTo(Photo pic, Album toAlbum) {
        if(toAlbum.getPhotoLocation(pic.fileName) != -1) {
            return;
        }
        else{
            Photo pic_copy = new Photo(pic.fileName, pic.caption, pic.path, pic.stringUri, pic.image);
            toAlbum.photos_arraylist.add(pic_copy);
        }
    }
    /**
     * This method searches for the album within the album list.
     * @param albumName the album we are searching for.
     * @return index of the album in the list if it exists. -1 otherwise.
     */
    public int searchForAlbum(String albumName) {
        for(int i = 0; i < album_list.size(); i++ ) {
            if(album_list.get(i).albumName.equalsIgnoreCase(albumName)) {//it exists
                return i;
            }
        }
        return -1; //doesn't exist
    }

    /**
     * This method creates a new album if the name is unique and
     * adds it to the album lists.
     * @param albumName This is the name of the album to be added.
     * @return returns the newly created Album
     */
    public Album createAlbum(String albumName){
        if(searchForAlbum(albumName) == -1) {
            Album newalbum = new Album(albumName);
            album_list.add(newalbum);
            return newalbum;
        }
        else {
            return null;
        }

    }

    /**
     * This method removes an album from a user's album list if it exists.
     * @param albumName This is the album name of the album the user wishes
     * to discard.
     * @return true if successful, false otherwise
     */
    public boolean deleteAlbum(String albumName) {
        int index = this.searchForAlbum(albumName);
        if(index >= 0 ) {
            this.album_list.remove(index);
            return true;
        }
        else {
            return false;
        }
    }

}







