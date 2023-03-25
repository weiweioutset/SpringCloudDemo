package com.cloud.demo.idgenerator.service.impl;

import com.cloud.demo.idgenerator.dao.TinyIdTokenDAO;
import com.cloud.demo.idgenerator.dao.entity.TinyIdToken;
import com.cloud.demo.idgenerator.service.TinyIdTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author weiwei
 * @Date 2022/6/18 下午7:00
 * @Version 1.0
 * @Desc
 */
@Component
public class TinyIdTokenServiceImpl implements TinyIdTokenService {

    @Autowired
    private TinyIdTokenDAO tinyIdTokenDAO;

    private static Map<String, Set<String>> token2bizTypes = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(TinyIdTokenServiceImpl.class);

    public List<TinyIdToken> queryAll() {
        return tinyIdTokenDAO.selectAll();
    }

    /**
     * 10分钟刷新一次token
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void refresh() {
        logger.info("refresh token begin");
        init();
    }

    @PostConstruct
    private synchronized void init() {
        logger.info("tinyId token init begin");
        List<TinyIdToken> list = queryAll();
        token2bizTypes = convert2Map(list);
        logger.info("tinyId token init success, token size:{}", list == null ? 0 : list.size());
    }

    @Override
    public boolean canVisit(String bizType, String token) {
        if (StringUtils.isEmpty(bizType) || StringUtils.isEmpty(token)) {
            return false;
        }
        Set<String> bizTypes = token2bizTypes.get(token);
        return (bizTypes != null && bizTypes.contains(bizType));
    }

    public Map<String, Set<String>> convert2Map(List<TinyIdToken> list) {
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream()
                .collect(Collectors.groupingBy(TinyIdToken::getToken,
                        Collectors.mapping(TinyIdToken::getBizType, Collectors.toSet())));
    }

}

