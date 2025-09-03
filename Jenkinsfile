pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'production', url: 'https://github.com/nghia09012005/Identity_service.git'
            }
        }
        stage('Build JAR') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }
    }
    post {
        success { echo '✅ Build completed' }
        failure { echo '❌ Build failed' }
    }
}
// update jenkins config
// start ngrok before push code
// connet webhook of github to jenkins