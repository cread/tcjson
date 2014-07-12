package io.cread.teamcity;

import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.responsibility.BuildTypeResponsibilityFacade;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public class JSONMonitorController implements Controller {
    private final SBuildServer server;
    private final ProjectManager projectManager;
    private final BuildTypeResponsibilityFacade responsibilityFacade;

    public JSONMonitorController(SBuildServer server, ProjectManager projectManager,
                                 BuildTypeResponsibilityFacade responsibilityFacade) {
        this.server = server;
        this.projectManager = projectManager;
        this.responsibilityFacade = responsibilityFacade;
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
                Date today = new Date();
                long todaysTime = today.getTime() / 1000;
                for (SBuildType buildType : buildTypes) {
                    if (buildType.getProject().isArchived()) {
                        continue;
                    }
                    String userName;
                    try {
                        userName = responsibilityFacade.findBuildTypeResponsibility( buildType )
                                   .getResponsibleUser().getUsername();
                    } catch (NullPointerException e) {
                        // It's quite likely to not have responsibility assigned for a build failure
                        userName = "";
                    }
                    try {
                        state.addJob(new JobState(
                            buildType.getName(),
                            buildType.getExternalId(),
                            buildType.getStatus().getText(),
                            userName,
                            buildType.getProject().getExtendedFullName(),
                            todaysTime - buildType.getLastChangesFinished().getFinishDate().getTime() / 1000 ));
                    } catch (NullPointerException e) {
                        // It's possible to have a build that doesn't have any finished builds yet,
                        // getLastChangesFinished() might therefore return null.
                        continue;
                    }
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
