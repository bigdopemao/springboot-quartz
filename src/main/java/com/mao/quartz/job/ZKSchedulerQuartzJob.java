package com.mao.quartz.job;

import com.mao.quartz.zookeeper.ZKClient;
import com.mao.quartz.zookeeper.ZKClientListener;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 分布式任务
 * 当其中一个节点挂掉后，大约在30多秒左右（1分钟以内）其他节点也不会成为leader，
 * 即如果此时有任务执行的话，所有节点都不会执行此次定时任务
 * @author bigdope
 * @create 2020-01-07
 **/
public class ZKSchedulerQuartzJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ZKClient zkClient = this.getZKClient();
        int times = 0;
        try {
            zkClient.startZKClient();
            System.out.println(Thread.currentThread().getName() + " ZKClient 开启");
            while (true) {
                if (!zkClient.hasLeadership()) {
                    if (times >= 3) {
                        return;
//                        zkClient.closeZKClient();
                    }
                    System.out.println(Thread.currentThread().getName() + " 当前服务不是leader");
                    Thread.sleep(1000);
                    times++;
                    continue;
                } else {
                    System.out.println(Thread.currentThread().getName() + "  当前服务是leader");
                    break;
                }
            }
            System.out.println(Thread.currentThread().getName() + " 任务开始执行");
            System.out.println("开始: " + System.currentTimeMillis());
            // TODO
            // 业务
            Thread.sleep(5000);

            System.out.println("结束: " + System.currentTimeMillis());
            System.out.println(Thread.currentThread().getName() + " 任务结束执行");
//            zkClient.closeClient();
//            System.out.println(Thread.currentThread().getName() + " ZKClient 关闭");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                zkClient.closeClient();
                System.out.println(Thread.currentThread().getName() + " ZKClient 关闭");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ZKClient getZKClient() {
        CuratorFramework client =
                CuratorFrameworkFactory.builder()
                        .connectString("192.168.0.82:2181")
                        .retryPolicy(new ExponentialBackoffRetry(5000, 3))
                        .connectionTimeoutMs(5000)
                        .build();

        LeaderLatch leaderLatch = new LeaderLatch(client, "/leaderLatch", "client", LeaderLatch.CloseMode.NOTIFY_LEADER);
        ZKClientListener zkClientListener = new ZKClientListener();
        leaderLatch.addListener(zkClientListener);
        ZKClient zkClient = new ZKClient(leaderLatch, client);
        return zkClient;
    }

}
