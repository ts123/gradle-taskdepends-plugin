package org.gradle.help

class TaskDependenciesPluginExtension {
    int maxLine = 4000
    int maxDepth = 20
    boolean showDetail = false
    String[] showRootTaskNames = []
    String[] ignoreTaskNames = []
}
