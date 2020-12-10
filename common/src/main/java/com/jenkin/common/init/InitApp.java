//package com.jenkin.common.init;
//
//import com.gitee.sunchenbin.mybatis.actable.manager.system.SysMysqlCreateTableManager;
//import com.gitee.sunchenbin.mybatis.actable.manager.system.SysMysqlCreateTableManagerImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
///**
// * @author jenkin
// * @className InitApp
// * @description TODO
// * @date 2020/12/10 15:31
// */
//@Component
//@Order(1)
//public class InitApp implements ApplicationListener<ContextRefreshedEvent> {
//    @Resource
//    private SysMysqlCreateTableManager sysMysqlCreateTableManager;
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//        sysMysqlCreateTableManager.createMysqlTable();
//    }
//}
