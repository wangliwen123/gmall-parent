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
        sh '''source /etc/profile
java -version

mvn clean package -Dmaven.test.skip=true'''
      }
    }
  }
}