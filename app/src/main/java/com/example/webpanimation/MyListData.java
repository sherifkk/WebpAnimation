package com.example.webpanimation;

class MyListData {
    private String description;
    private boolean imgId;
    public MyListData(String description, boolean imgId) {
        this.description = description;
        this.imgId = imgId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getImgId() {
        return imgId;
    }
    public void setImgId(boolean imgId) {
        this.imgId = imgId;
    }
}
