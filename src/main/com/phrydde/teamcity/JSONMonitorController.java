package com.phrydde.teamcity;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.serverSide.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class JSONMonitorController implements Controller {
    final Logger LOG = Logger.getInstance(JSONMonitorController.class.getName());
    private final SBuildServer server;
    private final ProjectManager projectManager;

    public JSONMonitorController(SBuildServer server, ProjectManager projectManager) {
        this.server = server;
        this.projectManager = projectManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String rootURL = server.getRootUrl();
        JSONViewState state = new JSONViewState(rootURL);

        List<String> requestedProjects = URIParser.getProjectList(request.getRequestURI());
        if (requestedProjects.size() == 0) {
            for (SProject project : projectManager.getActiveProjects()) {
                requestedProjects.add(project.getProjectId());
            }
        }

        ModelAndView modelAndView = new ModelAndView(new JSONView());

        for (String requestedProject : requestedProjects) {
            SProject project = projectManager.findProjectById(requestedProject);
            if (project != null) {
                List<SBuildType> buildTypes = project.getBuildTypes();
                for (SBuildType buildType : buildTypes) {
                    state.addJob(new JobState(buildType.getName(), buildType.getBuildTypeId(), buildType.getStatus().getText()));
                }
            } else {
                // TODO: Given a project that does not exist. Throw a 404?
            }
        }

        modelAndView.addObject("ViewState", state);
        modelAndView.addObject("url", rootURL + "/");
        modelAndView.addObject("numExecutors", server.getBuildAgentManager().<SBuildAgent>getRegisteredAgents().size());

        return modelAndView;
    }
}
