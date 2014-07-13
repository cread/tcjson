package io.cread.teamcity;

import jetbrains.buildServer.responsibility.ResponsibilityEntry;
import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.responsibility.BuildTypeResponsibilityFacade;
import jetbrains.buildServer.users.User;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class JSONMonitorController implements Controller {
    private final SBuildServer server;
    private final ProjectManager projectManager;
    private final BuildTypeResponsibilityFacade responsibilityFacade;

    public JSONMonitorController(SBuildServer server,
                                 ProjectManager projectManager,
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
                String externalId = project.getExternalId();
                if (!requestedProjects.contains(externalId)) {
                    requestedProjects.add(externalId);
                }
            }
        }

        ModelAndView modelAndView = new ModelAndView(new JSONView());

        for (String requestedProject : requestedProjects) {
            SProject project = projectManager.findProjectByExternalId(requestedProject);
            if (project != null) {
                List<SBuildType> buildTypes = project.getBuildTypes();
                for (SBuildType buildType : buildTypes) {
                    if (buildType.getProject().isArchived()) {
                        continue;
                    }

                    SBuild latestBuild = buildType.getLastChangesStartedBuild();
                    if (latestBuild != null) {
                        String userName = "";

                        ResponsibilityEntry buildTypeResponsibility = responsibilityFacade.findBuildTypeResponsibility(buildType);
                        if (buildTypeResponsibility != null) {
                            User responsibleUser = buildTypeResponsibility.getResponsibleUser();
                            userName = responsibleUser.getUsername();
                        }

                        long elapsed = -1;
                        if (!latestBuild.isFinished()) {
                            elapsed = latestBuild.getDuration();
                        }

                        state.addJob(new JobState(
                                latestBuild.getBuildTypeName(),
                                latestBuild.getBuildTypeExternalId(),
                                latestBuild.getBuildStatus().getText(),
                                userName,
                                latestBuild.getProjectExternalId(),
                                elapsed
                        ));
                    }
                }
            }
        }

        modelAndView.addObject("ViewState", state);
        modelAndView.addObject("url", rootURL + "/");
        modelAndView.addObject("numExecutors", server.getBuildAgentManager().<SBuildAgent>getRegisteredAgents().size());

        return modelAndView;
    }
}
