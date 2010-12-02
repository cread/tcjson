package com.phrydde.teamcity;

import com.intellij.openapi.diagnostic.Logger;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class JSONView implements View {
    final Logger LOG = Logger.getInstance(JSONView.class.getName());

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

        for (int i = 0; i < (Integer)map.get("projectCount"); i++) {
            HashMap project = (HashMap)map.get("project-" + String.valueOf(i));

            if (project == null) {
                LOG.error("Got a null project! project-" + String.valueOf(i));
            }
            data.append("\"name\":\"").append(project.get("name")).append("\",");
            data.append("\"url\":\"").append(project.get("url")).append("\",");
            data.append("\"color\":\"").append(project.get("color")).append("\",");
        }
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
