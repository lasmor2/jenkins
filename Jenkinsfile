pipeline {
    agent any

    environment {
        GITHUB_REPO = 'https://github.com/lasmor2/jenkins.git'
        IMAGE       = 'docker.io/<your-dockerhub-username>/python-app'
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
                    sh 'docker build -t "$IMAGE:$TAG" -t "$IMAGE:latest" .'
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
                    sh 'echo "$DOCKERHUB_PWD" | docker login -u "$DOCKERHUB_USER" --password-stdin'
                    sh 'docker push "$IMAGE:$TAG"'
                    sh 'docker push "$IMAGE:latest"'
                }
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker rm -f $CONTAINER || true'
                sh 'docker run -d --name $CONTAINER -p $PORT:5000 "$IMAGE:$TAG"'
                sh '''
                    cat > deploy-info-$BUILD_NUMBER.txt <<EOF
build:  $BUILD_NUMBER
image:  $IMAGE:$TAG
commit: ${GIT_COMMIT}
branch: $GIT_BRANCH
time:   $(date -u +"%Y-%m-%dT%H:%M:%SZ")
url:    $BUILD_URL
EOF
                '''
                archiveArtifacts artifacts: "deploy-info-${BUILD_NUMBER}.txt",
                                 fingerprint: true,
                                 followSymlinks: false
            }
        }

        stage('Test') {
            steps {
                sh 'sleep 3'
                sh '''
                    STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:$PORT/)
                    echo "HTTP status: $STATUS"
                    [ "$STATUS" = "200" ] || (echo "Health check failed!" && exit 1)
                '''
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
