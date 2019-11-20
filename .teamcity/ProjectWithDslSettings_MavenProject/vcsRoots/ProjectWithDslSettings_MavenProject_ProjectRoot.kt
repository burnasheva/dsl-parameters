package ProjectWithDslSettings_MavenProject.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

object ProjectWithDslSettings_MavenProject_ProjectRoot : GitVcsRoot({
    uuid = "ccd662db-b831-427c-8f65-e92de9f14461"
    name = "project root"
    url = "https://github.com/burnasheva/maven_unbalanced_messages.git"
    branchSpec = "+:refs/heads/*"
})
