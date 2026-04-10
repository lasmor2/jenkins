pipeline {
    agent { label 'windows' }
    environment {
        IMAGE="docker.io/cloudwithvarjosh/cwvj-flask"
        TAG="${BUILD_NUMBER}"
    }
    stages {
        stage ('build') {
            steps {
                bat 'docker build -t "%IMAGE%:%TAG%" -t "%IMAGE%:latest" ./python'
            }
        }
        stage ('push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'DOCKERHUB_PWD', usernameVariable: 'DOCKERHUB_USER')]) {
                bat 'echo %DOCKERHUB_PWD% | docker login -u "%DOCKERHUB_USER%" --password-stdin'
                bat 'docker push "%IMAGE%:%TAG%"'
                bat 'docker push "%IMAGE%:latest"'
}
                
            }
        }
stage('deploy') {
      steps {
        bat 'docker pull "%IMAGE%:%TAG%"'
        bat 'docker rm -f cwvj-flask 2>nul || exit /b 0'
        bat 'docker run -d --name cwvj-flask -p 5000:5000 "%IMAGE%:%TAG%"'

        // write deploy info with build number in the filename
        bat '''
          @echo off
          (
            echo build: %BUILD_NUMBER%
            echo image: %IMAGE%:%TAG%
            echo commit: %GIT_COMMIT%
            echo branch: %GIT_BRANCH%
            echo time: %date% %time%
            echo url: %BUILD_URL%
          ) > deploy-info-%BUILD_NUMBER%.txt
        '''

        // fingerprint: true tells Jenkins to compute and store a unique hash (a “fingerprint”) for the archived file.
        archiveArtifacts artifacts: "deploy-info-${BUILD_NUMBER}.txt", fingerprint: true, followSymlinks: false
      }
    }
        stage ('test') {
            steps {
                bat 'timeout /t 2 && echo Hit http://localhost:5000 to see the app.'
            }
        }      
            stage ('cleanup') {
            steps {
                cleanWs()
            }
        }
    }
      post {
    success {
        echo "Build ${env.BUILD_NUMBER} succeeded"
        }
    failure { echo "Build ${env.BUILD_NUMBER} failed" }
    always  { echo "Build ${env.BUILD_NUMBER} finished" }
  }
}
