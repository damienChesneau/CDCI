import com.cdci.annotation.DistribuitedTest
import com.impetus.annovention.listener._
import org.junit.Test

class MethodTestListener extends MethodAnnotationDiscoveryListener {
  private var discoveredValues = List[String]()

  override def discovered(clazz: String, method: String, annotation: String): Unit = {
    this.discoveredValues = (clazz + ":" + method + ":" + annotation) :: this.discoveredValues
  }

  override def supportedAnnotations(): Array[String] = {
    return Array[String](classOf[DistribuitedTest].getName, classOf[Test].getName)
  }

  def getDicoveredValues(): List[String] = {
    return this.discoveredValues
  }
}
