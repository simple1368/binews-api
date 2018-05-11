package ksd.binews.common;

import java.util.concurrent.CountDownLatch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisMessageConfig {

	@Bean
	public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter){
		
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, new PatternTopic("log"));
		
		return container;
	}
	
	@Bean
	public MessageListenerAdapter listenerAdapter(Receiver receiver){
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}
	
	@Bean
    Receiver receiver(CountDownLatch latch) {
        return new Receiver(latch);
    }

    @Bean
    public CountDownLatch latch() {
        return new CountDownLatch(1);
    }
}
