pipeline {
  agent any

  environment {
    releaseVersion = "1.0.$BUILD_NUMBER"
  }

  stages {
    stage('clean') {
      steps {
        deleteDir()
      }
    }
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

    stage('create artifact') {
      steps {
        sh "$WORKSPACE/gradlew assemble -Pversion=${releaseVersion}" 
      }
    }

    stage('publish to nexus') {
      steps {
        sh "$WORKSPACE/gradlew publish -Pversion=${releaseVersion}" 
      }
    }
  }

}
