def call(String image, String tag) {
    sh "docker build -t ${image}:${tag} -t ${image}:latest ./python"
}
