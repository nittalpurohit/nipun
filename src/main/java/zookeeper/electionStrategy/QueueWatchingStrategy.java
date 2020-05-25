package zookeeper.electionStrategy;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;

public class QueueWatchingStrategy implements LeaderElectionStrategy {

    private ZooKeeper currentZookeeperInstance;
    public String currentZookeeperNodeName = null;
    private String ELECTION_NAMESPACE = "/election";

    public QueueWatchingStrategy(ZooKeeper zooKeeper) {
        this.currentZookeeperInstance = zooKeeper;
    }

    @Override
    public void onLeaderElect() throws KeeperException, InterruptedException {
        Stat predecessorStat = null;
        String predecessorName = "";

        while(predecessorStat==null)
        {
            List<String> children = currentZookeeperInstance.getChildren(ELECTION_NAMESPACE,true);
            Collections.sort(children);
            String leader = children.get(0);

            if(currentZookeeperNodeName.equals(leader)){
                System.out.println("yayy! i am the leader");
                return;
            }
            else {
                int predecessorIndex = Collections.binarySearch(children, currentZookeeperNodeName)-1;
                predecessorName = children.get(predecessorIndex);
                predecessorStat = currentZookeeperInstance.exists(ELECTION_NAMESPACE+"/"+predecessorName, true);
            }
        }
        System.out.println("I am ("+currentZookeeperNodeName+") watching my predecessor ("+predecessorName+") node.");
    }

    @Override
    public void onPollAgain() throws KeeperException, InterruptedException {
        onLeaderElect();
    }

    @Override
    public void onRegisterNode() throws KeeperException, InterruptedException {
        String zondePrefix = ELECTION_NAMESPACE+"/c_";
        String znodeFullPath = null;
        znodeFullPath = currentZookeeperInstance.create(zondePrefix, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("zonde full path"+znodeFullPath);
        this.currentZookeeperNodeName = znodeFullPath.replace(ELECTION_NAMESPACE+"/","");
    }
}
