pipeline {
    agent any

    environment {
        GITHUB_REPO = 'https://github.com/lasmor2/jenkins.git'
        IMAGE       = 'docker.io/lasmor2025/python-app'
        TAG         = "${BUILD_NUMBER}"
        CONTAINER   = 'python-app'
        PORT        = '5000'
    }

    stages {
        stage('Checkout') {
            steps {
                git url: "${GITHUB_REPO}",
                    branch: 'main',
                    credentialsId: 'github-creds'
            }
        }

        stage('Build') {
            steps {
                dir('jenkins/python') {
                    bat 'docker build -t "%IMAGE%:%TAG%" -t "%IMAGE%:latest" .'
                }
            }
        }

        stage('Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub',
                    usernameVariable: 'DOCKERHUB_USER',
                    passwordVariable: 'DOCKERHUB_PWD'
                )]) {
                    bat 'echo %DOCKERHUB_PWD% | docker login -u %DOCKERHUB_USER% --password-stdin'
                    bat 'docker push "%IMAGE%:%TAG%"'
                    bat 'docker push "%IMAGE%:latest"'
                }
            }
        }

        stage('Deploy') {
            steps {
                bat 'docker rm -f %CONTAINER% || exit 0'
                bat 'docker run -d --name %CONTAINER% -p %PORT%:5000 "%IMAGE%:%TAG%"'
                bat """
                    (
                        echo build:  %BUILD_NUMBER%
                        echo image:  %IMAGE%:%TAG%
                        echo commit: %GIT_COMMIT%
                        echo branch: %GIT_BRANCH%
                        echo url:    %BUILD_URL%
                    ) > deploy-info-%BUILD_NUMBER%.txt
                """
                archiveArtifacts artifacts: "deploy-info-${BUILD_NUMBER}.txt",
                                 fingerprint: true,
                                 followSymlinks: false
            }
        }

        stage('Test') {
            steps {
                bat 'ping -n 4 127.0.0.1 > nul'
                bat """
                    for /f %%i in ('curl -s -o nul -w "%%{http_code}" http://localhost:%PORT%/') do set STATUS=%%i
                    echo HTTP status: %STATUS%
                    if not "%STATUS%"=="200" (echo Health check failed! && exit 1)
                """
            }
        }

        stage('Cleanup') {
            steps {
                cleanWs()
            }
        }
    }

    post {
        success { echo "Build ${env.BUILD_NUMBER} succeeded" }
        failure { echo "Build ${env.BUILD_NUMBER} failed" }
        always  { echo "Build ${env.BUILD_NUMBER} finished" }
    }
}
