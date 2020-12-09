package com.jenkin.menuservice.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    @Value("${redis.host:192.168.3.47}")
    private String host;

    @Value("${redis.port:6379}")
    private int port;

    @Value("${redis.password:}")
    private String password;

    @Value("${redis.database:0}")
    private int database;

    @Value("${redis.timeout:30000}")
    private int timeout;


    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host+":"+port);
//        redisManager.setPassword(password);// 访问密码
        redisManager.setDatabase(database);// 默认数据库
        redisManager.setTimeout(timeout);// 过期时间
        return redisManager;
    }

    /**
     * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件 MemorySessionDAO 直接在内存中进行会话维护
     * EnterpriseCacheSessionDAO
     * 提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     *
     * @return
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDao = new RedisSessionDAO();
        redisSessionDao.setKeyPrefix("shiro-session");//配置session前缀
        redisSessionDao.setSessionIdGenerator(sessionIdGenerator());
        redisSessionDao.setRedisManager(redisManager());
        // session在redis中的保存时间,最好大于session会话超时时间
        redisSessionDao.setExpire(timeout);
        return redisSessionDao;
    }
    //将自己的验证方式加入容器
    @Bean
    public MyShiroRealm myShiroRealm() {
        return new MyShiroRealm();
    }
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }
    //权限管理，配置主要是Realm的管理认证
    @Bean
    public SecurityManager securityManager(MyShiroRealm myShiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm);
//        securityManager.setRememberMeManager(rememberMeManager());
//        securityManager.setCacheManager(ehCacheManager());// 将缓存管理交给ehCache
        securityManager.setSessionManager(sessionManager());//将session管理交给reids
        return securityManager;
    }
//
//    public EhCacheManager ehCacheManager(){
//        return new EhCacheManager();
//    }


    /**
     * 单机环境，session交给shiro管理
     */
    @Bean
    public DefaultWebSessionManager sessionManager( ){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());

        // 全局会话超时时间（单位毫秒），默认30分钟 暂时设置为10秒钟 用来测试
        sessionManager.setGlobalSessionTimeout(120000);//单位毫秒
        // 是否开启删除无效的session对象 默认为true
        sessionManager.setDeleteInvalidSessions(true);
        // 是否开启定时调度器进行检测过期session 默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true);
        // 设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
        // 设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler
        // 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        sessionManager.setSessionValidationInterval(3600000);//单位毫秒
        // 取消url 后面的 JSESSIONID，设置为false为取消
        sessionManager.setSessionIdUrlRewritingEnabled(false);

        return sessionManager;
    }

    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String,String> map = new HashMap<String, String>();
        //登出
        map.put("/test/logout","logout");
        //登录
        map.put("/test/login","anon");
        map.put("/test/register","anon");
        //对所有用户认证
        map.put("/**","authc");
        //登录
        shiroFilterFactoryBean.setLoginUrl("/test/needLogin");


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
