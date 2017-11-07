pipeline {
  agent any

  stages {
    stage('clean') {
      deleteDir()
    }

    stage('pull') {
      steps {
        git url:'/Users/nayan/Documents/workspace/pipeline-demo/pipeline-demo-service', branch:'agileto2017'
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
