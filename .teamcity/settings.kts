import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2021.2"

project {
    defaultTemplate = RelativeId("DefaultTemplateWithParameter")

    vcsRoot(DotNetProject_ProjectRoot)
    vcsRoot(MavenProject_ProjectRoot)

    template(DefaultTemplateWithParameter)

    subProject(MavenProject)

    name = "Default name for project from VCS settings"
}

object DefaultTemplateWithParameter : Template({
    name = "Default Template"

    params {
        param("a", "b")
        password("parameter-from-token", "credentialsJSON:65bd2531-08d0-423a-a578-731e4cc3cd70")
    }
})

object DotNetProject_ProjectRoot : GitVcsRoot({
    name = "dotnet vcs"
    url = "https://github.com/burnasheva/mstest_dotnet3.git"
    branchSpec = "+:refs/heads/*"
})

object MavenProject_ProjectRoot : GitVcsRoot({
    name = "maven vcs"
    url = "https://github.com/burnasheva/maven_unbalanced_messages.git"
    branchSpec = "+:refs/heads/*"
    authMethod = password {
        userName = "burnasheva"
        password = "credentialsJSON:65bd2531-08d0-423a-a578-f853db5b788f"
    }
})


object MavenProject : Project({
    name = "maven project"

    buildType(MavenProject_RunTests)
    buildType(MavenProject_SimpleEcho)
})

object MavenProject_RunTests : BuildType({
    name = "maven configuration"

    vcs {
        root(MavenProject_ProjectRoot)
    }

    steps {
        maven {
            id = "RUNNER_1"
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }
    }
})

object MavenProject_SimpleEcho : BuildType({
    name = "maven echo configuration"

    steps {
        script {
            id = "RUNNER_1"
            scriptContent = """
                echo "Special Russial Symbol: русские символы"
                echo "Property from local file: value"
                echo "Optional Parameter: DEFAULT"
                echo "Context parameter value: %context.parameter%"
            """.trimIndent()
        }
    }

    params {
        param("context.parameter", DslContext.getParameter("context.parameter"))
    }
})
