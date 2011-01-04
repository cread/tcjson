package com.phrydde.teamcity;

import junit.framework.TestCase;

public class JSONViewStateTests extends TestCase {
    public void testShouldRenderEmptyListIfNoJobs() {
        JSONViewState state = new JSONViewState("");
        assertEquals("\"jobs\":[{}],", state.renderJobsList());
    }

    public void testShouldBeAbleToRenderMultipleJobDetails() {
        JSONViewState state = new JSONViewState("http://localhost:8111");

        state.addJob(new JobState("Job1", "bt3", "ERROR", "buildadmin", "Big Project", 100L));
        state.addJob(new JobState("Job2", "bt5", "SUCCESS", "jdoe", "Medium Project", 200L));


        assertEquals("\"jobs\":[" +
                "{\"name\":\"Job1\",\"url\":\"http://localhost:8111/viewType.html?buildTypeId=bt3\",\"responsible\":\"buildadmin\",\"project\":\"Big Project\",\"secondsElapsed\":\"100\",\"color\":\"red\"}," +
                "{\"name\":\"Job2\",\"url\":\"http://localhost:8111/viewType.html?buildTypeId=bt5\",\"responsible\":\"jdoe\",\"project\":\"Medium Project\",\"secondsElapsed\":\"200\",\"color\":\"blue\"}" +
                "],", state.renderJobsList());
    }

    public void testShouldBeAbleToRenderSingleJobDetails() {
        JSONViewState state = new JSONViewState("http://localhost:8111");

        state.addJob(new JobState("Job1", "bt3", "ERROR", "rstevens", "Little Project", 300L));

        assertEquals("\"jobs\":[" +
                "{\"name\":\"Job1\",\"url\":\"http://localhost:8111/viewType.html?buildTypeId=bt3\",\"responsible\":\"rstevens\",\"project\":\"Little Project\",\"secondsElapsed\":\"300\",\"color\":\"red\"}" +
                "],", state.renderJobsList());
    }

}
