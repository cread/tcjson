package com.phrydde.teamcity;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.messages.Status;
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
    final Logger LOG = Logger.getInstance(JSONMonitorController.class.getName());
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

        HashMap projects = new HashMap();

        for (int i = 0; i < numberOfProjects; i++) {
            HashMap p = new HashMap();
            SProject project = projectManager.getProjects().get(i);
            System.out.println(" -> project.getName() = " + project.getName());
            LOG.error(" -> project.getName() = " + project.getName());
            p.put("name", project.getName());
            p.put("url", project.getProjectId());
            p.put("color", project.getStatus());

            projects.put("project-" + String.valueOf(i), p);
        }

        modelAndView.addObject("projects", projects);
        return modelAndView;
    }
}
