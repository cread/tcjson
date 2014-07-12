package io.cread.teamcity;

import io.cread.teamcity.URIParser;
import junit.framework.TestCase;

import java.util.List;

public class URIParserTests extends TestCase {
    public void testCanPullOutSingleProjectFromURL() {
        List<String> list = URIParser.getProjectList("/app/json/MyProjectID/api/json");
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(list.get(0), "MyProjectID");
    }

    public void testCanPullOutMultipleProjectsFromURL() {
        List<String> list = URIParser.getProjectList("/app/json/Proj1/Proj2/Proj3/api/json");
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals(list.get(0), "Proj1");
        assertEquals(list.get(1), "Proj2");
        assertEquals(list.get(2), "Proj3");
    }

    public void testCanPullOutMultipleProjectsFromURLUsingAuth() {
        List<String> list = URIParser.getProjectList("/httpAuth/app/json/Proj1/Proj2/Proj3/api/json");
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals(list.get(0), "Proj1");
        assertEquals(list.get(1), "Proj2");
        assertEquals(list.get(2), "Proj3");
    }


    public void testOnlyAcceptsAPIJSONAtTheEnd() {
        List<String> list = URIParser.getProjectList("/app/json/MyProjectID/sheep/cheese");
        assertNotNull(list);
        assertEquals(0, list.size());
        list = URIParser.getProjectList("/app/json/MyProjectID/api/cheese");
        assertNotNull(list);
        assertEquals(0, list.size());
        list = URIParser.getProjectList("/app/json/MyProjectID/sheep/json");
        assertNotNull(list);
        assertEquals(0, list.size());
    }
}
