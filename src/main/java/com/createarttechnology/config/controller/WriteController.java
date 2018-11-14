package com.createarttechnology.config.controller;

import com.createarttechnology.common.BaseResp;
import com.createarttechnology.common.ErrorInfo;
import com.createarttechnology.config.bean.EditConfigReq;
import com.createarttechnology.config.service.WriteService;
import com.createarttechnology.logger.Logger;
import com.createarttechnology.jutil.StringUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by lixuhui on 2018/11/12.
 */
@RestController
public class WriteController {

    private static final Logger logger = Logger.getLogger(WriteController.class);

    @Resource
    private WriteService writeService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BaseResp saveConfig(@RequestBody EditConfigReq req) {
        logger.info("saveConfig, req={}", req);
        if (req == null || StringUtil.isEmpty(req.getProfiles()) || StringUtil.isEmpty(req.getName())) {
            return new BaseResp(ErrorInfo.INVALID_PARAMS);
        }
        try {
            return writeService.saveConfig(req);
        } catch (Exception e) {
            logger.error("saveConfig error, e:", e);
            return new BaseResp(ErrorInfo.ERROR);
        }
    }

}
