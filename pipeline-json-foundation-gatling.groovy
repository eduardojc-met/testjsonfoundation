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
echo json.toString()+"\nf"
echo json[0].toString()
/*
json.each { myData -
 
 def nameActual=myData.projectName.toString()

//se trata de cargar desde aqui el del pipeline y por argumentos se le pasa el nombre del de gatling
            
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
