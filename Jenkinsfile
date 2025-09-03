pipeline {
    agent any

    environment {
        IMAGE_NAME = "nghiatran9105/identity-service" // tên trên Docker Hub
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

        stage('Docker Build') {
            steps {
                sh 'docker build -t $IMAGE_NAME:latest .'
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                    sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                }
            }
        }

        stage('Docker Push') {
            steps {
                sh 'docker push $IMAGE_NAME:latest'
            }
        }

        stage('Stop Old Container (Remote)') {
            steps {
                sshagent(['production-server-ssh']) { // ID credential SSH tới server production
                    sh '''
                    ssh -o StrictHostKeyChecking=no user@production-server "
                    if [ \\"$(docker ps -q -f name=$CONTAINER_NAME)\\" ]; then
                        docker stop $CONTAINER_NAME
                        docker rm $CONTAINER_NAME
                    fi
                    "
                    '''
                }
            }
        }

        stage('Run New Container (Remote)') {
            steps {
                sshagent(['production-server-ssh']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no user@production-server "
                    docker pull $IMAGE_NAME:latest
                    docker run -d --name $CONTAINER_NAME -p $PORT:8081 $IMAGE_NAME:latest
                    "
                    '''
                }
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
