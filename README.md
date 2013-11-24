gradle-taskdepends-plugin
=========================

Display the ascii dependency trees of your project's each tasks.

### Usage example

create `build.gradle`:

    buildscript {
        repositories {
            maven {
                url 'https://github.com/ts123/maven-repository/raw/master'
            }
        }
        dependencies {
            classpath group: 'org.gradle.help', name: 'gradle-taskdepends-plugin', version: '1.0'
        }
    }
    apply plugin: 'taskdepends'
    apply plugin: 'java'

then execute gradle:

    $ gradle taskDependencies
    
result outout is:

    :taskDependencies
    
    assemble
    \---jar
        \---classes
            +---processResources
            \---compileJava
    
    build
    +---assemble
    |   \---jar
    |       \---classes
    |           +---processResources
    |           \---compileJava
    \---check
        \---test
            +---testClasses
            |   +---compileTestJava
            |   |   \---classes
            |   |       +---processResources
            |   |       \---compileJava
            |   \---processTestResources
            \---classes
                +---processResources
                \---compileJava
    
    
    buildNeeded
    \---build
        +---assemble
        |   \---jar
        |       \---classes
        |           +---processResources
        |           \---compileJava
        \---check
            \---test
                +---testClasses
                |   +---compileTestJava
                |   |   \---classes
                |   |       +---processResources
                |   |       \---compileJava
                |   \---processTestResources
                \---classes
                    +---processResources
                    \---compileJava
    
    check
    \---test
        +---testClasses
        |   +---compileTestJava
        |   |   \---classes
        |   |       +---processResources
        |   |       \---compileJava
        |   \---processTestResources
        \---classes
            +---processResources
            \---compileJava
    
    classes
    +---processResources
    \---compileJava
    
    clean
    
    compileJava
    
    compileTestJava
    \---classes
        +---processResources
        \---compileJava
    
    jar
    \---classes
        +---processResources
        \---compileJava
    
    javadoc
    \---classes
        +---processResources
        \---compileJava
    
    processResources
    
    processTestResources
    
    taskDependencies
    
    test
    +---testClasses
    |   +---compileTestJava
    |   |   \---classes
    |   |       +---processResources
    |   |       \---compileJava
    |   \---processTestResources
    \---classes
        +---processResources
        \---compileJava
    
    testClasses
    +---compileTestJava
    |   \---classes
    |       +---processResources
    |       \---compileJava
    \---processTestResources
    
