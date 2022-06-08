pipeline {
 
  environment {
       IBM_ACCESS_KEY_ID     = credentials('ibmuser')
        IBM_SECRET_ACCESS_KEY = credentials('ibmkey')
        //project=credentials('project-name')
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
def gatlingConf=json[1].stepsFile.toString()

bat "dir"
def pipelineFoundation = load "pipelinefoundation.groovy"
bat 'IF not exist foundation (mkdir foundation)'
dir("foundation") {
pipelineFoundation.start("","","","","","","","")
}
/*
json.each { myData -
 
 def nameActual=myData.projectName.toString()


            
            def archivoconf=myData.stepsFile.toString()

def pipelineB = load archivoconf
           
bat IF not exist ${nameActual} (mkdir ${nameActual})

dir(${nameActual}) {
//pipelineB.test(%IBM_ACCESS_KEY_ID%,%IBM_SECRET_ACCESS_KEY%)
}

           
       
  

        }*/
  }
   


    }
      }
        }
          }
