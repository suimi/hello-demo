package com.suimi.hello.nio2;

import org.apache.coyote.http11.AbstractHttp11JsseProtocol;
import org.apache.coyote.http2.Http2Protocol;
import org.apache.tomcat.util.net.Nio2Channel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TomcatNio2DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TomcatNio2DemoApplication.class, args);
    }

//    @Bean
    //nio2
    public EmbeddedServletContainerFactory embeddedServletContainerFactory() {
        TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory = new TomcatEmbeddedServletContainerFactory();
        tomcatEmbeddedServletContainerFactory.setProtocol("org.apache.coyote.http11.Http11Nio2Protocol");
        tomcatEmbeddedServletContainerFactory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            AbstractHttp11JsseProtocol<Nio2Channel> handler = (AbstractHttp11JsseProtocol) connector
                    .getProtocolHandler();
            handler.setMaxKeepAliveRequests(-1);
            handler.setAcceptorThreadCount(2);
            handler.setMaxHeaderCount(256);
            connector.setRedirectPort(8443);
        });

        return tomcatEmbeddedServletContainerFactory;
    }

    @Bean
    //http2
    public EmbeddedServletContainerCustomizer tomcatCustomizer() {
        return (container) -> {
            if (container instanceof TomcatEmbeddedServletContainerFactory) {
                ((TomcatEmbeddedServletContainerFactory) container).addConnectorCustomizers((connector) -> {
                    connector.addUpgradeProtocol(new Http2Protocol());
                });
            }
        };
    }


}
