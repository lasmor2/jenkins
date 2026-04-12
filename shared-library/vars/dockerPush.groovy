def call(String image, String tag, String credentialsId = 'dockerhub') {
    withCredentials([usernamePassword(
        credentialsId: credentialsId,
        usernameVariable: 'DOCKERHUB_USER',
        passwordVariable: 'DOCKERHUB_PWD'
    )]) {
        sh "echo $DOCKERHUB_PWD | docker login -u $DOCKERHUB_USER --password-stdin"
        sh "docker push ${image}:${tag}"
        sh "docker push ${image}:latest"
    }
}
