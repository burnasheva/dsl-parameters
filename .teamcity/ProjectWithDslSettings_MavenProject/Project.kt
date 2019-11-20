package ProjectWithDslSettings_MavenProject

import ProjectWithDslSettings_MavenProject.buildTypes.*
import ProjectWithDslSettings_MavenProject.vcsRoots.*
import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.Project

object Project : Project({
    uuid = "760624eb-1d3e-46df-a4b0-1f83b59d395a"
    id("ProjectWithDslSettings_MavenProject")
    parentId("ProjectWithDslSettings")
    name = "Maven Project"

    vcsRoot(ProjectWithDslSettings_MavenProject_ProjectRoot)

    buildType(ProjectWithDslSettings_MavenProject_RunTests)
})
