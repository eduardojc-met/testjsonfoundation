
def start(String IBM_ACCESS_KEY_ID,String IBM_SECRET_ACCESS_KEY, String git_commit,String front_image_push_id,String back_image_push_id,String url_for_gatling,String timestamp_front, String  timestamp_back){

 
 

 

 
    stage('Checkout Source') {
      
      
           script{
      def git_command=  git 'https://github.com/eduardojc-met/testgatling.git'
      
           git_commit= git_command["GIT_COMMIT"]
           
          
      }
    
      
    }
 
 
   stage('build image') {
       
      
        script{
            /*
        def packageJSON = readJSON file: 'package.json'
        def packageJSONVersion = packageJSON.version
        def appname=readMavenPom().getArtifactId()
        def now = new Date()
        timestamp_front = now.format("yyMMdd.hhmmss", TimeZone.getTimeZone("GMT+8"))
           bat 'docker build -t edujc/frontnginxjunto:'+"${packageJSONVersion}"+' -f src/main/docker/frontend/Dockerfile . && docker tag edujc/frontnginxjunto:'+"${packageJSONVersion}"+' de.icr.io/devops-tools/'+"${appname}"+'-fe:'+"${packageJSONVersion}"+"_"+"${timestamp_front}"
           bat 'mvn clean package -DskipTests -Pprod,tls '
           timestamp_back = now.format("yyMMdd.hhmmss", TimeZone.getTimeZone("GMT+8"))
           bat 'docker build -t edujc/backjunto:'+readMavenPom().getVersion()+' -f src/main/docker/backend/Dockerfile . && docker tag edujc/backjunto:'+readMavenPom().getVersion()+' de.icr.io/devops-tools/'+"${appname}"+'-bff:'+readMavenPom().getVersion()+"_"+"${timestamp_back}"
        */
        
        
        
        
        
        echo git_commit
        echo timestamp_back}
 
        
        
     
      }
    
 
  /*  stage ('push images') {
        
        script{
             def packageJSON = readJSON file: 'package.json'
        def packageJSONVersion = packageJSON.version 
        def pomVersion = readMavenPom().getVersion()
        def appname=readMavenPom().getArtifactId()
            dir("C:/Program Files/IBM/Cloud/bin"){
                
               
            bat label: 'Login to ibmcloud', script: '''ibmcloud.exe login -u %IBM_ACCESS_KEY_ID% -p %IBM_SECRET_ACCESS_KEY% -r eu-de ''' 
            bat label: 'Install ibmcloud ks plugin', script: '''echo n | ibmcloud.exe plugin install container-service ''' 
            bat label: 'Install ibmcloud cr plugin', script: '''echo n | ibmcloud.exe  plugin install container-registry ''' 
            bat label: 'Login to ibm cr', script: '''ibmcloud.exe  cr login '''
            bat label: 'Configuring kubernetes', script: '''ibmcloud.exe ks cluster config -c c7pb9mkf09cf7vh8tmu0
 '''

            bat 'docker push de.icr.io/devops-tools/'+"${appname}"+'-fe:'+"${packageJSONVersion}"+"_"+"${timestamp_front}"
            bat 'docker push de.icr.io/devops-tools/'+"${appname}"+'-bff:'+"${pomVersion}"+"_"+"${timestamp_back}"
            
            def full_id_front = bat(script:"docker inspect --format={{.RepoDigests}} de.icr.io/devops-tools/"+"${appname}"+'-fe:'+"${packageJSONVersion}"+"_"+"${timestamp_front}", returnStdout: true) 
def id_arr_front=full_id_front.toString().split('sha256:')
front_image_push_id = id_arr_front[1].toString().replace("de.icr.io/devops-tools/"+"${appname}"+'-fe',"").replace("'","").replace("\n","").replace("]","")

def full_id_back = bat(script:"docker inspect --format={{.RepoDigests}} de.icr.io/devops-tools/"+"${appname}"+'-bff:'+"${pomVersion}"+"_"+"${timestamp_back}", returnStdout: true) 
def id_arr_back=full_id_back.toString().split('sha256:')
back_image_push_id = id_arr_back[1].toString().replace("de.icr.io/devops-tools/"+"${appname}"+'-bff:',"").replace("'","").replace("\n","").replace("]","")
           
    
            }
                    
         
        }
        
  
    } */
 


  /*  stage('Deploying App to Kubernetes') {
      
        script {

	
            def appname=readMavenPom().getArtifactId()
            
            def packageJSON = readJSON file: 'package.json'
        def packageJSONVersion = packageJSON.version 
        def pomVersion = readMavenPom().getVersion()
             dir("src/main/docker/backend") {
            
              def datas = readYaml file:"back.yaml"
               datas[0].metadata.labels=['io.kompose.service': "${appname}"+'-bff']
               
               
                 datas[0].metadata.annotations["last-image-push-id"]=back_image_push_id
          
       
                datas[0].metadata.annotations["last-commit-sha"]=git_commit

               
               
               datas[0].metadata["name"]="${appname}"+'-bff'
               datas[0].spec.selector.matchLabels=['io.kompose.service': "${appname}"+'-bff']
               datas[0].spec.template.spec.containers[0]["name"]="${appname}"+'-bff'
               datas[0].spec.template.spec.containers[0]["image"]='de.icr.io/devops-tools/'+"${appname}"+'-bff:'+"${pomVersion}"+"_"+"${timestamp_back}"

               
                datas[0].spec.template.metadata.labels=['io.kompose.service': "${appname}"+'-bff']
              
              
              datas[1].metadata.labels=['io.kompose.service': "${appname}"+'-bff']
              datas[1].metadata["name"]="${appname}"+'-bff'
               datas[1].spec.selector=['io.kompose.service': "${appname}"+'-bff']
              
                bat 'del back.yaml'
                writeYaml file: 'back.yaml', data: datas[0]
                bat 'del serviceback.yaml > nul'
                writeYaml file: 'serviceback.yaml', data: datas[1]
               
         
             }
            
            dir("src/main/docker/frontend") {
            
              def datas = readYaml file:"front.yaml"
               datas[0].metadata.labels=['io.kompose.service': "${appname}"+'-fe']
               
               
               
               datas[0].metadata["name"]="${appname}"+'-fe'
               datas[0].spec.selector.matchLabels=['io.kompose.service': "${appname}"+'-fe']
              
                 
                 
                 datas[0].metadata.annotations["last-image-push-id"]=front_image_push_id
          
       
                datas[0].metadata.annotations["last-commit-sha"]=git_commit

               datas[0].spec.template.metadata.labels=['io.kompose.service': "${appname}"+'-fe']
               datas[0].spec.template.spec.containers[0]["name"]="${appname}"+'-fe'
               // datas[0].spec.template.spec.containers[0]["image"]='de.icr.io/devops-tools/'+"${appname}"+'-fe:'+"${packageJSONVersion}"
              
               datas[0].spec.template.spec.containers[0]["image"]='de.icr.io/devops-tools/'+"${appname}"+'-fe:'+"${packageJSONVersion}"+"_"+"${timestamp_front}"
              datas[1].metadata.labels=['io.kompose.service': "${appname}"+'-fe']
              datas[1].metadata["name"]="${appname}"+'-fe'
               datas[1].spec.selector=['io.kompose.service': "${appname}"+'-fe']
              
                   bat 'del front.yaml'
                writeYaml file: 'front.yaml', data: datas[0]
                bat 'del servicefront.yaml > nul'
               writeYaml file: 'servicefront.yaml', data: datas[1]

		def ingress = readYaml file:"ingress.yaml"
                	ingress.metadata["name"]= "${appname}"+'-fe'

                  url_for_gatling ="https://"+ingress.spec.rules[0]["host"]
              echo "gatlng:        "+"${url_for_gatling}"
          		bat 'del ingress.yaml > nul'
                writeYaml file: 'ingress.yaml', data: ingress
         
             }
           





            dir("C:/Program Files/IBM/Cloud/bin"){
             bat label: 'Login to ibmcloud', script: '''ibmcloud.exe login -u %IBM_ACCESS_KEY_ID% -p %IBM_SECRET_ACCESS_KEY% -r eu-de ''' 
           bat label: 'Login to ibm cr', script: '''ibmcloud.exe  cr login '''
           bat label: 'Configuring kubernetes', script: '''ibmcloud.exe ks cluster config -c c7pb9mkf09cf7vh8tmu0
 '''}
            
                    dir("src/main/docker/backend") {
            bat 'kubectl apply -f back.yaml --namespace=develop'
            bat 'kubectl apply -f serviceback.yaml --namespace=develop'
         
                }
            dir("src/main/docker/frontend") {
            bat 'kubectl apply -f front.yaml --namespace=develop'
            bat 'kubectl apply -f servicefront.yaml --namespace=develop'
	    bat 'kubectl apply -f ingress.yaml --namespace=develop'
         
                }
      
        }
      
    }*/


         stage('Checking pods status') {
   

     script{
       def appname=readMavenPom().getArtifactId()
frontend_status=""
backend_status=""
for(int i = 0; i < 6; i++){
     
 if(frontend_status.contains("Running") && backend_status.contains("Running")){
    
     break
     
 }else{ 
     
                    frontend_status= bat(script: 'kubectl get pods --namespace=develop --selector=io.kompose.service='+"${appname}"+'-fe --no-headers -o custom-columns=":status.phase"' ,returnStdout: true).toString().split('"')[2] 
               backend_status= bat(script: 'kubectl get pods --namespace=develop --selector=io.kompose.service='+"${appname}"+'-bff --no-headers -o custom-columns=":status.phase"' ,returnStdout: true).toString().split('"')[2] 
               
     if(i==5){
        
         error('Los pods no están ejecutandose correctamente, cancelando gatling...')

     }
     
 }

}

  
   


     }
   


 }
/*
 stage('Gatling test') {
   

     script{


   def gatling_pipeline = load "pipeline-gatling.groovy"
           
bat "IF not exist gatling (mkdir gatling)"

dir("gatling") {
gatling_pipeline.start("${url_for_gatling}")
}


     }
   


 }

   

     stage('Display Gatling results') {
   

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
 
  */
 

}