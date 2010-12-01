package com.phrydde.teamcity;

import jetbrains.buildServer.serverSide.ProjectManager;
import jetbrains.buildServer.serverSide.SBuildAgent;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SProject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class JSONMonitorController implements Controller {
    private final SBuildServer server;
    private final ProjectManager projectManager;

    public JSONMonitorController(SBuildServer server, ProjectManager projectManager) {
        this.server = server;
        this.projectManager = projectManager;
    }

    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        int numberOfProjects = projectManager.getNumberOfProjects();

        ModelAndView modelAndView = new ModelAndView(new JSONView());

        modelAndView.addObject("url", server.getRootUrl());
        modelAndView.addObject("numExecutors", server.getBuildAgentManager().<SBuildAgent>getRegisteredAgents().size());
        modelAndView.addObject("projectCount", numberOfProjects);

        HashMap[] projects = new HashMap[numberOfProjects];

        for (SProject project : projectManager.getProjects()) {
            System.out.println(" -> project.getName() = " + project.getName());
        }

        modelAndView.addObject("projects", projects);
        return modelAndView;
    }
}
