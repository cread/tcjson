package com.phrydde.teamcity;

import jetbrains.buildServer.serverSide.MainConfigProcessor;
import jetbrains.buildServer.serverSide.ProjectManager;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.responsibility.BuildTypeResponsibilityFacade;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import jetbrains.buildServer.web.openapi.WebResourcesManager;
import com.intellij.openapi.diagnostic.Logger;
import org.jdom.Element;


public class TCJSON implements MainConfigProcessor {
    final Logger LOG = Logger.getInstance(TCJSON.class.getName());
    public static final String PLUGIN_NAME = TCJSON.class.getSimpleName().toLowerCase();

    public TCJSON(SBuildServer server, ProjectManager projectManager,
                BuildTypeResponsibilityFacade responsibilityFacade,
                WebControllerManager webControllerManager, WebResourcesManager webResourcesManager) {

        server.registerExtension(MainConfigProcessor.class, PLUGIN_NAME, this);

        webResourcesManager.addPluginResources(PLUGIN_NAME, PLUGIN_NAME + ".jar");
        webControllerManager.registerController("/app/json/**", new JSONMonitorController(server,
              projectManager, responsibilityFacade));
    }

    public void readFrom(Element element) {
        // Nothing to see, move along
    }

    public void writeTo(Element element) {
        // Nothing to see, move along
    }
}
