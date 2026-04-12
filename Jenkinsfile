// groovylint-disable CompileStatic, NoDef, UnusedVariable, VariableName, VariableTypeRequired
@Library('cwvj-shared-library@main') _

pipeline {
    agent any

    environment {
        IMAGE          = 'docker.io/lasmor2025/cwvj-flask'
        TAG            = "${BUILD_NUMBER}"
        CONTAINER_NAME = 'cwvj-flask'
        PORT           = '5000'
    }

    stages {
        stage('Build') {
            steps {
                dockerBuild(IMAGE, TAG)
            }
        }

        stage('Push') {
            steps {
                dockerPush(IMAGE, TAG)
            }
        }

        stage('Deploy') {
            steps {
                dockerDeploy(image: IMAGE, tag: TAG, containerName: CONTAINER_NAME, port: PORT,
                             buildNumber: BUILD_NUMBER, gitCommit: GIT_COMMIT,
                             gitBranch: GIT_BRANCH, buildUrl: BUILD_URL)
            }
        }

        stage('Test') {
            steps {
                sh 'sleep 2; echo "Hit http://localhost:5000 to see the app."'
            }
        }

        stage('Cleanup') {
            steps {
                dockerCleanup()
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
