package org.gradle.help

class TaskDependenciesPluginExtension {
    int maxLine = 4000
    int maxDepth = 20
    String[] showRootTaskNames = []
    String[] ignoreTaskNames = []
}
