package com.createarttechnology.config.service;

import com.createarttechnology.common.BaseResp;
import com.createarttechnology.common.ErrorInfo;
import com.createarttechnology.config.bean.EditConfigReq;
import com.createarttechnology.config.util.InnerUtil;
import com.createarttechnology.logger.Logger;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import jutil.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Created by lixuhui on 2018/11/12.
 */
@Service
public class WriteService {

    private static final Logger logger = Logger.getLogger(WriteService.class);

    @Resource
    private ZKService zkService;

    /**
     * 保存config
     * 1.每次使用编号模式记录修改历史
     * 2.每次创建/更新trunk版本，客户端可同步
     */
    public BaseResp saveConfig(EditConfigReq req) throws Exception {
        Preconditions.checkNotNull(req);
        Preconditions.checkArgument(StringUtil.isNotEmpty(req.getProfiles()));
        Preconditions.checkArgument(StringUtil.isNotEmpty(req.getName()));

        Set<String> profileList = Sets.newHashSet(Splitter.on(CharMatcher.anyOf(",:|")).omitEmptyStrings().trimResults().split(req.getProfiles()));
        String configName = req.getName();
        String configContent = req.getContent();
        if (configContent == null) {
            configContent = "";
        }

        if (CollectionUtils.isEmpty(profileList)) {
            return new BaseResp(ErrorInfo.INVALID_PARAMS);
        }
        // 每个域都处理
        for (String profile : profileList) {
            if (!InnerUtil.isValidProfile(profile)) {
                continue;
            }
            String configNamePath = InnerUtil.getConfigNamePath(profile, configName);
            Stat stat = zkService.getInstance().checkExists().forPath(configNamePath);
            boolean exist = stat != null && stat.getNumChildren() > 0;
            if (!exist) {
                // parent path 不存在，创建
                zkService.getInstance().create().withMode(CreateMode.PERSISTENT).forPath(configNamePath);
            }
            String configFilePath = InnerUtil.getConfigFilePath(profile, configName);
            // 创建历史记录
            zkService.getInstance().create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(configFilePath, InnerUtil.convert(configContent));
            if (exist) {
                // 更新trunk节点
                zkService.getInstance().setData().forPath(configFilePath, InnerUtil.convert(configContent));
            } else {
                // 创建trunk节点
                zkService.getInstance().create().withMode(CreateMode.PERSISTENT).forPath(configFilePath, InnerUtil.convert(configContent));
            }
        }
        return new BaseResp(ErrorInfo.SUCCESS);
    }

}
