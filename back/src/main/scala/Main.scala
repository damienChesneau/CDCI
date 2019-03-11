
import com.cdci.database.PersistenceService

import scala.collection.JavaConverters._


object HelloWorld {
  def main(args: Array[String]): Unit = {
    val builds = PersistenceService.newInstance().getAllBuilds.asScala
    builds.foreach(x => println(x.getProject))

    println("Change project name of path:")
    val s = readLine()
    println("To:")
    val name = readLine()

    PersistenceService.newInstance().updateProjectNameByProjectPath(s, name)

    System.exit(0)
  }
}