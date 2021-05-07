package com.jenkin.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.jenkin.common.shiro.utils.ShiroUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.mapstruct.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * mybatis-plus分页插件 + 乐观锁插件
     */
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor paginationInterceptor = new MybatisPlusInterceptor();
        List<InnerInterceptor> sqlParserList = new ArrayList<>();
        // 攻击 SQL 阻断解析器、加入解析链
        sqlParserList.add(new BlockAttackInnerInterceptor());
        paginationInterceptor.setInterceptors(sqlParserList);
        //乐观锁插件
        paginationInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());


        // v3.4.0版本以上需要添加数据库类型，分页才生效
        paginationInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return paginationInterceptor;
    }

    /**
     * 如果是对全表的删除或更新操作，就会终止该操作
     *
     * @return
     */
    @Bean
    public BlockAttackInnerInterceptor sqlExplainInterceptor() {
        return new BlockAttackInnerInterceptor();
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
