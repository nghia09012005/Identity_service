pipeline {
    agent any

    environment {
        IMAGE_NAME = "identity-service"
        CONTAINER_NAME = "identity-service"
        PORT = "8080"
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout') {
            when { branch 'production' }
            steps {
                git branch: 'production', url: 'https://github.com/nghia09012005/Identity_service.git'
            }
        }

        stage('Build JAR') {
            when { branch 'production' }
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            when { branch 'production' }
            steps {
                sh 'docker build -t $IMAGE_NAME:latest .'
            }
        }

        stage('Stop Old Container') {
            when { branch 'production' }
            steps {
                sh '''
                if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
                    docker stop $CONTAINER_NAME
                    docker rm $CONTAINER_NAME
                fi
                '''
            }
        }

        stage('Run New Container') {
            when { branch 'production' }
            steps {
                sh 'docker run -d --name $CONTAINER_NAME -p $PORT:8080 $IMAGE_NAME:latest'
            }
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
