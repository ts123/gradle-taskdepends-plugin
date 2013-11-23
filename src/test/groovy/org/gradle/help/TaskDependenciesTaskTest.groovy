package org.gradle.help

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import static org.junit.Assert.*

class TaskDependenciesTaskTest {
    @Test
    public void canAddTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('taskDependencies', type: TaskDependenciesTask)
        assertTrue(task instanceof TaskDependenciesTask)
    }
}

