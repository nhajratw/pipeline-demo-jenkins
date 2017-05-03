pipeline {

  agent any

  environment {
    releaseVersion = "1.0.${BUILD_NUMBER}"
  }
  
  stages {

    stage('clean') {
      steps {
        deleteDir()
      }
    }

    stage('pull') {
      steps {
        git url:'/Users/nayan/Documents/workspace/pipeline-demo/pipeline-demo-service', branch:'setup'
      }
    }

    stage('unit test') {
      steps {
        sh "$WORKSPACE/gradlew test -Pversion=${releaseVersion}"
      }
    }

    stage('integration test') {
      steps {
        sh "$WORKSPACE/gradlew testIntegration -Pversion=${releaseVersion}"
        junit 'build/test-results/**/*.xml'
      }
    }

    stage('create artifacts') {
      steps {
        sh "$WORKSPACE/gradlew assemble -Pversion=${releaseVersion}"
      }
    }

    stage('publish artifacts') {
      steps {
        sh "$WORKSPACE/gradlew publish -Pversion=${releaseVersion}"
      }
    }

    stage('tag release') {
      steps {
        sh "git tag release/${releaseVersion}"
        sh "git push origin --tags"
      }
    }

    stage('deploy') {
      steps {
        sh "cf login -a api.local.pcfdev.io --skip-ssl-validation -u admin -p admin -o demo -s pipeline"
        sh "cf push pipeline-demo-service -p $WORKSPACE/build/libs/pipeline-demo-service-${releaseVersion}.jar"
      }
    }
  }
}
