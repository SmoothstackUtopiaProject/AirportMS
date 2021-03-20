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
                sh "$AWS_LOGIN"                
                sh "docker build --tag utopiaairportms:$COMMIT_HASH ."
                sh "docker tag utopiaairportms:$COMMIT_HASH $AWS_ID/utopiaairlines/airportms:$COMMIT_HASH"
                sh "docker push $AWS_ID/utopiaairlines/airportms:$COMMIT_HASH"
            }
        }
        stage('Deploy') {
           steps {
               sh "touch ECSService.yml"
               sh "rm ECSService.yml"
               sh "wget https://raw.githubusercontent.com/SmoothstackUtopiaProject/CloudFormationTemplates/main/ECSService.yml"
               sh "aws cloudformation deploy --stack-name UtopiaAirportMS --template-file ./ECSService.yml --parameter-overrides ApplicationName=UtopiaAirportMS ECRepositoryUri=$AWS_ID/utopiaairlines/airportms:$COMMIT_HASH DBUsername=$DB_USERNAME DBPassword=$DB_PASSWORD SubnetID=$SUBNET_ID SecurityGroupID=$SECURITY_GROUP_ID TGArn=$UTOPIA_PASSENGERMS_TARGETGROUP --capabilities \"CAPABILITY_IAM\" \"CAPABILITY_NAMED_IAM\""
           }
        }
        stage('Cleanup') {
            steps {
                sh "docker system prune -f"
            }
        }
    }
}
