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
  }
/*


  stage('retrieve artifacts') {
    steps {
      def remoteLocation =
      "http://www.nexus.ford.com/content/repositories/goe_private_release_repository/com/ford/gotd/goe-services/${releaseVersion}"
      sh "curl -u gotdgoe:FED[3y ${remoteLocation}/goe-services-${releaseVersion}.jar -o /tmp/goe-services-${releaseVersion}.jar"
      sh "curl -u gotdgoe:FED[3y ${remoteLocation}/goe-services-${releaseVersion}-manifests.zip -o
      /tmp/goe-services-${releaseVersion}-manifests.zip"
    }
  }

  stage('deploy') {
    steps {
      sh "rm -rf ~/.cf/ecc ~/.cf/fmc"
      def username = "goe-ci"
      def password = "Goe2016!"
      parallel (
        "ecc services demo" : { deploy("ecc", "goe-services", "demo", "jar", username, password, "Beta") },
        "fmc services demo" : { deploy("fmc", "goe-services", "demo", "jar", username, password, "Beta") },
        "ecc services ci"   : { deploy("ecc", "goe-services", "ci", "jar", username, password, "Alpha") },
        "fmc services ci"   : { deploy("fmc", "goe-services", "ci", "jar", username, password, "Alpha") },
      )
    }
  }
*/
}

/*
def deploy(dataCenter, artifactName, environment, type, username, password, space) {
  def artifactLocation = "/tmp"
  def expandedManifestLocation = "/tmp/${dataCenter}/${artifactName}"
  sh "mkdir -p ${expandedManifestLocation} && unzip -o ${artifactLocation}/${artifactName}-${releaseVersion}-manifests.zip -d ${expandedManifestLocation}"
  def manifestFile = "${expandedManifestLocation}/manifest-${environment}.yml"
  def api = "https://api.sys-q01.pcfqa${dataCenter}.ford.com"
  def cf_home = "~/.cf/${dataCenter}/${space}"
  def domain = "apps-q01.pcfqa${dataCenter}.ford.com"

  def command = 'mkdir -p ' + cf_home
  command += "; export CF_HOME=${cf_home}"
  command += "; cf api ${api} --skip-ssl-validation"
  command += "; cf login -u ${username} -p ${password} -o Ford_GOE_NA -s ${space}"
  command += "; cf push -f ${manifestFile} -p ${artifactLocation}/${artifactName}-${releaseVersion}.${type}"

  retry(3) { sh command }
}
*/
