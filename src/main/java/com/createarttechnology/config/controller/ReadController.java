package com.createarttechnology.config.controller;

import com.createarttechnology.config.bean.AppConfig;
import com.createarttechnology.config.bean.BaseTemplate;
import com.createarttechnology.config.service.ReadService;
import com.createarttechnology.config.util.InnerUtil;
import com.createarttechnology.logger.Logger;
import jutil.RequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by lixuhui on 2018/11/12.
 */
@Controller
public class ReadController {

    private static final Logger logger = Logger.getLogger(ReadController.class);

    @Resource
    private ReadService readService;

    @RequestMapping("/")
    public String getListPage(HttpServletRequest request, Model model) {
        List<AppConfig> configList = null;
        try {
            configList = readService.getConfigList();
        } catch (Exception e) {
            logger.error("getConfigList error, e:", e);
        }
        logger.info("configList={}", configList);
        model.addAttribute("configList", configList);
        model.addAttribute("page", new BaseTemplate());
        return "page/list";
    }

    @RequestMapping("/new")
    public String getNewPage(HttpServletRequest request, Model model) {
        return "page/edit";
    }

    @RequestMapping("/edit/{profile}/{configName:.+}")
    public String getEditPage(@PathVariable String profile, @PathVariable String configName, HttpServletRequest request, Model model) {
        logger.info("profile={}, configName={}", profile, configName);
        if (!InnerUtil.isValidProfile(profile)) {
            return "redirect:/";
        }
        int version = RequestUtil.getIntParameter(request, "version", -1);
        AppConfig config = null;
        try {
            config = readService.getConfig(profile, configName, version);
        } catch (Exception e) {
            logger.error("getEditPage error, profile={}, configName={}, version={}, e:", profile, configName, version, e);
        }
        model.addAttribute("config", config);
        model.addAttribute("page", new BaseTemplate());
        return "page/edit";
    }

}
