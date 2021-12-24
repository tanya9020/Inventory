package com.inventory.api.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
@Configuration
public class Config {
	
	@Bean
	MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
	return new MongoTransactionManager(dbFactory);
	}
	@Bean
	public RedisCacheConfiguration cacheConfiguration() {
	return RedisCacheConfiguration.defaultCacheConfig()
	.entryTtl(Duration.ofMinutes(60))
	.disableCachingNullValues()
	.serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
	}
	
	
	   @Bean
	   public JSR310Module jsr310Module() {
	     return new JSR310Module();
	   }
}


