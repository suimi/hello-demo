package com.suimi.gracefulshutdown.endpoint.jmx;

import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.endpoint.jmx.EndpointMBean;
import org.springframework.jmx.export.annotation.ManagedOperation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suimi.gracefulshutdown.endpoint.GracefulShutdownEndpoint;

/**
 * Special endpoint wrapper for {@link GracefulShutdownEndpoint}.
 *
 * @author Corentin Azelart
 */
public class GracefulShutdownEndpointMBean extends EndpointMBean {


    /**
     * Create a new {@link GracefulShutdownEndpointMBean} instance.
     * @param beanName the bean name
     * @param endpoint the endpoint to wrap
     * @param objectMapper the {@link ObjectMapper} used to convert the payload
     */
    public GracefulShutdownEndpointMBean(String beanName, Endpoint<?> endpoint, ObjectMapper objectMapper) {
        super(beanName, endpoint, objectMapper);
    }

    /**
     * Expose endpoint.
     * @return an object.
     */
    @ManagedOperation(description = "Graceful shutdown the ApplicationContext")
    public Object shutdown() {
        return convert(getEndpoint().invoke());
    }
}
