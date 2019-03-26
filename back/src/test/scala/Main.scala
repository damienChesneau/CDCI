
import com.cdci.database.PersistenceService
import com.impetus.annovention.ClasspathDiscoverer

import scala.collection.JavaConverters._


object HelloWorld {
  def main(args: Array[String]): Unit = {
    //    updateProjectName
    val tests = getAllTests()
    println(tests)
    for (test: String <- tests) {
      val splitted = test.split(":")
      this.executeTest(splitted(0), splitted(1))
    }
    System.exit(0)
  }

  private def updateProjectName = {
    val builds = PersistenceService.newInstance().getAllBuilds.asScala
    builds.foreach(x => println(x.getProject))

    println("Change project name of path:")
    val s = readLine()
    println("To:")
    val name = readLine()

    PersistenceService.newInstance.updateProjectNameByProjectPath(s, name)
  }

  def getAllTests(): List[String] = {
    val classpathDiscoverer = new ClasspathDiscoverer
    val discoveryListener = new MethodTestListener
    classpathDiscoverer.addAnnotationListener(discoveryListener)
    classpathDiscoverer.discover(true, true, true, true, true)
    return discoveryListener.getDicoveredValues
  }

  private def executeTest(clazzName: String, methodName: String): Unit = {
    val clazz = Class.forName(clazzName)
    val method = clazz.getDeclaredMethod(methodName)

    val constructors = clazz.getDeclaredConstructors
    println("Invoking: " + clazz + " and method " + method)
    method.invoke(constructors(0).newInstance()) //Invoke default constructor.
  }
}