package io.cread.teamcity;

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

        data.append("{")
                .append("\"assignedLabels\":[{}],")
                .append("\"mode\":\"NORMAL\",")
                .append("\"nodeDescription\":\"TeamCity Jenkins/Hudson interface\",")
                .append("\"nodeName\":\"\",")
                .append("\"numExecutors\":").append(map.get("numExecutors")).append(",")
                .append("\"description\":null,");

        JSONViewState state = (JSONViewState)map.get("ViewState");

        if (state != null) {
            data.append(state.renderJobsList());
        }

        data.append("\"overallLoad\":{},")
                .append("\"primaryView\":{")
                .append("\"name\":\"All\",")
                .append("\"url\":\"").append(map.get("url")).append("\"")
                .append("},")
                .append("\"slaveAgentPort\":0,")
                .append("\"useCrumbs\":false,")
                .append("\"useSecurity\":true,")
                .append("\"views\":[{")
                .append("\"name\":\"All\",")
                .append("\"url\":\"").append(map.get("url")).append("\"")
                .append("}]")
                .append("}");

        String[] jsonp = request.getParameterValues("jsonp");

        if (jsonp != null) {
            data.insert(0, jsonp[0] + "(");
            data.append(")\n");
        } else {
            data.append("\n");
        }
        PrintWriter writer = response.getWriter();
        writer.write(data.toString());
    }
}
