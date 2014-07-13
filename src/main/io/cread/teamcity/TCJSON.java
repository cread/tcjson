package io.cread.teamcity;

import jetbrains.buildServer.serverSide.ServerExtension;
import jetbrains.buildServer.serverSide.ProjectManager;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.responsibility.BuildTypeResponsibilityFacade;
import jetbrains.buildServer.web.openapi.WebControllerManager;

public class TCJSON implements ServerExtension {
    public static final String PLUGIN_NAME = TCJSON.class.getSimpleName().toLowerCase();

    public TCJSON(SBuildServer server,
                  ProjectManager projectManager,
                  BuildTypeResponsibilityFacade responsibilityFacade,
                  WebControllerManager webControllerManager) {

        server.registerExtension(ServerExtension.class, PLUGIN_NAME, this);

        webControllerManager.registerController("/app/json/**",
                new JSONMonitorController(server, projectManager, responsibilityFacade));
    }
}
