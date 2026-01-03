pipeline {
    agent any

    triggers {
        cron('0 11,22 * * *')
    }

    environment {
        baseURL        = credentials('baseURL')
        app_username   = credentials('app_username')
        app_password   = credentials('app_password')
        gridURL        = credentials('gridURL')
        execution_env  = credentials('execution_env')
    }

    stages {
        stage('Build & Test') {
            steps {
                bat 'mvn clean test'
            }
        }
    }

    post {
        success {
            emailext(
                to: 'adityakrishnaprasad6@gmail.com',
                subject: "SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
Job: ${env.JOB_NAME}
Build Number: ${env.BUILD_NUMBER}
Status: SUCCESS
Build URL: ${env.BUILD_URL}
"""
            )
        }
        failure {
            emailext(
                to: 'adityakrishnaprasad6@gmail.com',
                subject: "FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
Job: ${env.JOB_NAME}
Build Number: ${env.BUILD_NUMBER}
Status: FAILED
Build URL: ${env.BUILD_URL}
"""
            )
        }
    }
}
