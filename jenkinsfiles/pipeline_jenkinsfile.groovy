pipeline {
  agent any

  stages {
	/*
	stage('clean') {
		steps {
			deleteDir()
		}
	}

	stage('pull') {
		steps {
			git '/Users/nayan/Documents/workspace/pipeline-demo/pipeline-demo-service'
		}
	}

	stage('unit test') {
		steps {
			sh "$WORKSPACE/gradlew test"
		}
	}

	stage('integration test') {
		steps {
			sh "$WORKSPACE/gradlew testIntegration"
		}
	}

	stage('create artifacts') {
		steps {
			sh "$WORKSPACE/gradlew assemble"
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

	stage('retrieve artifacts') {
		steps {
			def remoteLocation =
            "http://www.nexus.ford.com/content/repositories/goe_private_release_repository/com/ford/gotd/goe-services/${releaseVersion}"
			sh "curl -u gotdgoe:FED[3y ${remoteLocation}/goe-services-${releaseVersion}.jar -o
            /tmp/goe-services-${releaseVersion}.jar"
			sh "curl -u gotdgoe:FED[3y ${remoteLocation}/goe-services-${releaseVersion}-manifests.zip -o
            /tmp/goe-services-${releaseVersion}-manifests.zip"
		}
	}

	stage('deploy') {
		steps {
			)
		}
	}
	*/
  }
}
