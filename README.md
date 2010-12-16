Description
===========

tcjson is a Server Side Plugin to add JSON and JSONP support to [TeamCity](http://www.jetbrains.com/teamcity/). It is built to emulate the Hudson JSON API.

Built and tested with TeamCity 5.x. TeamCity 6.x supported too.

Installation
============

To install, simply copy the tcjson-[version].zip file to your `~/.BuildServer/plugins` directory and restart your server.

Usage
=====

Here are some sample HTTP requests.

To see all active builds:

    GET /app/json/api/json

To see all active builds for a given project:

    GET /app/json/<projectId>/api/json

To see all active builds for a list of projects:

    GET /app/json/<projectId1>/<projectId2>/<projectId3>/api/json

Authentication is controlled via the global TeamCity settings. 

