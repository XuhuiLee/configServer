package com.createarttechnology.config.service;

import com.createarttechnology.logger.Logger;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by lixuhui on 2018/11/12.
 */
@Service
public class ZKService {

    private static final Logger logger = Logger.getLogger(ZKService.class);

    private CuratorFramework client;

    @PostConstruct
    public void init() throws Exception {
        String zookeeperConnectionString = "www.createarttechnology.com:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
        client.start();
    }

    @PreDestroy
    public void destroy() throws Exception {
        client.close();
    }

    public CuratorFramework getInstance() {
        return this.client;
    }

}
