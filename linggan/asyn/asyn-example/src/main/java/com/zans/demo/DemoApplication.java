package com.zans.demo;

import com.github.sonus21.rqueue.config.SimpleRqueueListenerContainerFactory;
import com.github.sonus21.rqueue.utils.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {


    @Value("${workers.count:3}")
    private int workersCount;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

    }


    @Bean
    public SimpleRqueueListenerContainerFactory simpleRqueueListenerContainerFactory() {
        SimpleRqueueListenerContainerFactory simpleRqueueListenerContainerFactory =
                new SimpleRqueueListenerContainerFactory();
        simpleRqueueListenerContainerFactory.setMaxNumWorkers(workersCount);
        simpleRqueueListenerContainerFactory.setPollingInterval(Constants.ONE_MILLI);
        return simpleRqueueListenerContainerFactory;
    }
}
