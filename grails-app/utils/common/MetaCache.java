package common;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class MetaCache {

	final public static Cache<String, Object> flowCache =  CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(18,TimeUnit.HOURS).build();
	final public static Cache<String, Object> batchCache =  CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(18,TimeUnit.HOURS).build();

	public static Object getMeta(Cache cache, String key){
		return (Object) cache.getIfPresent(key);
	}

	public static void setMeta(Cache cache, String key, Object value){
		cache.put(key, value);
	}

}
