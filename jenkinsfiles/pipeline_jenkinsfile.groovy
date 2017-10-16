pipeline {
  agent any


  stages {
    stage('pull') {
      steps {
        git url:'/Users/nayan/Documents/workspace/pipeline-demo/pipeline-demo-service', branch:'agiledc17'
      }
    }

    stage('micro tests') {
      steps {
        sh "$WORKSPACE/gradlew test" 
	junit "build/test-results/**/*.xml"
      }
    }

  }

}
