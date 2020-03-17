package com.idyllix.bol_tech.models;

import android.graphics.Bitmap;

public class SaveImageAttr {

    private String filePath;
    private String filename;
    private Bitmap.CompressFormat format;
    private int quality;

    public SaveImageAttr() {
    }

    public SaveImageAttr(String filePath, String filename, Bitmap.CompressFormat format, int quality) {
        this.filePath = filePath;
        this.filename = filename;
        this.format = format;
        this.quality = quality;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Bitmap.CompressFormat getFormat() {
        return format;
    }

    public void setFormat(Bitmap.CompressFormat format) {
        this.format = format;
    }
}
