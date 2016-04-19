package domi.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;



import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisHelper {
    protected static final String REDIS_HOST = "localhost";
    protected static final int REDIS_PORT = 6379;
    public final Set<Jedis> connectionList = new HashSet<Jedis>();
    public final JedisPool pool;

    
    private JedisHelper() {
    	
    //	JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
	//	JedisPool pool = new JedisPool(jedisPoolConfig, "localhost",6379);
	//	Jedis jedis = pool.getResource();
	//	jedis.set("foo", "bar");
	//	String value = jedis.get("foo");
		
    //	System.out.println("jedishelper start");
   // 	GenericObjectPool config = new GenericObjectPool();
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();

        config.setMaxTotal(20);
        config.setBlockWhenExhausted(true);

        this.pool = new JedisPool(config, REDIS_HOST, REDIS_PORT);
        
        Jedis jedis = pool.getResource();
        jedis.set("foo", "bar");
        jedis.set("aa", "bb");
        String value = jedis.get("foo");
        System.out.println(value);

        
    	//	jedis.set("foo", "bar");
    	//	String value = jedis.get("foo");
        System.out.println(this.pool);
    }

   
    private static class LazyHolder {
        @SuppressWarnings("synthetic-access")
        private static final JedisHelper INSTANCE = new JedisHelper();
    }

   
    @SuppressWarnings("synthetic-access")
    public static JedisHelper getInstance() {
        return LazyHolder.INSTANCE;
    }

    final public Jedis getConnection() {
        Jedis jedis = this.pool.getResource();
        this.connectionList.add(jedis);

        return jedis;
    }

   
    final public void returnResource(Jedis jedis) {
        this.pool.returnResource(jedis);
    }

 
    final public void destoryPool() {
        Iterator<Jedis> jedisList = this.connectionList.iterator();
        while (jedisList.hasNext()) {
            Jedis jedis = jedisList.next();
            this.pool.returnResource(jedis);
        }

        this.pool.destroy();
    }
}