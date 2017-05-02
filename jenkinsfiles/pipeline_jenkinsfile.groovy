node {
   try {
     artifactVersion = "1.2.$BUILD_NUMBER"
     stage('clean') {
        deleteDir()
     }
     stage('pull') {
        git 'git@github.ford.com:order-to-delivery/goe-services.git'
     }
     stage('test') {
        sh "export GIT_COMMIT=`git rev-parse HEAD`"
        sh "$WORKSPACE/gradlew test -Pversion=${artifactVersion}"

     }
     stage('create java artifacts') {
        sh "export GIT_COMMIT=`git rev-parse HEAD`"
        sh "$WORKSPACE/gradlew assemble -Pversion=${artifactVersion}"
     }

     stage('publish java artifacts') {
        sh "$WORKSPACE/gradlew publish -Pversion=${artifactVersion}"
     }

     stage('tag release') {
        sh "git tag release/${artifactVersion}"
        sh "git push origin --tags"
     }
     stage('retrieve artifacts') {
        def remoteLocation = "http://www.nexus.ford.com/content/repositories/goe_private_release_repository/com/ford/gotd/goe-services/${artifactVersion}"
        sh "curl -u gotdgoe:FED[3y ${remoteLocation}/goe-services-${artifactVersion}.jar -o /tmp/goe-services-${artifactVersion}.jar"
        sh "curl -u gotdgoe:FED[3y ${remoteLocation}/goe-services-${artifactVersion}-manifests.zip -o /tmp/goe-services-${artifactVersion}-manifests.zip"
     }
     stage('deploy to demo') {
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
   catch (e) {
      currentBuild.result = "FAILED"
      throw e
   }
   finally {
      notifyBuild(currentBuild.result)
   }
}

def deploy(dataCenter, artifactName, environment, type, username, password, space) {
      def artifactLocation = "/tmp"
      def expandedManifestLocation = "/tmp/${dataCenter}/${artifactName}"
      sh "mkdir -p ${expandedManifestLocation} && unzip -o ${artifactLocation}/${artifactName}-${artifactVersion}-manifests.zip -d ${expandedManifestLocation}"
      def manifestFile = "${expandedManifestLocation}/manifest-${environment}.yml"
      def api = "https://api.sys-q01.pcfqa${dataCenter}.ford.com"
      def cf_home = "~/.cf/${dataCenter}/${space}"
      def domain = "apps-q01.pcfqa${dataCenter}.ford.com"

      def command = 'mkdir -p ' + cf_home
      command += "; export CF_HOME=${cf_home}"
      command += "; cf api ${api} --skip-ssl-validation"
      command += "; cf login -u ${username} -p ${password} -o Ford_GOE_NA -s ${space}"
      command += "; cf push -f ${manifestFile} -p ${artifactLocation}/${artifactName}-${artifactVersion}.${type}"

      retry(3) { sh command }
}

def notifyBuild(String buildStatus) {

  buildStatus =  buildStatus ?: 'SUCCESSFUL'

  def colorName = 'RED'
  def colorCode = '#FF0000'
  def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
  def summary = "${subject} (${env.BUILD_URL})"

  // Override default values based on build status
  if (buildStatus == 'SUCCESSFUL') {
    colorCode = '#00FF00'
  } else {
    colorCode = '#FF0000'
  }

  // Send notifications
  slackSend (color: colorCode, message: summary)
}
