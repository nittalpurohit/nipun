package zookeeper.electionStrategy;

import org.apache.zookeeper.KeeperException;

public interface LeaderElectionStrategy {

    void onRegisterNode() throws KeeperException, InterruptedException;
    void onLeaderElect() throws KeeperException, InterruptedException;
    void onPollAgain() throws KeeperException, InterruptedException;
}
