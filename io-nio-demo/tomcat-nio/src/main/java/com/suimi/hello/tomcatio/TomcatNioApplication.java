package com.suimi.hello.tomcatio;

import org.apache.coyote.http11.AbstractHttp11JsseProtocol;
import org.apache.coyote.http2.Http2Protocol;
import org.apache.tomcat.util.net.Nio2Channel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication public class TomcatNioApplication {

    public static void main(String[] args) {
        SpringApplication.run(TomcatNioApplication.class, args);
    }

        @Bean
    //nio2 default is nio1
    public ServletWebServerFactory embeddedServletContainerFactory() {
        TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
        tomcatServletWebServerFactory.setProtocol("org.apache.coyote.http11.Http11Nio2Protocol");
        tomcatServletWebServerFactory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            AbstractHttp11JsseProtocol<Nio2Channel> handler = (AbstractHttp11JsseProtocol) connector
                .getProtocolHandler();
            handler.setMaxKeepAliveRequests(-1);
            handler.setAcceptorThreadCount(2);
            handler.setMaxHeaderCount(256);
            connector.setRedirectPort(8443);
        });

        return tomcatServletWebServerFactory;
    }


}
