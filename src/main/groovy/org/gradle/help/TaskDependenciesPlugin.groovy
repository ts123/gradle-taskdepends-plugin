package org.gradle.help

import org.gradle.api.Project
import org.gradle.api.Plugin

class TaskDependenciesPlugin implements Plugin<Project> {
    public void apply(Project project ) {
        project.extensions.create('taskdepends', TaskDependenciesPluginExtension)
        project.task('taskDependencies', type: TaskDependenciesTask)
    }
}


