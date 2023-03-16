pipeline {
    agent any
    parameters {
        choice(name: 'VERSION', choices: ['1.0', '1.1', '1.2'], description: 'declarative pipeline')
        booleanParam(name: 'Run', defaultValue: true, description: 'boolean')
    }
    stages {
        stage('Clone Application Code') {
            steps {
                git 'https://github.com/psrc8888/Development-Team-Repo.git'
            }
        }
        stage('Build') {
            steps {
                script {
                    currentBuild.displayName = "Current Build"
                    currentBuild.description = "The Declarative Pipeline"
                }
                sh 'mvn package -f pom.xml'
            }
        }
        stage('Deploy') {                 // copy web.war file to tomcat server
            steps {
                echo 'Deploy Application Code'
                sh 'sudo cp /var/lib/jenkins/workspace/secondpipeline/target/web.war /root/apache-tomcat-10.1.7/webapps'
            }
        }
    }
}
