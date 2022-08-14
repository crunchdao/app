package com.crunchdao.app.gateway.filter;

import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.crunchdao.app.common.security.SecurityHttpHeaders;
import com.crunchdao.app.common.security.authentication.AuthenticationType;
import com.crunchdao.app.gateway.api.auth.AuthenticatedUserDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationWebFilter implements WebFilter {
	
	private final String authenticateEndpoint;
	private final WebClient webClient;
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		return extractAuthorization(exchange)
			.flatMap((authorization) -> webClient.get()
				.uri(authenticateEndpoint)
				.header(HttpHeaders.AUTHORIZATION, authorization)
				.exchangeToMono(this::handleResponse)
				.onErrorResume((error) -> {
					log.warn("Could not authenticate", error);
					return Mono.empty();
				}))
			.map((authenticatedUser) -> applyHeaders(exchange, authenticatedUser))
			.switchIfEmpty(Mono.defer(() -> Mono.just(exchange)))
			.map(AuthenticationWebFilter::removeOtherHeaders)
			.flatMap(chain::filter);
	}
	
	public Mono<String> extractAuthorization(ServerWebExchange exchange) {
		return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
	}
	
	public Mono<AuthenticatedUserDto> handleResponse(ClientResponse response) {
		if (response.statusCode().is2xxSuccessful()) {
			return response.bodyToMono(AuthenticatedUserDto.class);
		}
		
		return Mono.empty();
	}
	
	public static ServerWebExchange applyHeaders(ServerWebExchange exchange, AuthenticatedUserDto authenticatedUser) {
		return exchange.mutate()
			.request((request) -> request
				.header(SecurityHttpHeaders.AUTHENTICATION_TYPE, AuthenticationType.USER.name())
				.header(SecurityHttpHeaders.USER_ID, authenticatedUser.getUserId().toString())
				.header(SecurityHttpHeaders.AUTHORITIES, authenticatedUser.getAuthorities().stream().collect(Collectors.joining(","))))
			.build();
	}
	
	public static ServerWebExchange removeHeaders(ServerWebExchange exchange) {
		return exchange.mutate()
			.request((request) -> request.headers((headers) -> {
				headers.remove(SecurityHttpHeaders.AUTHENTICATION_TYPE);
				headers.remove(SecurityHttpHeaders.USER_ID);
				headers.remove(SecurityHttpHeaders.AUTHORITIES);
			}))
			.build();
	}
	
	public static ServerWebExchange removeOtherHeaders(ServerWebExchange exchange) {
		return exchange.mutate()
			.request((request) -> request.headers((headers) -> {
				headers.remove(HttpHeaders.COOKIE);
				headers.remove(HttpHeaders.SET_COOKIE);
				headers.remove(HttpHeaders.AUTHORIZATION);
			}))
			.build();
	}
	
}