/*
    Name:   PhotoStorage.java
    Author: Sean Smith
    Date:   28 December 2013

    This provides various 'getter' and 'setter' methods required for the retrieval / initialisation of photos.
*/

package com.project.paigepro;

public class PhotoStorage {

    public int storageID;
    public String storageName;
    public byte[] storageImage;

    public PhotoStorage() {}

    public PhotoStorage(int newStorageID) {

        this.storageID = newStorageID;
    }

    public PhotoStorage(String newStorageName, byte[] newStorageImage) {

        this.storageName = newStorageName;
        this.storageImage = newStorageImage;
    }

    public int getPhotoID() { return this.storageID; }
    public byte[] getPhotoImage() {
        return this.storageImage;
    }

    public void setPhotoID(int newStorageID) {
        this.storageID = newStorageID;
    }
    public void setPhotoName(String newStorageName) { this.storageName = newStorageName; }
    public void setPhotoImage(byte[] newStorageImage) {
        this.storageImage = newStorageImage;
    }
}