package com.cloud.demo.annotation;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import com.cloud.demo.utils.DBContextHolder;

/**
 * @Author weiwei
 * @Date 2022/5/12 下午11:01
 * @Version 1.0
 * @Desc
 */
@Aspect
@Component
public class DataSourceAop {
    /**
     * 读切面
     * 表示service包下面的以select、get、list等开头的接口，默认走从库
     * 注解优先级大于方法名
     */
    @Pointcut("@annotation(com.cloud.demo.annotation.SlaveDB) " +
            "|| execution(* com.cloud.demo.service..*.select*(..)) " +
            "|| execution(* com.cloud.demo.service..*.list*(..)) " +
            "|| execution(* com.cloud.demo.service..*.export*(..)) " +
            "|| execution(* com.cloud.demo.service..*.get*(..))")
    public void readPointcut() {
    }

    /**
     * 写切面
     * 表示service包下面的以insert、add、update等开头的接口，默认走主库
     * 注解优先级大于方法名
     */
    @Pointcut("@annotation(com.cloud.demo.annotation.MasterDB) " +
            "|| execution(* com.cloud.demo.service..*.insert*(..)) " +
            "|| execution(* com.cloud.demo.service..*.add*(..)) " +
            "|| execution(* com.cloud.demo.service..*.update*(..)) " +
            "|| execution(* com.cloud.demo.service..*.edit*(..)) " +
            "|| execution(* com.cloud.demo.service..*.register*(..)) " +
            "|| execution(* com.cloud.demo.service..*.delete*(..)) " +
            "|| execution(* com.cloud.demo.service..*.remove*(..))")
    public void writePointcut() {
    }

    /**
     * Before方法，设置ThreadLocal里的数据源变量为slave
     */
    @Before("readPointcut()")
    public void read() {
        DBContextHolder.slave();
    }

    /**
     * Before方法，设置ThreadLocal里的数据源变量为master
     */
    @Before(("writePointcut()"))
    public void write() {
        DBContextHolder.master();
    }

    /**
     * After方法，恢复默认数据库
     */
    @After(("readPointcut() || writePointcut()"))
    public void reset() {
        DBContextHolder.reset();
    }
}
