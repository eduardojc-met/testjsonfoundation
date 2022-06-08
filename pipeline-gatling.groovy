def start(String url){

     stage('Checkout Source') {
      
      script{
      def git_command=  git "https://github.com/eduardojc-met/gatling.git"
      
      }
       
    }




        stage("Insert project url") {

            
           dir("src/test/java/computerdatabase/"){

                  script{
                 def javaFile = readFile "Java11Simulation.java"
            javaFile.replaceAll("https*", "${url}"+")")
                bat "del Java11Simulation.java"
                 writeFile file: "Java11Simulation.java", text: javaFile

            }
           }

         
           
        }

        stage("Gatling run") {
            script {
                bat 'mvn gatling:test -Dgatling.simulationClass=io.gatling.demo.Java11Simulation'
            }
            gatlingArchive()
            
        }






}



return this