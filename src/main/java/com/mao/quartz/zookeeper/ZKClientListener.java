package com.mao.quartz.zookeeper;

import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author bigdope
 * @create 2019-12-31
 **/
public class ZKClientListener implements LeaderLatchListener {

//    private static Log log = LogFactory.getLog(ZKClientListener.class);
    private static Logger log = LoggerFactory.getLogger(ZKClientListener.class);

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

    @Override
    public void isLeader() {
        log.info(simpleDateFormat.format(new Date()) + "当前服务已变为leader，将从事消费业务");
        ZKClientInfo.isLeader = true;

    }

    @Override
    public void notLeader() {
        log.info(simpleDateFormat.format(new Date()) + "当前服务已退出leader，不再从事消费业务");
        ZKClientInfo.isLeader = false;

    }


}
