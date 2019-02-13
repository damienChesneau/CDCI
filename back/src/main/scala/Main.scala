import com.cdci.database.PersistanceService

import scala.collection.JavaConverters._


object HelloWorld {
  def main(args: Array[String]): Unit = {
    val builds = PersistanceService.newInstance().getAllBuilds.asScala
    builds.foreach(x => println(x.getProject))

    println("Change project name of path:")
    val s = readLine()
    println("To:")
    val name = readLine()

    PersistanceService.newInstance().updateProjectNameByProjectPath(s, name)

    System.exit(0)
  }
}