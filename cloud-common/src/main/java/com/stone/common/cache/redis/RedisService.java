package com.stone.common.cache.redis;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisService {

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    boolean expire(String key, long time);

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    long getExpire(String key);

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    boolean hasKey(String key);

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    void del(String... key);

    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    String get(String key);

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    boolean set(String key, String value);

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    boolean set(String key, String value, long time);

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    long incr(String key, long delta);

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    long decr(String key, long delta);

    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    String hGet(String key, String item);

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    Map<Object, Object> hmget(String key);

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    boolean hmset(String key, Map<String, String> map);

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    boolean hmset(String key, Map<String, String> map, long time);

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    boolean hSet(String key, String item, String value);

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    boolean hSet(String key, String item, String value, long time);

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    void hDel(String key, String... item);

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    boolean hHasKey(String key, String item);

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    double hIncr(String key, String item, double by);

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    double hDecr(String key, String item, double by);

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    Set<String> sGet(String key);

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    boolean sIsMember(String key, String value);

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    long sAdd(String key, String... values);

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    long sAddWithTime(String key, long time, String... values);

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    long sGetSetSize(String key);

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    long sMove(String key, String... values);
    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    List<String> lGet(String key, long start, long end);

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    long lGetListSize(String key);

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    String lGetIndex(String key, long index);

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    boolean lSet(String key, String value);

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    boolean lSet(String key, String value, long time);

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    boolean lSet(String key, List<String> value);

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    boolean lSet(String key, List<String> value, long time);

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    boolean lUpdateIndex(String key, long index, String value);

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    long lRemove(String key, long count, String value);

    // ===============================sorted set=================================

    /**
     * 向有序集合添加一个成员的
     * <p>
     * ZADD key score1 member1 [score2 member2]
     */
    boolean zAdd(String key, String member, double score, long time);

    /**
     * ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT]
     * 通过分数返回有序集合指定区间内的成员
     */
    Set<String> zRangeByScore(String key, double minScore, double maxScore);

    /**
     * 分值降序key
     * @param key
     * @param offset
     * @param size
     * @return
     */
    Set<String> zRevRange(String key, int offset, int size);

    /**
     * 获取分值降序集合包含分数
     * @param key
     * @param offset
     * @param size
     * @return
     */
    Set<ZSetOperations.TypedTuple<String>> zRevRangeWithScore(String key, int offset, int size);


    /**
     * 按分值降序区间查询
     * @param key
     * @param maxScore
     * @param minScore
     * @param offset
     * @param size
     * @return
     */
    Set<String> zRevRangeByScoreLimit(String key, double maxScore, double minScore, int offset, int size);


    /**
     * 按分值降序区间查询包含分值
     * @param key
     * @param maxScore
     * @param minScore
     * @param offset
     * @param size
     * @return
     */
    Set<ZSetOperations.TypedTuple<String>> zRevRangeByScoreLimitWithScores(String key, double maxScore, double minScore, int offset, int size);

    /**
     * 获取有序集合成员数
     * @param key
     * @return
     */
    Long zCard(String key);

    /**
     * ZSCORE key member
     * 返回有序集中，成员的分数值
     */
    Double zScore(String key, String member);

    /**
     * ZRANK key member 返回有序集合中指定成员的索引
     */
    Long zRank(String key, String member);

    /**
     * Zscan 迭代有序集合中的元素（包括元素成员和元素分值）
     */
    Cursor<ZSetOperations.TypedTuple<String>> zScan(String key);
}
