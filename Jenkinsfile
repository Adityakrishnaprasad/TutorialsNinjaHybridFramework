// Reads email\summary.properties written by ci\build-email.ps1
def readSummary() {
    def summary = [:]
    if (fileExists('email/summary.properties')) {
        readFile('email/summary.properties').split('\\r?\\n').each { line ->
            def parts = line.split('=', 2)
            if (parts.size() == 2) { summary[parts[0].trim()] = parts[1].trim() }
        }
    }
    return summary
}

// "Scheduled" for cron runs, "Manual (user)" for Build Now / Build with Parameters
def triggerText() {
    if (currentBuild.getBuildCauses('hudson.triggers.TimerTrigger$TimerTriggerCause')) { return 'Scheduled' }
    def userCause = currentBuild.getBuildCauses('hudson.model.Cause$UserIdCause')
    if (userCause) { return "Manual (${userCause[0].userName})" }
    return 'Other'
}

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
                bat 'if exist email rmdir /s /q email'
            }
        }

        stage('Start Selenium Grid') {
            when { environment name: 'RUN_MODE', value: 'grid' }
            steps {
                // Remove any Grid left running from an earlier or local run
                bat(script: 'docker compose down', returnStatus: true)
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
                    // Test failures don't stop the build here; 'Check Results' decides the status
                    bat "mvn clean test -Dsurefire.suiteXmlFiles=${suite} -Dexecution_env=${execEnv} -Dmaven.test.failure.ignore=true"
                }
            }
        }

        stage('Check Results') {
            steps {
                powershell '& .\\ci\\build-email.ps1 -SummaryOnly'
                script {
                    def s = readSummary()
                    def total   = (s.TOTAL   ?: '0') as int
                    def failed  = (s.FAILED  ?: '0') as int
                    def skipped = (s.SKIPPED ?: '0') as int
                    if (total == 0) {
                        error('No test results were produced')
                    } else if (failed > 0 || skipped > 0) {
                        unstable("${failed} failed, ${skipped} skipped out of ${total} tests")
                    }
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

            script {
                // Allure command line installed by the Jenkins Allure plugin (used for the single-file report)
                def allureHome = ''
                try {
                    allureHome = tool 'Allure'
                } catch (e) {
                    allureHome = 'C:\\ProgramData\\Jenkins\\.jenkins\\tools\\ru.yandex.qatools.allure.jenkins.tools.AllureCommandlineInstallation\\Allure'
                }

                def status = currentBuild.currentResult
                withEnv([
                    "BUILD_RESULT=${status}",
                    "TRIGGER_TEXT=${triggerText()}",
                    "BUILD_START_MS=${currentBuild.startTimeInMillis}",
                    "BUILD_DURATION_MS=${currentBuild.duration}",
                    "ALLURE_CLI=${allureHome}"
                ]) {
                    powershell(script: '& .\\ci\\build-email.ps1', returnStatus: true)
                }

                def s = readSummary()
                def label = [SUCCESS: 'PASSED', UNSTABLE: 'UNSTABLE', FAILURE: 'FAILED', ABORTED: 'ABORTED'][status] ?: status
                def counts = s.TOTAL ? " - ${s.PASSED}/${s.TOTAL} passed" : ''

                emailext(
                    to: env.notify_email,
                    subject: "[${label}] ${env.JOB_NAME} #${env.BUILD_NUMBER}${counts} (${env.RUN_MODE})",
                    body: '${FILE,path="email/email-body.html"}',
                    mimeType: 'text/html',
                    attachmentsPattern: 'email/attach/*'
                )
            }
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
