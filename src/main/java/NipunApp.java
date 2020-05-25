import org.apache.zookeeper.KeeperException;
import zookeeper.watchers.NipunWatcher;

import java.io.IOException;

public class NipunApp {

    private static NipunWatcher nipunWatcher;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        nipunWatcher = new NipunWatcher();
        nipunWatcher.registerAndElect();


        NipunApp nipunApp = new NipunApp();
        nipunApp.run();
    }

    public void run() throws InterruptedException {
        synchronized (this){
            this.wait();
        }
    }
}
