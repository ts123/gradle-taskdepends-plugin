package org.gradle.help

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import static org.junit.Assert.*

class TaskDependenciesPluginTest {
    @Test
    public void pluginAddsTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'taskdepends'
        assertTrue(project.tasks.taskDependencies instanceof TaskDependenciesTask)
    }
}

