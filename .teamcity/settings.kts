import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.MavenBuildStep
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
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

version = "2021.1"

project {

    vcsRoot(SpringPetclinicTeamcityDsl_PetclinicVcs)

    buildType(SpringPetclinicTeamcityDsl_Build)
}

object SpringPetclinicTeamcityDsl_Build : BuildType({
    id("Build")
    name = "Build"

    artifactRules = "target/*jar"

    vcs {
        root(SpringPetclinicTeamcityDsl_PetclinicVcs)
    }

    steps {
        maven {
            goals = "clean package"
            localRepoScope = MavenBuildStep.RepositoryScope.MAVEN_DEFAULT
            dockerImage = "maven:3.6.0-jdk-8"
        }
    }

    triggers {
        vcs {
            groupCheckinsByCommitter = true
        }
    }
})

object SpringPetclinicTeamcityDsl_PetclinicVcs : GitVcsRoot({
    id("PetclinicVcs")
    name = "PetclinicVcs"
    url = "https://github.com/spring-projects/spring-petclinic.git"
    branch = "main"
    checkoutPolicy = GitVcsRoot.AgentCheckoutPolicy.USE_MIRRORS
})
