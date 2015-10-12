package com.cest.smartclass_student;

/**
 * Created by pc on 2015-09-27.
 */
public class MenuItem {

    int id;
    int img;
    String name;

    public MenuItem(int id, int img, String name){
        this.id = id;
        this.img = img;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public int getImg() {
        return img;
    }

    public String getName() {
        return name;
    }
}
