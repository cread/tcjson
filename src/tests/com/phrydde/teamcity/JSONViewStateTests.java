package com.phrydde.teamcity;

import junit.framework.TestCase;

import jetbrains.buildServer.messages.Status;

public class JSONViewStateTests extends TestCase {
    public void testShouldRenderEmptyListIfNoJobs() {
        JSONViewState state = new JSONViewState("");
        assertEquals("\"jobs\":[{}],", state.renderJobsList());
    }

    public void testShouldBeAbleToRenderMultipleJobDetails() {
        JSONViewState state = new JSONViewState("http://localhost:8111");

        state.addJob(new JobState("Job1", "bt3", "ERROR"));
        state.addJob(new JobState("Job2", "bt5", "SUCCESS"));

        assertEquals("\"jobs\":[" +
                "{\"name\":\"Job1\",\"url\":\"http://localhost:8111/viewType.html?buildTypeId=bt3\",\"color\":\"red\"}," +
                "{\"name\":\"Job2\",\"url\":\"http://localhost:8111/viewType.html?buildTypeId=bt5\",\"color\":\"blue\"}" +
                "],", state.renderJobsList());
    }

    public void testShouldBeAbleToRenderSingleJobDetails() {
        JSONViewState state = new JSONViewState("http://localhost:8111");

        state.addJob(new JobState("Job1", "bt3", "ERROR"));

        assertEquals("\"jobs\":[" +
                "{\"name\":\"Job1\",\"url\":\"http://localhost:8111/viewType.html?buildTypeId=bt3\",\"color\":\"red\"}" +
                "],", state.renderJobsList());
    }

}
