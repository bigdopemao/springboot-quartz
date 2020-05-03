package com.mao.quartz.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author bigdope
 * @create 2019-12-30
 **/
public class ZKClient {

    private LeaderLatch leader;

    private CuratorFramework client;

    public ZKClient() {
    }

    public ZKClient(LeaderLatch leader, CuratorFramework client){
        this.client = client;
        this.leader = leader;
    }

    /**
     * 启动客户端
     * @throws Exception
     */
    public void startZKClient() throws Exception {
        client.start();
        leader.start();
    }

    /**
     * 关闭客户端
     * @throws Exception
     */
    public void closeZKClient() throws Exception {
        leader.close();
        client.close();
    }

    /**
     * 判断是否变为领导者
     * @return
     */
    public boolean hasLeadership(){
        return leader.hasLeadership() && ZKClientInfo.isLeader;
    }


    public LeaderLatch getLeader() {
        return leader;
    }

    public void setLeader(LeaderLatch leader) {
        this.leader = leader;
    }

    public CuratorFramework getClient() {
        return client;
    }

    public void setClient(CuratorFramework client) {
        this.client = client;
    }

    public static void main(String[] args) throws Exception {
        CuratorFramework client =
                CuratorFrameworkFactory.builder()
                        .connectString("127.0.0.1:2181")
                        .retryPolicy(new ExponentialBackoffRetry(5000, 3))
                        .connectionTimeoutMs(5000)
                        .build();

        LeaderLatch leaderLatch = new LeaderLatch(client, "/leaderLatch", "client1", LeaderLatch.CloseMode.NOTIFY_LEADER);
        ZKClientListener zkClientListener = new ZKClientListener();
        leaderLatch.addListener(zkClientListener);


        ZKClient zkClient = new ZKClient(leaderLatch,client);
        zkClient.startZKClient();
        Thread.sleep(5000);

        int i = 0;
        while (i<15){
            //System.out.println("hasLeadership = "+zkClient.hasLeadership());
            Thread.sleep(1000);
            i++;
        }
        zkClient.closeZKClient();
        Thread.sleep(5000);
    }

    public void startClient()  throws Exception  {
        client.start();
    }

    public void closeClient()  throws Exception  {
        client.close();
    }

    public void startLeaderLatch() throws Exception {
        leader.start();
    }

    public void closeLeaderLatch() throws Exception {
        leader.close();
    }
}
