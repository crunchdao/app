package com.crunchdao.app.gateway.configuration;

import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class WebServerConfiguration implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {
	
	private static final int MAX_HEADER_SIZE = 8192 * 8;
	
	@Override
	public void customize(NettyReactiveWebServerFactory factory) {
		factory.addServerCustomizers(server -> server.httpRequestDecoder(decoder -> decoder.maxHeaderSize(MAX_HEADER_SIZE)));
	}
	
}