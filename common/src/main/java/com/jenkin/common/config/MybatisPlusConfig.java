package com.jenkin.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import com.jenkin.common.shiro.utils.ShiroUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jenkin
 * @className MybatisPlusConfig
 * @description 数据源配置
 * @date 2020/6/19 9:56
 */
@Configuration
@EnableTransactionManagement

public class MybatisPlusConfig {

    /**
         * mybatis-plus分页插件
         */
        @Bean
        public PaginationInterceptor paginationInterceptor() {
            PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
            List<ISqlParser> sqlParserList = new ArrayList<>();
            // 攻击 SQL 阻断解析器、加入解析链
            sqlParserList.add(new BlockAttackSqlParser());
            paginationInterceptor.setSqlParserList(sqlParserList);
            //paginationInterceptor.setLocalPage(true);// 开启 PageHelper 的支持
            return paginationInterceptor;
        }


        /**
         * 乐观锁mybatis插件
         */
        @Bean
        public OptimisticLockerInterceptor optimisticLockerInterceptor() {
            return new OptimisticLockerInterceptor();
        }


        /**
         * 如果是对全表的删除或更新操作，就会终止该操作
         *
         * @return
         */
        @Bean
        public SqlExplainInterceptor sqlExplainInterceptor() {
            return new SqlExplainInterceptor();
        }

        @Bean
        public MetaObjectHandler metaObjectHandler(){
            return new MetaObjectHandler() {
                @Override
                public void insertFill(MetaObject metaObject) {
                    this.strictInsertFill(metaObject, "deleteFlag", Integer.class, 0);

                    this.strictInsertFill(metaObject, "creationDate", LocalDateTime.class, LocalDateTime.now());
                    this.strictInsertFill(metaObject, "createdBy", String.class, ShiroUtils.getUserCode());
                    this.strictInsertFill(metaObject, "lastUpdateDate", LocalDateTime.class, LocalDateTime.now());
                    this.strictInsertFill(metaObject, "lastUpdatedBy", String.class, ShiroUtils.getUserCode());
                }

                @Override
                public void updateFill(MetaObject metaObject) {
                    this.strictInsertFill(metaObject, "lastUpdateDate", LocalDateTime.class, LocalDateTime.now());
                    this.strictInsertFill(metaObject, "lastUpdatedBy", String.class, ShiroUtils.getUserCode());
                }
            };
        }
}
