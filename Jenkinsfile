pipeline {
    agent any

    environment {
        IMAGE_NAME = "identity-service"
        CONTAINER_NAME = "identity-service"
        PORT = "8081"
    }

    triggers {
        githubPush()
    }

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


    post {
        success {
            echo '🚀 Deployment completed successfully on production branch!'
        }
        failure {
            echo '❌ Deployment failed. Check logs!'
        }
    }
}
