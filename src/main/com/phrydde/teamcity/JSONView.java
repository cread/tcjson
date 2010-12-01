package com.phrydde.teamcity;

import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JSONView implements View {
    public String getContentType() {
        return "application/json";
    }

    public void render(Map map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        StringBuilder data = new StringBuilder();

        String[] jsonp = request.getParameterValues("jsonp");

        if (jsonp != null) {
            data.append(jsonp[0]).append("(");
        }


        data.append("{")
            .append("\"assignedLabels\":[{}],")
            .append("\"mode\":\"NORMAL\",")
            .append("\"nodeDescription\":\"TeamCity Hudson clone\",")
            .append("\"nodeName\":\"\",")
            .append("\"numExecutors\":").append(map.get("numExecutors")).append(",")
            .append("\"projectCount\":").append(map.get("projectCount")).append(",")
            .append("\"description\":null,")
            .append("\"jobs\":[{");

        // TODO: Loop through and fill this in
//            "name":"Broken Build",
//            "url":"http://localhost:8080/job/Broken%20Build/",
//            "color":"red"
//        },
//        {
//            "name":"Clean Build",
//            "url":"http://localhost:8080/job/Clean%20Build/",
//            "color":"blue"
//        }],

        data.append("}],")
            .append("\"overallLoad\":{},")
            .append("\"primaryView\":{")
            .append("\"name\":\"All\",")
            .append("\"url\":\"").append(map.get("url")).append("\"")
            .append(")},")
            .append("\"slaveAgentPort\":0,")
            .append("\"useCrumbs\":false,")
            .append("\"useSecurity\":false,")
            .append("\"views\":[{")
            .append("\"name\":\"All\",")
            .append("\"url\":\"").append(map.get("url")).append("\"")
            .append(")}]")
            .append("}");

        if (jsonp != null) {
            data.append(")\n");
        } else {
            data.append("\n");
        }
        PrintWriter writer = response.getWriter();
        writer.write(data.toString());
    }
}
