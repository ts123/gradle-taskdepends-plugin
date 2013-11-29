package org.gradle.help

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.internal.AbstractTask
import org.gradle.api.internal.artifacts.DefaultPublishArtifactSet
import org.gradle.api.internal.artifacts.configurations.DefaultConfiguration
import org.gradle.api.internal.file.UnionFileCollection
import org.gradle.api.internal.file.collections.DefaultConfigurableFileCollection
import org.gradle.api.internal.tasks.AbstractTaskDependency
import org.gradle.api.internal.tasks.CommandLineOption
import org.gradle.api.internal.tasks.DefaultTaskDependency
import org.gradle.api.tasks.TaskAction
import org.gradle.language.base.LanguageSourceSet

class TaskDependenciesTask extends DefaultTask {
    {
        description = "Display dependencies of your project's each taskss"
    }

    int count = 0
    String rootTaskName
    private boolean DEBUG = false
    boolean detail = false
    final String sep = ','

    @CommandLineOption(options = "all", description = "Show tasks and files.")
    public void setShowDetail(boolean detail) {
//        this.detail = detail;
        project.extensions.taskdepends.showDetail = detail
    }
    @CommandLineOption(options = "root", description = "")
    public void setRootNames(String names) {
        project.extensions.taskdepends.showRootTaskNames = names.split(sep);
    }
    @CommandLineOption(options = "rootw", description = "")
    public void setRootWords(String names) {
        project.extensions.taskdepends.showRootTaskWords = names.split(sep);
    }
    @CommandLineOption(options = "rootp", description = "")
    public void setRootPatterns(String names) {
        project.extensions.taskdepends.showRootTaskPattern = names.split(sep);
    }
    @CommandLineOption(options = "ignore", description = "")
    public void setIgnoreNames(String names) {
        project.extensions.taskdepends.ignoreTaskNames = names.split(sep);
    }
    @CommandLineOption(options = "ignorew", description = "")
    public void setIgnoreWords(String names) {
        project.extensions.taskdepends.ignoreTaskWords = names.split(sep);
    }
    @CommandLineOption(options = "ignorep", description = "")
    public void setIgnorePatterns(String names) {
        project.extensions.taskdepends.ignoreTaskPatterns = names.split(sep);
    }

    @TaskAction
    def show() {
        detail = project.extensions.taskdepends.showDetail
        project.getTasks().each { task ->
            if (project.taskdepends.showRootTaskNames) {
                if (!project.taskdepends.showRootTaskNames.contains(task.getName())) {
                    return
                }
            }
            println ""
            count = 0
            printTaskDependency(task)
        }
    }

    void printTaskDependency(object, String indent = "", int depth = 0, boolean islast = true) {
        count = count + 1
        if (project.taskdepends.maxLine < count) {
            return
        }
        if (project.taskdepends.maxDepth < depth) {
            return
        }
        if (!object) {
            return
        }
        if (object instanceof String) {
            // from DefaultTaskDependency#values
            return
        }
        if (object.metaClass.respondsTo(object, "getName")) {
            def name = object.getName()
            if (project.taskdepends.ignoreTaskNames.contains(name)) {
                return
            }
            for (def it : project.taskdepends.ignoreTaskWords) {
                if (name.contains(it)) return
            }
            for (def it : project.taskdepends.ignoreTaskPatterns) {
                if (name ==~ /$it/) return
            }
        }

        // print one node line
        if (!(object instanceof DefaultTaskDependency) || detail) {
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
            printNode(object)
        }

        // find object's dependency
        if (object instanceof AbstractTask) {
             printTaskDependency(object.getTaskDependencies(), indent, depth+1, true)
        }
        else if (object instanceof AbstractTaskDependency) {
            if (object instanceof DefaultTaskDependency) {
                def depends = []
                if (detail) {
                    depends.addAll(object.values)
                }
                // buildDependents's dependency object throw GradleException
                // when getDepencencies() is called
                try {
                    depends.addAll(object.getDependencies())
                } catch (GradleException e) {
                    if (DEBUG) e.printStackTrace()
                }
                def len = depends.size()
                depends.eachWithIndex { o, i ->
                    printTaskDependency(o, indent, depth+1, i == len - 1)
                }
            }
            else {
                // for ArtifactsTaskDependency in ArtifactSet
                if (object.hasProperty('this$0')) {
                    printTaskDependency(object.this$0, indent, depth+1, true)
                    return
                }
                println object
            }
            return
        }
        else if (object instanceof UnionFileCollection) {
            def len = object.sources.size()
//            printf "%s:%d\n", task, len
            object.sources.toArray().eachWithIndex { o, i ->
                printTaskDependency(o, indent, depth+1, i == len - 1)
            }
            return
        }
        else if (object instanceof DefaultPublishArtifactSet) {
            def len = object.files.getFiles().size()
//            printf "%s:%d\n", task, len
            object.files.getFiles().eachWithIndex { o, i ->
                printTaskDependency(o, indent, depth+1, i == len -1)
            }
            return
        }
        else if (object instanceof DefaultConfigurableFileCollection) {
            def len = object.files.size()
            object.files.eachWithIndex { o, i ->
                printTaskDependency(o, indent, depth+1, i == len -1)
            }
            return
        }
    }

    def printNode(object) {
        if (object instanceof  DefaultTask) {
            if (DEBUG) print 'dtask:'
            printf '%s\n', object.getName()
        }
        else if (object instanceof String) {
            // assemble
            if (DEBUG) print 'str:'
            printf '%s\n', object
        }
        else if (object instanceof File) {
            print 'file://'
            printf '%s\n', object
        }
        else if (object instanceof DefaultConfiguration) {
            if (DEBUG) print 'dconf:'
            printf '%s\n', object
        }
        //
        else if (object instanceof UnionFileCollection) {
            if (DEBUG) print 'ufc:'
            printf 'files: %s\n', object.getFiles().size()
        }
        // task ':XXX' input files
        // task ':XXX' source files
        else if (object instanceof DefaultConfigurableFileCollection) {
            if (DEBUG) print 'dcfc:'
            printf '%s\n', object
        }
        else if (object instanceof LanguageSourceSet) {
            if (DEBUG) print 'lss:'
            printf '%s\n', object
        }
        else if (object instanceof AbstractTaskDependency) {
            if (DEBUG) {
                print 'atd:'
                printf '%s\n', object
            }
            else {
                println '\\'
            }
        }
        else if (object instanceof DefaultPublishArtifactSet) {
            if (DEBUG) print 'dpas:'
            println object
        }
        else {
            if (DEBUG) print 'unknown:'
            println object
        }
    }
}
