pipelineJob("pipeline-demo-service") {
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
            url('/Users/nayan/Documents/workspace/pipeline-demo/pipeline-demo-service')
            branch('*/goat17')
          }
        }
      }
      scriptPath("Jenkinsfile")
    }
  }
}
