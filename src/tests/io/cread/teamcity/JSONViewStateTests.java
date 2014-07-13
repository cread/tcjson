package io.cread.teamcity;

import io.cread.teamcity.JSONViewState;
import io.cread.teamcity.JobState;
import junit.framework.TestCase;

public class JSONViewStateTests extends TestCase {
    JSONViewState state;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        state = new JSONViewState("http://localhost:8111");
    }

    public void testShouldRenderEmptyListIfNoJobs() {
        assertEquals("\"jobs\":[{}],", state.renderJobsList());
    }

    public void testShouldBeAbleToRenderMultipleJobDetails() {
        state.addJob(new JobState("Job1", "bt3", "ERROR", "buildadmin", "Big Project", 100L));
        state.addJob(new JobState("Job2", "bt5", "SUCCESS", "jdoe", "Medium Project", 200L));


        assertEquals("\"jobs\":[" +
                "{\"name\":\"Job1\",\"url\":\"http://localhost:8111/viewType.html?buildTypeId=bt3\",\"responsible\":\"buildadmin\",\"project\":\"Big Project\",\"secondsElapsed\":\"100\",\"color\":\"red\"}," +
                "{\"name\":\"Job2\",\"url\":\"http://localhost:8111/viewType.html?buildTypeId=bt5\",\"responsible\":\"jdoe\",\"project\":\"Medium Project\",\"secondsElapsed\":\"200\",\"color\":\"blue\"}" +
                "],", state.renderJobsList());
    }

    public void testShouldBeAbleToRenderSingleJobDetails() {
        state.addJob(new JobState("Job1", "bt3", "ERROR", "rstevens", "Little Project", 300L));

        assertEquals("\"jobs\":[" +
                "{\"name\":\"Job1\",\"url\":\"http://localhost:8111/viewType.html?buildTypeId=bt3\",\"responsible\":\"rstevens\",\"project\":\"Little Project\",\"secondsElapsed\":\"300\",\"color\":\"red\"}" +
                "],", state.renderJobsList());
    }

    public void testShouldNotAddDuplicates() {
        JobState job = new JobState("Job1", "bt3", "SUCCESS", "cread", "Stuff", -1);

        state.addJob(job);

        assertEquals(1, state.getJobSize());

        state.addJob(job);

        assertEquals(1, state.getJobSize());
    }

    public void testShouldNotRenderNoElapsedSeconds() {
        state.addJob(new JobState("Job1", "bt3", "ERROR", "rstevens", "Little Project", -1L));

        assertEquals("\"jobs\":[" +
                "{\"name\":\"Job1\",\"url\":\"http://localhost:8111/viewType.html?buildTypeId=bt3\",\"responsible\":\"rstevens\",\"project\":\"Little Project\",\"color\":\"red\"}" +
                "],", state.renderJobsList());
    }
}
