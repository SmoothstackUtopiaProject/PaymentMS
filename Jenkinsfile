pipeline {
    agent any
    environment {
        APPLICATION_NAME = 'UtopiaPassengerMS'
        APPLICATION_NAME_LOWERCASE = 'utopiapassengerms'
        APPLICATION_REPOSITORY = 'utopia/utopiapassengerms'
        COMMIT_HASH = "${sh(script:'git rev-parse --short HEAD', returnStdout: true).trim()}"
    }

    stages {
        stage('Environment') {
            // List Environment Variables for easy trouble-shooting
            // To keep secrets kept safe, regulate Jenkins HTTP & SSH access
            echo "ENVIRONMENT VARIABLES:"
            echo "========================================================="
            echo "========================================================="
            echo "Application Name: $APPLICATION_NAME"
            echo "Application Name (lowercase): $APPLICATION_NAME_LOWERCASE"
            echo "Application Repository: $APPLICATION_REPOSITORY"
            echo "AWS ID: $AWS_ID"
            echo "AWS Login: $AWS_LOGIN"
            echo "Commit Hash: $COMMIT_HASH"
            echo "Database URL: $DB_URL"
            echo "Database Username: $DB_USERNAME"
            echo "Database Password: $DB_PASSWORD"
            echo "Execution Role ARN: $EXECUTION_ROLE_ARN"
            echo "Security Group ID: $SECURITY_GROUP_ID"
            echo "Subnet ID: $SUBNET_ID"
            echo "Target Group ARN (development): $UTOPIA_PASSENGERMS_TARGETGROUP_DEV"
            echo "Target Group ARN (production): $UTOPIA_PASSENGERMS_TARGETGROUP_PROD"
            echo "VPC ID: $VPC_ID"
            echo "========================================================="
            echo "========================================================="
        }
        stage('Package') {
            steps {
                echo 'Building..'
                script {
                    sh 'mvn clean package'
                }
            }
        }
        stage('Build') {
            steps {
                echo 'Deploying....'
                sh "$AWS_LOGIN"
                sh "docker build --tag $APPLICATION_NAME_LOWERCASE:$COMMIT_HASH ."
                sh "docker tag $APPLICATION_NAME_LOWERCASE:$COMMIT_HASH $AWS_ID/$APPLICATION_REPOSITORY:$COMMIT_HASH"
                sh "docker push $AWS_ID/$APPLICATION_REPOSITORY:$COMMIT_HASH"
            }
        }
        stage('Deploy') {
            steps {
                // Grabs the Cloud Formation Template
                sh 'touch CloudDeploymentTemplate.yml'
                sh 'rm CloudDeploymentTemplate.yml'
                sh 'touch UtopiaAirlinesServicesTemplate.yml'
                sh 'rm UtopiaAirlinesServicesTemplate.yml'
                sh 'wget https://raw.githubusercontent.com/SmoothstackUtopiaProject/CloudFormationTemplates/main/UtopiaAirlinesServicesTemplate.yml'
                sh 'mv UtopiaAirlinesServicesTemplate.yml CloudDeploymentTemplate.yml'

                // Grabs the Cloud Deployment Script
                sh 'touch CloudDeploy.sh'
                sh 'rm CloudDeploy.sh'
                sh 'wget https://raw.githubusercontent.com/SmoothstackUtopiaProject/CloudFormationTemplates/main/CloudDeploy.sh'
                sh 'chmod 777 ./CloudDeploy.sh'
                sh 'exec ./CloudDeploy.sh'
            }
        }
        stage('Cleanup') {
            steps {
                sh ' docker system prune -a --volumes -f'
            }
        }
    }
}
