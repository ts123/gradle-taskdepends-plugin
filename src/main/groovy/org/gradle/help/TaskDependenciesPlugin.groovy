package org.gradle.help

import org.gradle.api.Project
import org.gradle.api.Plugin

class TaskDependenciesPlugin implements Plugin<Project> {
    void apply(Project target) {
        target.task('taskDependencies', type: TaskDependenciesTask)
    }
}
