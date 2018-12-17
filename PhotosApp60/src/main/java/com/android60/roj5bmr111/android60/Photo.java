package com.android60.roj5bmr111.android60;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import android.graphics.BitmapFactory;
import java.io.*;


public class Photo implements Serializable{

    private static final long serialVersionUID = 7883731826149723627L;
    protected String fileName;
    protected String caption;
    protected ArrayList<Tag> tags_arraylist;
    protected String path;
    protected String stringUri;
    protected transient Bitmap image;
    protected byte [] imageByteArr;
    protected int width;
    protected int height;

    public Photo(String fileName, String caption, String path, String stringUri, Bitmap image) {

        this.fileName = fileName;
        this.caption = caption;
        this.path = path;
        this.tags_arraylist = new ArrayList<Tag>();
        this.stringUri = stringUri;
        this.image = image;

    }



    public Bitmap getImage(){
        return image;
    }

    public void setImage(Bitmap image){
        this.image = image;
    }

    /**
     * This method retrieve's a photo's array list of tags.
     * @return tags_arraylist the ArrayList of tags of the photo
     */
    public ArrayList<Tag> getTagArrayList(){
        return tags_arraylist;
    }

    /**
     * This method retrieves a photo's string version of Uri
     * @return stringUri String
     */
    public String getStringUri(){
        return stringUri;
    }

    /**
     * This method retrieves a photo's path variable.
     * @return path String
     */
    public String getPath() {
        return path;
    }

    /**
     * This method changes a photo's caption variable.
     * @param newCaption the new caption of the photo
     */
    public void setCaption(String newCaption) {
        caption = newCaption;
    }
    /**
     * This method retrieves a photo's caption variable.
     * @return caption
     */
    public String getCaption() {
        return caption;
    }
    /**
     * This method retrieves a photo's fileName variable.
     * @return fileName
     */
    public String getFileName() {

        return fileName;
    }

    /**
     * This method allows a caption to be updated or edited.
     * @param caption The new caption for the photo
     */
    public void addCaption( String caption) {
        this.caption = caption;
    }

    /**
     * This caption allows a tag to be added to the
     * picture's tag list.
     * @param type The type of tag
     * @param value The actual tag itself
     *
     */
    public void addTag(String type, String value) {
        this.tags_arraylist.add(new Tag(type, value));
    }

    public boolean compareTags(String givenType, String givenValue){

        if(givenValue.equals("")){
            return false;
        }

        for(int i = 0 ; i < tags_arraylist.size() ; i++){
            if(tags_arraylist.get(i).getType().equals(givenType)) {
                if (tags_arraylist.get(i).getValue().contains(givenValue)) {
                    if (tags_arraylist.get(i).getValue().charAt(0) == givenValue.charAt(0)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public String getTagValues(){

        String returnTagValues = "";

        for(int i = 0 ; i < tags_arraylist.size() ; i++){
            if(i + 1 != tags_arraylist.size()){
                returnTagValues += tags_arraylist.get(i).getValue() + ", ";
            }
            else{
                returnTagValues += tags_arraylist.get(i).getValue();
            }
        }

        return returnTagValues;
    }

    /**
     * @param type The type of tag
     * @param value The actual tag itself
     * @return The index of the location of a specified tag represented as a positive
     * integer.  If a tag cannot be found, then it returns -1.
     */
    public int getTagLocation(String type, String value) {
        for (int i = 0; i < this.tags_arraylist.size(); i++) {
            if (tags_arraylist.get(i).equals(new Tag(type, value))) {
                return i;
            }
        }
        return -1;
    }
    /**
     * This method deletes a specified tag from the picture's tag list.
     * @param type The type of tag
     * @param value The actual tag itself
     * @return true if successful, false otherwise
     */
    public boolean deleteTag(String type, String value) {
        int index = this.getTagLocation(type, value);
        if (index != -1) {
            this.tags_arraylist.remove(index);
            return true;
        }
        else {
            return false;
        }
    }

    /*
     *
     * This method renames the photo.
     * @param name The name the user wants to name the photo.
     * @return void
     */
    public void renamePhoto(String name) {
        this.fileName = name;
    }
    /**
     * This method compares two photo objects.
     * @return true if they match, false otherwise
     */
    @Override
    public boolean equals(Object o) {

        if(o == null || !(o instanceof Photo)) {
            return false;
        }

        Photo checkPhoto = (Photo)o;

        if(!(checkPhoto.fileName).equals(fileName)) {
            return false;
        }

        return true;
    }
}