package com.myGwtApplication.client;

import java.io.Serializable;

public class Subject implements Serializable{
    private String name;
    private int[] grades;

    public Subject(String name, int[] grades) {
        this.name = name;
        this.grades = grades;
    }

    public Subject() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getGrades() {
        return grades;
    }

    public void setGrades(int[] grades) {
        this.grades = grades;
    }
}
