pipeline {
 
  environment {
       IBM_ACCESS_KEY_ID     = credentials('ibmuser')
        IBM_SECRET_ACCESS_KEY = credentials('ibmkey')
        //project=credentials('project-name')
        gatlingConf=""
        url_for_gatling=''
    }
 
  agent any
 
  stages {
 


  stage('Checkout Source') {
      
      steps {
        git "https://github.com/eduardojc-met/testjsonfoundation.git"
      }
    }
 

    stage('Get & use Configuration') {
      
      steps {
        script{


def json = readJSON file: 'configurations.json' 

echo json[0].stepsFile.toString()

def foundationConf=json[0].stepsFile.toString()
gatlingConf=json[1].stepsFile.toString()

def pipelineFoundation = load foundationConf
bat 'IF not exist foundation (mkdir foundation)'
dir("foundation") {
pipelineFoundation.start("","","","","","","","")
}

 dir("foundation/src/main/docker/frontend") {

   	def ingress = readYaml file:"ingress.yaml"
    url_for_gatling ="https://"+ingress.spec.rules[0]["host"]
 }



  }
   


    }
      }




 stage('Gatling test') {
   steps{
     script{


   def gatling_pipeline = load "pipeline-gatling.groovy"
           
bat "IF not exist gatling (mkdir gatling)"
def pipelineGatling = load gatlingConf
dir("gatling") {
pipelineGatling.start("${url_for_gatling}")
}


     }
   
   }

 }

   /*

     stage('Display Gatling results') {
   steps{

     script{

def folderName=""

dir("gatling/target/gatling"){

folderName= bat(script: "type lastRun.txt", returnStdout: true)
folderName=folderName.split(" ")[2].replace(" ","").replace("\n","").trim()

}


dir("gatling/"){
def dockerf= readFile "Dockerfile"

dockerf=dockerf.replace("folder","${folderName}")

bat "del Dockerfile"
writeFile file: 'Dockerfile', text: dockerf


 bat 'docker build -t gatlingresults-nginx . && docker tag gatlingresults-nginx de.icr.io/devops-tools/gatlingresults-nginx'


}

            dir("C:/Program Files/IBM/Cloud/bin"){
             bat label: 'Login to ibmcloud', script: '''ibmcloud.exe login -u %IBM_ACCESS_KEY_ID% -p %IBM_SECRET_ACCESS_KEY% -r eu-de ''' 
           bat label: 'Login to ibm cr', script: '''ibmcloud.exe  cr login '''
           bat label: 'Configuring kubernetes', script: '''ibmcloud.exe ks cluster config -c c7pb9mkf09cf7vh8tmu0
 '''}
dir("gatling/"){
            bat "docker push de.icr.io/devops-tools/gatlingresults-nginx"
            bat 'kubectl apply -f deployment.yaml --namespace=develop'
            bat 'kubectl apply -f service.yaml --namespace=develop'
            bat 'kubectl apply -f ingress.yaml --namespace=develop'

}  
                
      


     }


     }
   


 }*/
 


        }
          }
