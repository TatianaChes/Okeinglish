package com.example.myapplicationtest.study;

import java.io.Serializable;

public class Student implements Serializable  {

    private String name;
    private  String group;
    private  double overallProgress;
    private int continuousDay;
    public Student(String name, String group, double overallProgress, int continuousDay){
        this.name = name;
        this.group = group;
        this.overallProgress = overallProgress;
        this.continuousDay = continuousDay;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public double getOverallProgress() {
        return overallProgress;
    }

    public int getContinuousDay() {
        return continuousDay;
    }

}
