def call(String image, String tag, String buildContext = './python') {
    validateDockerInput(image, tag)
    sh "docker build -t ${image}:${tag} -t ${image}:latest ${buildContext}"
}
