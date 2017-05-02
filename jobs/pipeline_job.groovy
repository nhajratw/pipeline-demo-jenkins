pipelineJob("pipeline-demo") {
  description "Build, test and deploy the pipeline demo"
    logRotator {
      numToKeep(50)
    }
  triggers{
    githubPush()
  }
  definition {
    cpsScm {
      scm {
        git {
          remote {
            url('git@github.ford.com:order-to-delivery/jenkins-job-configurations.git')
            branch('*/master')
          }
        }
      }
      scriptPath("jenkinsfiles/pipeline_jenkinsfile.groovy")
    }
  }
}
