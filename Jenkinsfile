pipeline {
  agent any
  stages {
    stage('OK') {
      steps {
        echo '66666'
      }
    }
    stage('error') {
      steps {
        sh '''java -version
mvn -version'''
      }
    }
  }
}