def call(String image, String tag, String containerName, String port, String buildNumber, String gitCommit, String gitBranch, String buildUrl) {
    sh "docker pull ${image}:${tag}"
    sh "docker stop ${containerName} || true"
    sh "docker rm -f ${containerName} || true"
    sh "docker run -d --name ${containerName} -p ${port}:${port} ${image}:${tag}"

    sh """
cat > deploy-info-${buildNumber}.txt <<EOF
build:  ${buildNumber}
image:  ${image}:${tag}
commit: ${gitCommit}
branch: ${gitBranch}
time:   \$(date -u +"%Y-%m-%dT%H:%M:%SZ")
url:    ${buildUrl}
EOF
"""
    archiveArtifacts artifacts: "deploy-info-${buildNumber}.txt",
                     fingerprint: true,
                     followSymlinks: false
}
