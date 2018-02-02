/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.gracefulshutdown.config;


import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties.Undertow;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.servlet.Servlet;

import com.suimi.gracefulshutdown.endpoint.GracefulShutdownEndpoint;
import com.suimi.gracefulshutdown.health.GracefulHealth;
import com.suimi.gracefulshutdown.properties.GracefulShutdownProperties;
import com.suimi.gracefulshutdown.service.TomcatShutdown;


/**
 * This configuration class will be picked up by Spring Boot's auto configuration capabilities as soon as it's
 * on the classpath.
 */
@Configuration
@ConditionalOnProperty(prefix = "endpoints.shutdown.graceful", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(GracefulShutdownProperties.class)
//@Import(EmbeddedServletContainerAutoConfiguration.BeanPostProcessorsRegistrar.class)
public class GracefulShutdownAutoConfiguration {

    /**
     * Properties.
     */
    @Autowired
    private GracefulShutdownProperties gracefulShutdownProperties;

    /**
     * Configuration for Tomcat.
     */
    @Configuration
    @ConditionalOnClass({Servlet.class, Tomcat.class})
//    @ConditionalOnBean(TomcatEmbeddedServletContainerFactory.class)
    public static class EmbeddedTomcat {
        @Bean
        public TomcatShutdown tomcatShutdown() {
            return new TomcatShutdown();
        }

        /**
         * Customise the tomcat factory.
         *
         * @return an EmbeddedServletContainerCustomizer
         */
        @Bean
        public EmbeddedServletContainerCustomizer tomcatCustomizer() {
            return new EmbeddedServletContainerCustomizer() {
                public void customize(ConfigurableEmbeddedServletContainer container) {
                    if (container instanceof TomcatEmbeddedServletContainerFactory) {
                        ((TomcatEmbeddedServletContainerFactory) container).addConnectorCustomizers(tomcatShutdown());
                    }
                }
            };
        }
    }


    /**
     * Graceful shutdown util.
     *
     * @return the unique tool to perform the graceful shutdown.
     */
    @Bean
    @ConditionalOnMissingBean
    protected GracefulShutdownEndpoint gracefulShutdownEndpoint() {
        return new GracefulShutdownEndpoint(gracefulShutdownProperties.getTimeout(),
                gracefulShutdownProperties.getWait());
    }

    /**
     * Graceful health.
     *
     * @return a graceful health.
     */
    @Bean
    public GracefulHealth gracefulHealth() {
        return new GracefulHealth();
    }

}
