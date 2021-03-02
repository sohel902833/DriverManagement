package com.sohel.drivermanagement.Admin.DataModuler;

public class Category {
    String categoryName;
    String categoryId;
    String image;
public Category(){}
    public Category(String categoryName, String categoryId, String image) {
        this.categoryName = categoryName;
        this.categoryId = categoryId;
        this.image = image;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getImage() {
        return image;
    }
}
