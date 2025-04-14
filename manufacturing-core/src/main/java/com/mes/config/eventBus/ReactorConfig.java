package com.mes.config.eventBus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.Environment;
import reactor.bus.EventBus;

@Configuration
public class ReactorConfig {
	@Bean
    Environment env() {
        return Environment.initializeIfEmpty().assignErrorJournal();
    }

    @Bean
    EventBus createEventBus(Environment env) {	
	    EventBus eventBus=	EventBus.create(env, Environment.THREAD_POOL);
	    //eventBus.on($("taskStateChanged"), workflowTaskStateMirror);
	    return eventBus;
    }
}
