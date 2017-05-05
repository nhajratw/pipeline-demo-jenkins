pipeline {
  agent any

  stages {
    stage('clean') {
      steps {
        deleteDir()
      }
    }
  }
}
