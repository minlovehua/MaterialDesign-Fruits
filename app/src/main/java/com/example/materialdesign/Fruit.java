package com.example.materialdesign;

/**
 * Created by 媚敏 on 2017/6/14.
 */


//13.定义一个实体类
public class Fruit {

    private String name;
    private int imageId;

    public Fruit(String name,int imageId){//构造方法
        this.name=name;
        this.imageId=imageId;
    }

    public String getName(){
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
