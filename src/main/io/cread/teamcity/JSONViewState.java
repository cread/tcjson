package io.cread.teamcity;

import java.util.ArrayList;
import java.util.List;

public class JSONViewState {
    private final String rootURL;
    private final List<JobState> jobs;
    private final List<String> ids;

    public JSONViewState(String rootURL) {
        this.rootURL = rootURL;
        this.jobs = new ArrayList<JobState>();
        this.ids = new ArrayList<String>();
    }

    public String renderJobsList() {
        StringBuilder data = new StringBuilder();
        data.append("\"jobs\":[");

        if (jobs.size() > 0) {
            for (JobState job : jobs) {
                data.append("{\"name\":\"").append(job.name)
                        .append("\",\"url\":\"").append(rootURL).append("/viewType.html?buildTypeId=").append(job.id)
                        .append("\",\"responsible\":\"").append(job.responsible)
                        .append("\",\"project\":\"").append(job.project);
                if (job.secondsElapsed > 0) {
                    data.append("\",\"secondsElapsed\":\"").append(job.secondsElapsed);
                }
                data.append("\",\"color\":\"").append(job.color).append("\"},");
            }
            data.deleteCharAt(data.length() - 1);
        } else {
            data.append("{}");
        }

        data.append("],");
        return data.toString();
    }

    public void addJob(JobState job) {
        if (!ids.contains(job.id)) {
            ids.add(job.id);
            jobs.add(job);
        }
    }

    public int getJobSize() {
        return jobs.size();
    }
}
