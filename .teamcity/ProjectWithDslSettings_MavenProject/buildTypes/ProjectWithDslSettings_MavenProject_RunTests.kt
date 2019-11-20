package ProjectWithDslSettings_MavenProject.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.maven

object ProjectWithDslSettings_MavenProject_RunTests : BuildType({
    uuid = "517b5459-64e2-481d-afc6-65aec63ac2ea"
    name = "run tests"

    vcs {
        root(ProjectWithDslSettings_MavenProject.vcsRoots.ProjectWithDslSettings_MavenProject_ProjectRoot)
    }

    steps {
        maven {
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }
    }
})
