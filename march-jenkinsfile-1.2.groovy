pipeline {
    agent any
    //def setDescription() { 
    //def item = Jenkins.instance.getItemByFullName(env.JOB_NAME) 
    //item.setDescription("Some description.") 
    //item.save()
  // }
   //setDescription()
    parameters {
        choice(name: 'VERSION', choices: ['1.0', '1.1', '1.2'], description: 'declarative pipeline')
        booleanParam(name: 'Run', defaultValue: true, description: 'boolean')
    }
    stages {
        stage('Clone Application Code Using GIT') {
            steps {
                git 'https://github.com/psrc8888/Development-Team-Repo.git'
            }
        }
        stage('Build Application Using Maven') {
            steps {
                script {
                    currentBuild.displayName = "Current Build"
                    currentBuild.description = "The Declarative Pipeline"
                }
                sh 'mvn package -f pom.xml'
            }
        }
        stage('Upload Build Artifact to Nexus Repository') {
            steps {
                nexusArtifactUploader artifacts: [[artifactId: 'web', classifier: '', file: 'target/web.war', type: 'war']], credentialsId: 'nexuskey', groupId: 'project', nexusUrl: 'http://44.204.144.184:8081/nexus', nexusVersion: 'nexus2', protocol: 'http', repository: 'releases', version: '2.3'
            }
        }
        stage('Deploy .war file To Tomcat Server') {                 
            steps {
                echo 'Deploy Application Code'
                //sh 'sudo cp /var/lib/jenkins/workspace/secondpipeline/target/web.war /root/apache-tomcat-10.1.7/webapps'
                sh 'sudo cd /root/apache-tomcat-10.1.7/webapps'
                sh 'sudo wget http://44.204.144.184:8081/nexus/content/repositories/releases/web.war'
            }
        }
    }
}
