pipeline {
  agent any
  environment { releaseVersion = "1.0.$BUILD_NUMBER" }

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
        sh "$WORKSPACE/gradlew assemble -Pversion=${releaseVersion}"
      }
    }

    stage('publish artifact') {
      steps {
        sh "$WORKSPACE/gradlew publish -Pversion=${releaseVersion}"
      }
    }

    stage('deploy a') {
      steps {
        sh """
          cf login -a api.local.pcfdev.io --skip-ssl-validation -u admin -p admin -o demo -s pipeline
          //cf unmap-route pipeline-demo-service-a local.pcfdev.io --hostname pipeline-demo-service

          cf push pipeline-demo-service-a -p $WORKSPACE/build/libs/pipeline-demo-service-${releaseVersion}.jar
          cf map-route pipeline-demo-service-a local.pcfdev.io --hostname pipeline-demo-service
        """
      }
    }

    stage('deploy b') {
      steps {
        sh """
          cf login -a api.local.pcfdev.io --skip-ssl-validation -u admin -p admin -o demo -s pipeline

          cf push pipeline-demo-service-b -p $WORKSPACE/build/libs/pipeline-demo-service-${releaseVersion}.jar
          cf map-route pipeline-demo-service-b local.pcfdev.io --hostname pipeline-demo-service
        """
      }
    }


  }
}
