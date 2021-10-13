package com.test.TestAPIAF.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableAspectJAutoProxy
public class SpringFoxConfig {                                    
    @Bean
    public Docket api() { 
    	return new Docket(DocumentationType.SWAGGER_2)
    			.useDefaultResponseMessages(false)
    			.select()                                  
    			.apis(RequestHandlerSelectors.any())              
    			.paths(PathSelectors.any())
    			.build();                                           
    }
    
}
