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
        notify_email   = credentials('notify_email')
    }

    stages {
        stage('Build & Test') {
            steps {
                bat 'mvn clean test'
            }
        }
    }

   post {
    always {
        allure(
            includeProperties: false,
            jdk: '',
            results: [[path: 'target/allure-results']]
        )
    }

    success {
        emailext(
            to: env.notify_email,
            subject: "SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            body: """
Job: ${env.JOB_NAME}
Build Number: ${env.BUILD_NUMBER}
Status: SUCCESS
Build URL: ${env.BUILD_URL}

Allure Report:
${env.BUILD_URL}allure/
"""
        )
    }

    failure {
        emailext(
            to: env.notify_email,
            subject: "FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            body: """
Job: ${env.JOB_NAME}
Build Number: ${env.BUILD_NUMBER}
Status: FAILED
Build URL: ${env.BUILD_URL}

Allure Report:
${env.BUILD_URL}allure/
"""
        )
    }
}

}
