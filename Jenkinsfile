pipeline {
    agent any
    environment {
        COMMIT_HASH="${sh(script:'git rev-parse --short HEAD', returnStdout: true).trim()}"
    }

    stages {
        stage('Package') {
            steps {
                echo 'Building..'
                script {
                    sh "mvn clean package"
                }
            }
        }
        stage('Build') {
            steps {
                echo 'Deploying....'
                sh "aws ecr get-login-password --region us-east-1 --profile=default | docker login --username AWS --password-stdin 466486113081.dkr.ecr.us-east-1.amazonaws.com"                
                sh "docker build --tag utopiaairportms:$COMMIT_HASH ."
                sh "docker tag utopiaairportms:$COMMIT_HASH 466486113081.dkr.ecr.us-east-1.amazonaws.com/utopiaairlines/airportms:$COMMIT_HASH"
                sh "docker push 466486113081.dkr.ecr.us-east-1.amazonaws.com/utopiaairlines/airportms:$COMMIT_HASH"
            }
        }
        stage('Deploy') {
           steps {
               sh "touch ECSService.yml"
               sh "rm ECSService.yml"
               sh "wget https://raw.githubusercontent.com/SmoothstackUtopiaProject/CloudFormationTemplates/main/ECSService.yml"
               sh "aws cloudformation deploy --stack-name UtopiaAirportMS --template-file ./ECSService.yml --parameter-overrides ApplicationName=UtopiaAirportMS ECRepositoryUri=466486113081.dkr.ecr.us-east-1.amazonaws.com/utopiaairlines/airportms:$COMMIT_HASH DBUsername=$DB_USERNAME DBPassword=$DB_PASSWORD --capabilities \"CAPABILITY_IAM\" \"CAPABILITY_NAMED_IAM\""
           }
        }
        stage('Cleanup') {
            steps {
                sh "docker system prune -f"
            }
        }
    }
}
