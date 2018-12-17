package com.android60.roj5bmr111.android60;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Tag implements Comparable<Tag>, Serializable {


    private String tagType;

    private String tagValue;

    public Tag(String tagType, String tagValue) {
        this.tagType = tagType;
        this.tagValue = tagValue;
    }
    /**
     *
     * @param givenTag the tag we wish to update
     */
    public Tag(Tag givenTag) {
        tagType = givenTag.tagType;
        tagValue = givenTag.tagValue;
    }

    /**
     * This method retrieves a tag's tagType variable.
     * @return a string of the tag's type
     */
    public String getType() {
        return this.tagType;
    }
    /**
     * This method retrieves a tag's tagValue variable.
     * @return a string of the tag's value
     */
    public String getValue() {
        return this.tagValue;
    }
    /**
     * This method retrieves a tag's tagType and tagValue variable
     * as one string.
     * @return a string in the style of "tag name = tag value
     */

    @Override
    public String toString() {
        return tagType + "=" + tagValue;
    }
    /**
     * This method compares two tag objects.
     * @return true if they match, false otherwise
     */
    @Override
    public boolean equals(Object o) {

        if(o == null || !(o instanceof Tag)) {
            return false;
        }

        Tag checkTag = (Tag)o;

        if((checkTag.tagType).equals(tagType) && (checkTag.tagValue).equals(tagValue)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * This methods compares two tags based on their tagType and tagValue.
     * @param tag The tag we wish to compare to
     * @return an int indicating whether or not they are equal
     */
    @Override
    public int compareTo(Tag tag) {
        int result = tagType.compareToIgnoreCase(tag.tagType);
        return (result != 0) ? result : tagValue.compareToIgnoreCase(tag.tagValue);
    }
}