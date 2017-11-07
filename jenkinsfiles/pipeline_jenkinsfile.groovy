pipeline {
  agent any

  stages {
    stage('clean') {
      steps {
        deleteDir()
      }
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

    stage('create artifact') {
      steps {
        sh "$WORKSPACE/gradlew assemble"
      }
    }


  }
}
