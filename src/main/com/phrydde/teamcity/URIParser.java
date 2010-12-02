package com.phrydde.teamcity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URIParser {
    public static List<String> getProjectList(String uri) {
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile("^/app/json/(.*?)/api/json$");
        Matcher matcher = pattern.matcher(uri);
        if (matcher.matches()) {
            String[] ids = matcher.group(1).split("/");
            list.addAll(Arrays.asList(ids));
        }
        return list;
    }
}
