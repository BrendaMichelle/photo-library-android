package com.android60.roj5bmr111.android60;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

//@SuppressWarnings("serial")
public class Album implements Serializable{


    private static final long serialVersionUID = 2264530268255810505L;
    protected ArrayList<Photo> photos_arraylist;
    protected String albumName;
    protected String startDate;
    protected String endDate;
    protected Photo albumThumbnail;

    public Album(String name) {
        this.albumName = name;
        this.photos_arraylist = new ArrayList<Photo>();

    }

    /**
     * This method changes an album's albumName field.
     * @param newName the name to which a certain album
     * should be changed.
     */
    public void renameAlbumTo(String newName) {
        this.albumName = newName;
    }

    /**
     * this method allows us to retrieve an album's albumThumbnail variable.
     * @return a Photo object
     */
    public Photo getAlbumThumbnail() {
        return albumThumbnail;
    }
    /**
     * This method allows us to set an album's albumThumbnail variable,
     * which is a photo.
     * @param newThumbnail the thumbnail we wish to set to the album.
     */
    public void setAlbumThumbnail(Photo newThumbnail) {
        albumThumbnail = newThumbnail;
    }


    /**
     * This method returns the name of the album.
     * @return name of the album.
     */
    public String getAlbumName() {
        return albumName;
    }
    /**
     * @param name The name of the photo we're searching for
     * @return The index of the location of a specified photo represented as a positive
     * integer.  If a photo cannot be found, then it returns -1.
     */
    public int getPhotoLocation(String name) {
        for (int i = 0; i < this.photos_arraylist.size(); i++) {
            if (photos_arraylist.get(i).fileName.compareTo(name) == 0) {
                return i;
            }
        }
        return -1;
    }
    /**
     * This method adds an already existing photo to an album's lists.
     * @param existingPhoto the photo object we wish to add.
     * @return true if the add was successful, false otherwise
     */
    public boolean addPhoto(Photo existingPhoto) {
        String photosName = existingPhoto.getFileName();

        if(getPhotoLocation(photosName) != -1) {
            return false;
        }

        photos_arraylist.add(existingPhoto);
        return true;
    }

    /*
     * This method creates a new photo object and adds it to an album.
     * @param photo name This is what the photo is to be called.
     * @param caption This is the photo's optional caption.
     * @return true is successful, false if otherwise
     */
    public boolean addPhoto(String photoName, String caption, String path, String stringUri, Bitmap image) {
        if(this.getPhotoLocation(photoName) != -1) {
            return false;
        }

        Photo pic = new Photo(photoName, caption, path, stringUri, image);


        this.photos_arraylist.add(pic);
        return true;
    }
    /**
     * This method removes a photo from a specified album.
     * @param name  This is the name of the photo we want to remove.
     * @return true if successful, false otherwise
     */
    public boolean deletePhoto(String name) {
        int index = this.getPhotoLocation(name);
        if(index == -1) {
            return false;
        }
        else {
            this.photos_arraylist.remove(index);
            return true;
        }
    }
    /**
     * This method is used for testing.  It prints out the photo names of all
     * the photos in a given album.
     */
    public void printPhotoFileNames() {
        for(int i = 0; i < this.photos_arraylist.size(); i++) {
            System.out.println(this.photos_arraylist.get(i).fileName);
        }
    }
    /**
     * This method retrieves an albums photo array list.
     * @return this.photos_Arraylist which is an ArrayList of photos
     */
    public ArrayList<Photo> getPhotoArrayList(){
        return this.photos_arraylist;
    }

    @Override
    public String toString() {
        return albumName;
    }
}