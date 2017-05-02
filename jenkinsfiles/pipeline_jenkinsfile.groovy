node {
  /*
  stage('clean') {
    deleteDir()
  }

  stage('pull') {
    git '/Users/nayan/Documents/workspace/pipeline-demo/pipeline-demo-service'
  }

  stage('unit test') {
    sh "$WORKSPACE/gradlew test
  }

  stage('integration test') {
    sh "$WORKSPACE/gradlew testIntegration
  }

  stage('create artifacts') {
    sh "$WORKSPACE/gradlew assemble
  }
  stage('publish artifacts') {
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

  stage('deploy') {
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
  */
}

/*
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
*/
