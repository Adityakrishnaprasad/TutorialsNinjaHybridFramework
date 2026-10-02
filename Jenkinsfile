pipeline {
    agent any

    parameters {
        choice(
            name: 'RUN_MODE',
            choices: ['grid', 'local'],
            description: 'grid = Chrome, Firefox and Edge in parallel on Docker Grid | local = Chrome only on this machine'
        )
    }

    triggers {
        // Scheduled runs use the first choice above (grid)
        cron('H 11,22 * * *')
    }

    environment {
        baseURL      = credentials('baseURL')
        app_username = credentials('app_username')
        app_password = credentials('app_password')
        gridURL      = credentials('gridURL')
        notify_email = credentials('notify_email')
        // First run after adding the parameter can have no value, so default to grid
        RUN_MODE     = "${params.RUN_MODE ?: 'grid'}"
    }

    stages {
        stage('Clean Old Results') {
            steps {
                bat 'if exist allure-results rmdir /s /q allure-results'
            }
        }

        stage('Start Selenium Grid') {
            when { environment name: 'RUN_MODE', value: 'grid' }
            steps {
                bat 'docker compose up -d'
                // Wait until the hub and all 3 browser nodes are ready (max 2 minutes)
                powershell '''
                    $deadline = (Get-Date).AddSeconds(120)
                    while ((Get-Date) -lt $deadline) {
                        try {
                            $s = Invoke-RestMethod -Uri "http://localhost:4444/status" -TimeoutSec 5
                            if ($s.value.ready -and $s.value.nodes.Count -ge 3) {
                                Write-Host "Selenium Grid is ready with $($s.value.nodes.Count) nodes"
                                exit 0
                            }
                        } catch { }
                        Start-Sleep -Seconds 3
                    }
                    Write-Error "Selenium Grid was not ready within 120 seconds"
                    exit 1
                '''
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    def suite   = (env.RUN_MODE == 'grid') ? 'grid-parallel-suite.xml' : 'master.xml'
                    def execEnv = (env.RUN_MODE == 'grid') ? 'remote' : 'local'
                    echo "Run mode: ${env.RUN_MODE} | Suite: ${suite}"
                    bat "mvn clean test -Dsurefire.suiteXmlFiles=${suite} -Dexecution_env=${execEnv}"
                }
            }
        }
    }

    post {
        always {
            allure(
                includeProperties: false,
                jdk: '',
                results: [[path: 'allure-results']]
            )
        }

        success {
            emailext(
                to: env.notify_email,
                subject: "SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER} (${env.RUN_MODE})",
                body: """
Job: ${env.JOB_NAME}
Build Number: ${env.BUILD_NUMBER}
Run Mode: ${env.RUN_MODE}
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
                subject: "FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER} (${env.RUN_MODE})",
                body: """
Job: ${env.JOB_NAME}
Build Number: ${env.BUILD_NUMBER}
Run Mode: ${env.RUN_MODE}
Status: FAILED
Build URL: ${env.BUILD_URL}

Allure Report:
${env.BUILD_URL}allure/
"""
            )
        }

        cleanup {
            script {
                if (env.RUN_MODE == 'grid') {
                    // Always stop the Grid; never fail the build because of this step
                    bat(script: 'docker compose down', returnStatus: true)
                }
            }
        }
    }
}
