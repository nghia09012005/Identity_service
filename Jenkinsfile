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
        success {
            echo '🚀 Build completed successfully on production branch!'
        }
        failure {
            echo '❌ Build failed. Check logs!'
        }
    }
}
// start ngrok before push code