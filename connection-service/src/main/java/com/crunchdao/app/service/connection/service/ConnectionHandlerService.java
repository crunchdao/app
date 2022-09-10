package com.crunchdao.app.service.connection.service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.crunchdao.app.service.connection.configuration.HandlerConfigurationProperties;
import com.crunchdao.app.service.connection.dto.HandlerDescriptionDto;
import com.crunchdao.app.service.connection.exception.UnknownHandlerException;
import com.crunchdao.app.service.connection.handler.ConnectionHandler;
import com.crunchdao.app.service.connection.handler.ConnectionIdentity;
import com.crunchdao.app.service.connection.handler.HandlerContext;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConnectionHandlerService {
	
	private final Map<String, ConnectionHandler> handlers;
	private final Map<String, String> redirectionUrls;
	private final HandlerConfigurationProperties properties;
	
	public ConnectionHandlerService(List<ConnectionHandler> handlers, HandlerConfigurationProperties properties, @Value("${app.base-url}") String base) {
		this.handlers = buildHandlersMap(handlers);
		this.redirectionUrls = buildRedirectionUrlsMap(handlers, base);
		this.properties = properties;
		
		log.info("Using {} handler(s)", handlers.size());
	}
	
	public String generateUrl(UUID userId, String type) {
		ConnectionHandler handler = getHandler(type);
		HandlerContext context = createContext(userId, type);
		
		return handler.generateUrl(context);
	}
	
	public ConnectionIdentity fetchIdentity(UUID userId, String type, String code) throws Exception {
		ConnectionHandler handler = getHandler(type);
		HandlerContext context = createContext(userId, type);
		
		return handler.fetchIdentity(context, code);
	}
	
	public HandlerContext createContext(UUID userId, String type) {
		String redirectionUrl = getRedirectionUrls(type);
		
		return new SimpleHandlerContext(userId, redirectionUrl);
	}
	
	public ConnectionHandler getHandler(String type) {
		return get(handlers, type);
	}
	
	public String getRedirectionUrls(String type) {
		return get(redirectionUrls, type);
	}
	
	public List<HandlerDescriptionDto> getHandlerTypes() {
		return handlers.keySet()
			.stream()
			.map((key) -> new HandlerDescriptionDto(key, properties.getNames().getOrDefault(key, key)))
			.toList();
	}
	
	public static String buildRedirectionUrl(String type, String base) {
		return String.format("%s/auth/callback/connections/%s", base, type.toLowerCase());
	}
	
	public static Map<String, String> buildRedirectionUrlsMap(List<ConnectionHandler> handlers, String base) {
		return handlers
			.stream()
			.collect(Collectors.toMap(ConnectionHandler::getType, (handler) -> buildRedirectionUrl(handler.getType(), base), (a, b) -> a, () -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER)));
	}
	
	public static Map<String, ConnectionHandler> buildHandlersMap(List<ConnectionHandler> handlers) {
		return handlers
			.stream()
			.collect(Collectors.toMap(ConnectionHandler::getType, Function.identity(), (a, b) -> a, () -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER)));
	}
	
	public static <V> V get(Map<String, V> map, String type) {
		V value = map.get(type);
		
		if (value == null) {
			throw new UnknownHandlerException(type);
		}
		
		return value;
	}
	
	@Getter
	@AllArgsConstructor
	public static class SimpleHandlerContext implements HandlerContext {
		
		private final UUID userId;
		private final String redirectionUrl;
		
	}
	
}