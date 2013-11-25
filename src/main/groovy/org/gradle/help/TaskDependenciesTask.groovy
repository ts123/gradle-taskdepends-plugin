package org.gradle.help

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class TaskDependenciesTask extends DefaultTask {
    {
        description = "Display dependencies of your project's each taskss"
    }

    def int count

    @TaskAction
    public void show() {
        project.getTasks().each { task ->
            if (project.taskdepends.showRootTaskNames) {
                if (!project.taskdepends.showRootTaskNames.contains(task.getName())) {
                    return
                }
            }
            println ""
            count = 0;
            printDepends(task)
        }
    }

    public void printDepends(task, String indent = "", int depth = 0, boolean islast = false) {
        count = count + 1
        if (project.taskdepends.maxLine < count) {
            return
        }
        if (project.taskdepends.maxDepth < depth) {
            return
        }
        if (!task) {
            return
        }
        if (task.metaClass.respondsTo(task, "getName")) {
            if (task.getName() == "buildDependents") {
                return
            }
            if (project.taskdepends.ignoreTaskNames.contains(task.getName())) {
                return
            }
        }
        if (!(task instanceof org.gradle.api.internal.tasks.DefaultTaskDependency)) {
            print indent
            if (depth == 0) {
                indent += ""
            }
            else if (islast) {
                print '\\---'
                indent += "    "
            }
            else {
                print '+---'
                indent += "|   "
            }
            println task.getName()
        }
        if (task.metaClass.respondsTo(task, "getTaskDependencies")) {
            task.getTaskDependencies().eachWithIndex { o, i ->
                printDepends(o, indent, depth+1, true)
            }
        }
        else if (task.metaClass.respondsTo(task, "getDependencies")) {
            Set depends = task.getDependencies()
            if (depends) {
                depends.eachWithIndex { o, i ->
                    printDepends(o, indent, depth+1, i == task.getDependencies().size() - 1)
                }
            }
        }
    }
}
