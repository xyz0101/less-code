package com.jenkin.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: jenkin
 * @since: 2019/4/18 20:39
 * @history: 1.2019/4/18 created by jenkin
 */
@Component
public class Redis {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    private Logger logger = LoggerFactory.getLogger(Redis.class);
    // =============================common 公用部分============================


    /**
     * 反序列化
     * @param bytes
     * @return
     */
    public Object deserialize(byte[] bytes){
        Object deserialize = redisTemplate.getValueSerializer().deserialize(bytes);
        return deserialize;
    }

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public <T> T  executeLua(RedisScript<T>  redisScript,List<String> keys,Object... args){
         return  redisTemplate.execute(redisScript,keys,args);

    }

    /**
     * 根据key的表达式删除key
     * @param patten
     * @return
     */
    public Long deleteKeysByPatten(String patten){
        Set<String> keys = redisTemplate.keys(patten);
       // System.out.println("包含表达式："+patten+" 的key==>"+keys);
        return redisTemplate.delete( keys) ;
    }

    /**
     * 根据表达式获取符合条件的key
     * @param patten
     * @return
     */
    public Set<String> keys(String patten){
        Set<String> keys = redisTemplate.keys(patten);
        return keys==null?new HashSet<>():keys;
    }


    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }
    // ============================Channel 消息信道类型=============================

    /**
     * redis 发送topic消息
     * @param channel
     * @param message
     */
    public void sendMessage(String channel,String message){
        logger.info("redis发送消息=====》"+message);
        redisTemplate.convertAndSend(channel,message);
    }

    // ============================String 字符串类型=============================


    /**
     * @param key
     * @param value
     * @param expireTime
     * @Title: set
     * @Description: set cache.
     */
    public void set(String key, int value, Date expireTime) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        counter.set(value);
        counter.expireAt(expireTime);
    }

    /**
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @Title: set
     * @Description: set cache.
     */
    public void set(String key, int value, long timeout, TimeUnit unit) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        counter.set(value);
        counter.expire(timeout, unit);
    }

    /**
     * @param key
     * @return
     * @Title: generate
     * @Description: Atomically increments by one the current value.
     */
    public long generate(String key) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        return counter.incrementAndGet();
    }

    /**
     * @param key
     * @return
     * @Title: generate
     * @Description: Atomically increments by one the current value.
     */
    public long generate(String key, Date expireTime) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        counter.expireAt(expireTime);
        return counter.incrementAndGet();
    }

    /**
     * @param key
     * @param increment
     * @return
     * @Title: generate
     * @Description: Atomically adds the given value to the current value.
     */
    public long generate(String key, int increment) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        return counter.addAndGet(increment);
    }

    /**
     * @param key
     * @param increment
     * @param expireTime
     * @return
     * @Title: generate
     * @Description: Atomically adds the given value to the current value.
     */
    public long generate(String key, int increment, Date expireTime) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        counter.expireAt(expireTime);
        return counter.addAndGet(increment);
    }

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    /**
     * 递增
     * @param key 键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @param key 键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    // ================================Map 键对值类型=================================

    /**
     * 管道操作插入
     * @param key
     * @param map
     * @return
     */
    public List<Object>  executeHmsetPipelined(String key,Map map ){
        List<Object> results =redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.hMSet(key.getBytes(),map);
                connection.close();
                return null;
            }
        });
        return results;

    }
    /**
     * 管道操作查询
     * @param key
     * @return
     */
    public List<Object>  executeHmgetPipelined(String key ){
        List<Object> results = null;
        try {
           results = redisTemplate.executePipelined(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    Map<byte[], byte[]> map = connection.hGetAll(key.getBytes());
                    connection.close();
                    return map;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            System.err.println("发生异常，key 为===>"+key);
        }
        return results;
    }

    /**
     * HashGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        try {
            return redisTemplate.opsForHash().get(key, item);
        }catch (Exception e){
            e.printStackTrace();
        }
       return null;
    }

    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     * @param key 键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        try {
            return redisTemplate.opsForHash().hasKey(key, item);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key 键
     * @param item 项
     * @param by 要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     * @param key 键
     * @param item 项
     * @param by 要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    // ============================set 无序不重复集合类型=============================
    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     * @param key 键
     * @param time 时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        if (StringUtils.isEmpty(key)) {
            return 0;
        }
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    // ===============================list 有序集合类型=================================

    /**
     * 获取list缓存的内容
     * @param key 键
     * @param start 开始
     * @param end 结束 0 到 -1代表所有值
     * @return
     */
    public List<?> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean elSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

//========================================================zset 部分=================================




    /**
     * 添加一条数据
     * @param key
     * @param score
     * @param value
     * @return
     */
    public boolean zAdd(String key,double score ,Object value ){
        try {
            Boolean add = redisTemplate.opsForZSet().add(key, value, score);
            return add;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 获取一条数据的索引
     * @param key
     * @param value
     * @return
     */
    public Long zGet(String key,Object value ){
        try {
            return redisTemplate.opsForZSet().rank(key,value);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Double zScore(String key,Object value){
        Double score = redisTemplate.opsForZSet().score(key, value);
        return score==null?0:score;
    }

    /**
     * 移除一部分数据
     * @param key
     * @param startScore
     * @param endScore
     * @return
     */
    public long zRemoveRange(String key,long startScore ,long endScore ){
        try {
            return redisTemplate.opsForZSet().removeRange(key, startScore, endScore);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 分数变化
     * @param key
     * @param score
     * @param value
     * @return
     */
    public double zIncrementMerge(String key,double score ,Object value ){
        try {
            return redisTemplate.opsForZSet().incrementScore(key, value, score);
        }catch (Exception e){
            e.printStackTrace();
        }
        return -999.0;
    }
    /**
     * 获取总数
     * @param key
     * @return
     */
    public long zCount(String key){
        try {
            return redisTemplate.opsForZSet().zCard(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 删除数据
     * @param key
     * @param item
     * @return
     */
    public long zDeleteByItem(String key,Object item){
        try {
            return redisTemplate.opsForZSet().remove(key, item);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 分页获取数据，从高到低排序
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> zGetByPage(String key,int start,int end){

        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet()
                .reverseRangeWithScores(key, start, end);
        return typedTuples;

    }
    /**
     * 分页获取数据，从低到高排序
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> zGetByPageAsc(String key,int start,int end){

        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet()
                .rangeWithScores(key, start, end);
        return typedTuples;

    }
    /**
     * 获取数据，从高到低排序
     * @param key
     * @param cousrName
     * @return
     */
    public Cursor<ZSetOperations.TypedTuple<Object>> zScan(String key,long count,String cousrName){
        ScanOptions tempSet = ScanOptions
                .scanOptions()
                .count(count)
                .match(cousrName)
                .build();
        Cursor<ZSetOperations.TypedTuple<Object>> scan = redisTemplate.opsForZSet().scan(key, tempSet);
        return scan;

    }

    /**
     * 获取特定key的键值对数据
     * @param key
     * @return
     */
    public Cursor<ZSetOperations.TypedTuple<Object>> zScanByKey(String key){
        Cursor<ZSetOperations.TypedTuple<Object>> scan = redisTemplate.opsForZSet().scan(key, ScanOptions.NONE);
        return scan;

    }

    /**
     * 获取特定key的键值对数据,从低到高
     * @param key
     * @return
     */
    public  Set<ZSetOperations.TypedTuple<Object>>  zRangeWithScores(String key,Integer start,Integer end){
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().rangeWithScores(key,start,end);
        return typedTuples;

    }

    /**
     * 获取特定key的键值对数据,从高到低
     * @param key
     * @return
     */
    public  Set<ZSetOperations.TypedTuple<Object>>  zRevRangeWithScores(String key,Integer start,Integer end){
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(key,start,end);
        return typedTuples;

    }


    //---------------------------------------sort------------------------------------------
    public void sort(){
        Map<String,Object> val = new HashMap<>();
        val.put("name","张三");
        val.put("code","100001");
        val.put("org","信息技术部");
        val.put("score",15);
        hmset("test:hash:sort:1",val);
        val.put("name","王五");
        val.put("code","100003");
        val.put("org","采购部");
        val.put("score",2);
        hmset("test:hash:sort:2",val);
        val.put("name","李四");
        val.put("code","100002");
        val.put("org","人力资源部");
        val.put("score",444);
        hmset("test:hash:sort:3",val);
        val.put("name","赵六");
        val.put("code","100006");
        val.put("org","生产部");
        val.put("score",13);
        hmset("test:hash:sort:4",val);
        SortQuery sortQuery = SortQueryBuilder.sort("testlist")// 排序的key
                .by("test:hash:sort:*->org")       //key的正则过滤
//                .noSort()            //不使用正则过滤key
//                .get("test:hash:sort:*->code")            //在value里过滤正则，可以连续写多个get
                .get("test:hash:sort:*->name")            //在value里过滤正则，可以连续写多个get
                .get("test:hash:sort:*->org")            //在value里过滤正则，可以连续写多个get

//                .get("test:hash:sort:*->score")            //在value里过滤正则，可以连续写多个get
//                .limit(2, 2)         //分页，和mysql一样
                .order(SortParameters.Order.ASC)   //正序or倒序
                .alphabetical(true)  //ALPHA修饰符用于对字符串进行排序，false的话只针对数字排序
                .build();
        List sort = redisTemplate.sort(sortQuery);
        logger.debug("排序结果--》"+JSON.toJSONString(sort));
    }


}
