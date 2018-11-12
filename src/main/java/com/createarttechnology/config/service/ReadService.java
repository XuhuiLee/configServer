package com.createarttechnology.config.service;

import com.createarttechnology.config.bean.AppConfig;
import com.createarttechnology.config.util.InnerUtil;
import com.createarttechnology.logger.Logger;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import jutil.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lixuhui on 2018/11/12.
 */
@Service
public class ReadService {

    private static final Logger logger = Logger.getLogger(ReadService.class);

    @Resource
    private ZKService zkService;

    public List<AppConfig> getConfigList() throws Exception {
        List<String> profileList = zkService.getInstance().getChildren().forPath(InnerUtil.getRoot());
        if (CollectionUtils.isEmpty(profileList)) {
            return null;
        }
        List<AppConfig> configList = Lists.newArrayList();
        for (String profile : profileList) {
            List<String> configNameList = zkService.getInstance().getChildren().forPath(InnerUtil.getConfigRoot(profile));
            if (CollectionUtils.isEmpty(configNameList)) {
                continue;
            }
            for (String configName : configNameList) {
                int version = zkService.getInstance().checkExists().forPath(InnerUtil.getConfigFilePath(profile, configName)).getVersion();
                byte[] configContent = zkService.getInstance().getData().forPath(InnerUtil.getConfigFilePath(profile, configName));
                AppConfig config = new AppConfig();
                config.setConfigName(configName);
                config.setConfigContent(InnerUtil.convert(configContent));
                config.setProfile(profile);
                config.setVersion(version);
                configList.add(config);
            }
        }
        Collections.sort(configList, new Comparator<AppConfig>() {
            @Override
            public int compare(AppConfig o1, AppConfig o2) {
                return o1.getConfigName().compareTo(o2.getConfigName());
            }
        });
        return configList;
    }

    public AppConfig getConfig(String profile, String configName, int version) throws Exception {
        Preconditions.checkArgument(StringUtil.isNotEmpty(configName));
        Preconditions.checkArgument(version > -2);

        Stat stat = zkService.getInstance().checkExists().forPath(InnerUtil.getConfigNamePath(profile, configName));
        if (stat == null) {
            throw new InvalidParameterException("configName invalid");
        }
        // 去掉trunk
        int configVersion = stat.getNumChildren() - 1;
        if (version == -1) {
            String configNamePath = InnerUtil.getConfigFilePath(profile, configName);

            byte[] configContent = zkService.getInstance().getData().forPath(configNamePath);

            AppConfig config = new AppConfig();
            config.setConfigName(configName);
            config.setConfigContent(InnerUtil.convert(configContent));
            config.setVersion(configVersion);
            config.setProfile(profile);
            return config;
        } else {
            if (version > configVersion) {
                throw new InvalidParameterException("version invalid");
            }
            List<String> tagList = zkService.getInstance().getChildren().forPath(InnerUtil.getConfigNamePath(profile, configName));
            if (CollectionUtils.isNotEmpty(tagList) && version + 1 < tagList.size()) {
                Collections.sort(tagList, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });
                String curConfigName = tagList.get(version + 1);
                byte[] configContent = zkService.getInstance().getData().forPath(InnerUtil.getConfigTagFilePath(profile, configName, curConfigName));
                AppConfig config = new AppConfig();
                config.setConfigName(configName);
                config.setConfigContent(InnerUtil.convert(configContent));
                config.setProfile(profile);
                config.setVersion(configVersion);
                return config;
            }
        }
        return null;
    }

}
