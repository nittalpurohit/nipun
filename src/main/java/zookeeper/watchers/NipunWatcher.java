package zookeeper.watchers;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import zookeeper.electionStrategy.LeaderElectionStrategy;
import zookeeper.electionStrategy.QueueWatchingStrategy;

import java.io.IOException;

public class NipunWatcher implements Watcher {

    private LeaderElectionStrategy electionStrategy;
    private static final String ZOOKEEPER_ADDRESS = "localhost:2181";
    private static final int SESSION_TIMEOUT = 3000;
    public ZooKeeper zooKeeper;


    public NipunWatcher() throws IOException {
        zooKeeper = new ZooKeeper(ZOOKEEPER_ADDRESS, SESSION_TIMEOUT, this);
        electionStrategy = new QueueWatchingStrategy(zooKeeper);
    }


    public void registerAndElect() throws KeeperException, InterruptedException {
        electionStrategy.onRegisterNode();
        electionStrategy.onLeaderElect();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case None:
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("connected to zookeeper server.");
                }
                break;
            case NodeDeleted:
                try {
                    electionStrategy.onPollAgain();
                } catch (KeeperException e) {
                } catch (InterruptedException e) {
                }
                break;
            case NodeChildrenChanged:
                System.out.println("children changed!");
                break;
            case NodeCreated:
                System.out.println("Node Created");
            case NodeDataChanged:
                System.out.println("Node data changed");
        }
    }


}
