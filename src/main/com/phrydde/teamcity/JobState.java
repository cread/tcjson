package com.phrydde.teamcity;

public class JobState {
    public final String name;
    public final String id;
    public final String status;
    public final String color;

    public JobState(String name, String id, String status) {
        this.name = name;
        this.id = id;
        this.status = status;
        this.color = ("SUCCESS".equals(status)) ? "blue" : "red";
    }
}
