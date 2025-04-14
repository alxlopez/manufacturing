package com.mes.config;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/*
 * Drools setUp
 */
@Configuration
public class DroolsConfiguration {
    @Bean
    public KieContainer kieContainer() throws IOException {
        KieServices ks = KieServices.Factory.get();
        return ks.getKieClasspathContainer();
    }
}
