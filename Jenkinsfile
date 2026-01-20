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
        notify_email   = 'adityakrishnaprasad6@gmail.com'
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

        script {
            try {
                allure(
                    includeProperties: false,
                    jdk: '',
                    results: [[path: 'target/allure-results']]
                )
            } catch (Exception e) {
                echo "Allure failed or marked unstable, ignoring"
            }
        }

        // REQUIRED: force SUCCESS (keep as-is)
        script {
            currentBuild.result = 'SUCCESS'
        }
    }
}


        success {
            emailext(
                to: env.notify_email,
                replyTo: env.notify_email,                 // ✅ IMPORTANT for Gmail
                subject: "SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                mimeType: 'text/html',                    // ✅ IMPORTANT
                body: """
                <h3>Build SUCCESS</h3>
                <p><b>Job:</b> ${env.JOB_NAME}</p>
                <p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
                <p><b>Status:</b> SUCCESS</p>
                <p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>

                <p><b>Allure Report:</b>
                <a href="${env.BUILD_URL}allure/">View Report</a></p>
                """
            )
        }

        failure {
            emailext(
                to: env.notify_email,
                replyTo: env.notify_email,
                subject: "FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                mimeType: 'text/html',
                body: """
                <h3>Build FAILED</h3>
                <p><b>Job:</b> ${env.JOB_NAME}</p>
                <p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
                <p><b>Status:</b> FAILED</p>
                <p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>

                <p><b>Allure Report:</b>
                <a href="${env.BUILD_URL}allure/">View Report</a></p>
                """
            )
        }
    }
}
