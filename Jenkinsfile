pipeline {
    agent any

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
}
