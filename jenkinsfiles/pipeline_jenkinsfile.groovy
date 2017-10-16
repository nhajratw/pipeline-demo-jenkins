pipeline {
  agent any


  stages {
    stage('pull') {
      steps {
        git url:'/Users/nayan/Documents/workspace/pipeline-demo/pipeline-demo-service', branch:'agiledc17'
      }
    }
  }

}
