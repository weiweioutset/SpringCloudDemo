package com.cloud.demo.idgenerator.dao;

import com.cloud.demo.idgenerator.dao.entity.TinyIdToken;

import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/6/18 下午6:47
 * @Version 1.0
 * @Desc
 */
public interface TinyIdTokenDAO {
    /**
     * 查询db中所有的token信息
     * @return
     */
    List<TinyIdToken> selectAll();
}
