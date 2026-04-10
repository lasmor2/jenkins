pipeline {
    agent any

    environment {
        IMAGE = 'docker.io/lasmor2025/cwvj-flask'
        TAG   = "${BUILD_NUMBER}"
    }

    stages {
        stage('Build') {
            steps {
                sh "docker build -t $IMAGE:$TAG -t $IMAGE:latest ./python"
            }
        }

        stage('Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub',
                    usernameVariable: 'DOCKERHUB_USER',
                    passwordVariable: 'DOCKERHUB_PWD'
                )]) {
                    sh "echo $DOCKERHUB_PWD | docker login -u $DOCKERHUB_USER --password-stdin"
                    sh "docker push $IMAGE:$TAG"
                    sh "docker push $IMAGE:latest"
                }
            }
        }

        stage('Deploy') {
            steps {
                sh "docker pull $IMAGE:$TAG"
                sh "docker stop cwvj-flask || true"
                sh "docker rm -f cwvj-flask || true"
                sh "docker run -d --name cwvj-flask -p 5000:5000 $IMAGE:$TAG"

                sh """
cat > deploy-info-${BUILD_NUMBER}.txt <<EOF
build:  ${BUILD_NUMBER}
image:  ${IMAGE}:${TAG}
commit: ${GIT_COMMIT}
branch: ${GIT_BRANCH}
time:   \$(date -u +"%Y-%m-%dT%H:%M:%SZ")
url:    ${BUILD_URL}
EOF
"""
                archiveArtifacts artifacts: "deploy-info-${BUILD_NUMBER}.txt",
                                 fingerprint: true,
                                 followSymlinks: false
            }
        }

        stage('Test') {
            steps {
                sh 'sleep 2; echo "Hit http://localhost:5000 to see the app."'
            }
        }

        stage('Cleanup') {
            steps {
                cleanWs()
            }
        }
    }

    post {
        success {
            echo "Build ${env.BUILD_NUMBER} succeeded"
        }
        failure {
            echo "Build ${env.BUILD_NUMBER} failed"
        }
        always {
            echo "Build ${env.BUILD_NUMBER} finished"
        }
    }
}
