package io.cread.teamcity;

public class JobState {
    public final String name;
    public final String id;
    public final String status;
    public final String responsible;
    public final String project;
    public final long buildDuration;
    public final long secondsSinceFinished;
    public final String color;

    public JobState(String name, String id, String status, String responsible, String project, long buildDuration, long secondsSinceFinished) {
        this.name = name;
        this.id = id;
        this.status = status;
        this.buildDuration = buildDuration;
        this.responsible = responsible;
        this.project = project;
        this.secondsSinceFinished = secondsSinceFinished;
        this.color = ("SUCCESS".equals(status)) ? "blue" : "red";
    }
}
