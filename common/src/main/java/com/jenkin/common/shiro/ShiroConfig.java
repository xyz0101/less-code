package com.jenkin.common.shiro;

import com.jenkin.common.shiro.token.MyTokenFilter;
import com.jenkin.common.utils.Redis;
import org.apache.shiro.mgt.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/3 21:23
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Configuration
public class ShiroConfig {
    @Value("${redis.host:mall.jenkin.tech}")
    private String host;

    @Value("${redis.port:9001}")
    private int port;

    @Value("${redis.password:}")
    private String password;

    @Value("${redis.database:0}")
    private int database;

    @Value("${redis.timeout:30000}")
    private int timeout;


//    @Bean
//    public RedisManager redisManager() {
//        RedisManager redisManager = new RedisManager();
//        redisManager.setHost(host+":"+port);
////        redisManager.setPassword(password);// 访问密码
//        redisManager.setDatabase(database);// 默认数据库
//        redisManager.setTimeout(timeout);// 过期时间
//        return redisManager;
//    }
//
//    /**
//     * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件 MemorySessionDAO 直接在内存中进行会话维护
//     * EnterpriseCacheSessionDAO
//     * 提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
//     *
//     * @return
//     */
//    @Bean
//    public RedisSessionDAO redisSessionDAO() {
//        RedisSessionDAO redisSessionDao = new RedisSessionDAO();
//        redisSessionDao.setKeyPrefix("shiro-session");//配置session前缀
//        redisSessionDao.setSessionIdGenerator(sessionIdGenerator());
//        redisSessionDao.setRedisManager(redisManager());
//        // session在redis中的保存时间,最好大于session会话超时时间
//        redisSessionDao.setExpire(timeout);
//        return redisSessionDao;
//    }
    //将自己的验证方式加入容器
    @Bean
    public MyShiroRealm myShiroRealm() {
        return new MyShiroRealm();
    }
    @Bean
    public SubjectFactory subjectFactory(){
        return new StatelessDefaultSubjectFactory();
    }

//    @Bean
//    public SessionIdGenerator sessionIdGenerator() {
//        return new JavaUuidSessionIdGenerator();
//    }
    //权限管理，配置主要是Realm的管理认证
    @Bean
    public SecurityManager securityManager(MyShiroRealm myShiroRealm,SubjectFactory subjectFactory) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm);
        securityManager.setSubjectFactory(subjectFactory);
        ((DefaultSessionStorageEvaluator)((DefaultSubjectDAO)securityManager.getSubjectDAO()).
                getSessionStorageEvaluator()).setSessionStorageEnabled(false);

        return securityManager;
    }
//
//    public EhCacheManager ehCacheManager(){
//        return new EhCacheManager();
//    }

    /**
     * 禁用session, 不保存用户登录状态。保证每次请求都重新认证。
     * 需要注意的是，如果用户代码里调用Subject.getSession()还是可以用session，
     * 如果要完全禁用，要配合下面的noSessionCreation的Filter来实现
     */
//    @Bean
//    protected SessionStorageEvaluator sessionStorageEvaluator(){
//        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
//        sessionStorageEvaluator.setSessionStorageEnabled(false);
//        return sessionStorageEvaluator;
//    }
    /**
     * 单机环境，session交给shiro管理
     */
    @Bean
    public DefaultWebSessionManager sessionManager( ){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();

        sessionManager.setSessionValidationSchedulerEnabled(false);
        //shiro 的session默认放在cookie中 禁用
        sessionManager.setSessionIdCookieEnabled(false);
        //禁用url 重写 url; shiro请求时默认 jsessionId=id
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager, Redis redis) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //设置自己的过滤器
        Map<String, Filter> filterMap = new HashMap<String, Filter>(1);
        shiroFilterFactoryBean.setFilters(filterMap);
        filterMap.put("token", new MyTokenFilter(redis));

        Map<String,String> map = new HashMap<String, String>();
        //登出
        map.put("/user/logout","logout");
        //登录
        map.put("/user/login","anon");
        map.put("/shiro/needLogin","anon");
        map.put("/user/register","anon");
        map.put("/user/getPublicKey","anon");
        map.put("/swagger-ui.html", "anon");
        map.put("/doc.html", "anon");
        map.put("/swagger**/**", "anon");
        map.put("/webjars/**", "anon");
        map.put("/v2/**", "anon");
        //对所有用户认证
        map.put("/**","token");
        //登录
        shiroFilterFactoryBean.setLoginUrl("/shiro/needLogin");


        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    //加入注解的使用，不加入这个注解不生效
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new ShiroAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
