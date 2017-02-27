package com.youshon.routefr;

import java.io.Serializable;

/**
 * Created by hansj on 2017/2/27.
 */

public class Student implements Serializable{
    private String name;

    public Student(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
