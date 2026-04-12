def call(Map cfg) {
    validateDockerInput(cfg.image, cfg.tag)
    sh "docker pull ${cfg.image}:${cfg.tag}"
    sh "docker stop ${cfg.containerName} || true"
    sh "docker rm -f ${cfg.containerName} || true"
    sh "docker run -d --name ${cfg.containerName} -p ${cfg.port}:${cfg.port} ${cfg.image}:${cfg.tag}"

    sh """
cat > deploy-info-${cfg.buildNumber}.txt <<EOF
build:  ${cfg.buildNumber}
image:  ${cfg.image}:${cfg.tag}
commit: ${cfg.gitCommit}
branch: ${cfg.gitBranch}
time:   \$(date -u +"%Y-%m-%dT%H:%M:%SZ")
url:    ${cfg.buildUrl}
EOF
"""
    archiveArtifacts artifacts: "deploy-info-${cfg.buildNumber}.txt",
                     fingerprint: true,
                     followSymlinks: false
}
