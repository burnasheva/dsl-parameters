import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.*
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot
import java.io.File
import java.io.FileInputStream
import java.util.*

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

version = "2019.2"

project {

    vcsRoot(MavenProject_ProjectRoot)
    vcsRoot(DotNetProject_ProjectRoot)

    subProject(MavenProject)

    val createDotnet = DslContext.getParameter("is.dotnet.needed", "false")
    if (createDotnet.toBoolean()) {
        subProject(DotNetProject)
    }
}

object DotNetProject : Project({
    name = DslContext.getParameter("project.name.dotnet")

    buildType(DotNetProject_RunTests)
})

object DotNetProject_RunTests : BuildType({
    name = DslContext.getParameter("build.name.dotnet")

    vcs {
        root(DotNetProject_ProjectRoot)
    }

    steps {
        dotnetRestore {
            projects = "**/*.csproj"
        }
        dotnetBuild {
            projects = "**/*.csproj"
        }
        dotnetTest {
            projects = "**/*Tests.csproj"
        }
    }
})


object MavenProject : Project({
    name = DslContext.getParameter("project.name.maven")

    buildType(MavenProject_RunTests)
    buildType(MavenProject_SimpleEcho)
})

object MavenProject_SimpleEcho : BuildType({
    name = DslContext.getParameter("build.name.maven.echo")
    val inputFile = FileInputStream(File(DslContext.baseDir, "some.properties"))
    val prop = Properties()
    prop.load(inputFile)
    val optionalParameter = DslContext.getParameter("scrip.symbols.optional", defaultValue = "DEFAULT")

    steps {
        script {
            scriptContent = """
                echo "Special Russial Symbol: ${DslContext.getParameter("script.symbols.russian")}"
                echo "Property from local file: ${prop.getProperty("property.name")}"
                echo "Optional Parameter: $optionalParameter"
                """.trimIndent()
        }
    }
})

object MavenProject_RunTests : BuildType({
    name = DslContext.getParameter("build.name.maven")

    vcs {
        root(MavenProject_ProjectRoot)
    }

    steps {
        maven {
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }
    }
})

object MavenProject_ProjectRoot : GitVcsRoot({
    name = DslContext.getParameter("vcs.name.maven")
    url = DslContext.getParameter("vcs.url.maven")
    branchSpec = "+:refs/heads/*"
    authMethod = password {
        userName = "burnasheva"
        password = "credentialsJSON:65bd2531-08d0-423a-a578-f853db5b788f"
    }
})

object DotNetProject_ProjectRoot : GitVcsRoot({
    name = DslContext.getParameter("vcs.name.dotnet")
    url = DslContext.getParameter("vcs.url.dotnet")
    branchSpec = "+:refs/heads/*"
})
