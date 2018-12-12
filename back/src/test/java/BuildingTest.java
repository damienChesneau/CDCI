import com.cdci.Messaging;
import org.junit.Test;

public class BuildingTest {

    @Test
    public void doBuild() {
//        new Thread(() -> {
//            Building b = new Building(Paths.get("/root/CDCI/back/Dockerfile"));
//            b.build();
//        }).start();
        new Messaging().sendMessage("trigger-build", "12", "do it");
        System.out.println("hi");
        new Messaging().consumeTopicMessage("current-builds", "12", (s, s2) -> {
            System.out.println(s2);
            if (s2.contains("end")) {
                return;
            }
        });

    }

    @Test
    public void doCoucou() {
        System.out.println("coucou");
    }

}
