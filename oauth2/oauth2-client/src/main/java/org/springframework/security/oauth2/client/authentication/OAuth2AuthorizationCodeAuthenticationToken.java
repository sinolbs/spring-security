/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.oauth2.client.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.util.Assert;

import java.util.Collections;

/**
 * An {@link AbstractAuthenticationToken} for the OAuth 2.0 Authorization Code Grant.
 *
 * @author Joe Grandja
 * @since 5.1
 * @see AbstractAuthenticationToken
 * @see ClientRegistration
 * @see OAuth2AuthorizationExchange
 * @see OAuth2AccessToken
 * @see <a target="_blank" href="https://tools.ietf.org/html/rfc6749#section-4.1">Section 4.1 Authorization Code Grant Flow</a>
 */
public class OAuth2AuthorizationCodeAuthenticationToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
	private ClientRegistration clientRegistration;
	private OAuth2AuthorizationExchange authorizationExchange;
	private OAuth2AccessToken accessToken;

	/**
	 * This constructor should be used when the Authorization Request/Response is complete.
	 *
	 * @param clientRegistration the client registration
	 * @param authorizationExchange the authorization exchange
	 */
	public OAuth2AuthorizationCodeAuthenticationToken(ClientRegistration clientRegistration,
														OAuth2AuthorizationExchange authorizationExchange) {
		super(Collections.emptyList());
		Assert.notNull(clientRegistration, "clientRegistration cannot be null");
		Assert.notNull(authorizationExchange, "authorizationExchange cannot be null");
		this.clientRegistration = clientRegistration;
		this.authorizationExchange = authorizationExchange;
	}

	/**
	 * This constructor should be used when the Access Token Request/Response is complete,
	 * which indicates that the Authorization Code Grant flow has fully completed.
	 *
	 * @param clientRegistration the client registration
	 * @param authorizationExchange the authorization exchange
	 * @param accessToken the access token credential
	 */
	public OAuth2AuthorizationCodeAuthenticationToken(ClientRegistration clientRegistration,
														OAuth2AuthorizationExchange authorizationExchange,
														OAuth2AccessToken accessToken) {
		this(clientRegistration, authorizationExchange);
		Assert.notNull(accessToken, "accessToken cannot be null");
		this.accessToken = accessToken;
		this.setAuthenticated(true);
	}

	@Override
	public Object getPrincipal() {
		return this.clientRegistration.getClientId();
	}

	@Override
	public Object getCredentials() {
		return this.accessToken != null ?
			this.accessToken.getTokenValue() :
			this.authorizationExchange.getAuthorizationResponse().getCode();
	}

	/**
	 * Returns the {@link ClientRegistration client registration}.
	 *
	 * @return the {@link ClientRegistration}
	 */
	public ClientRegistration getClientRegistration() {
		return this.clientRegistration;
	}

	/**
	 * Returns the {@link OAuth2AuthorizationExchange authorization exchange}.
	 *
	 * @return the {@link OAuth2AuthorizationExchange}
	 */
	public OAuth2AuthorizationExchange getAuthorizationExchange() {
		return this.authorizationExchange;
	}

	/**
	 * Returns the {@link OAuth2AccessToken access token}.
	 *
	 * @return the {@link OAuth2AccessToken}
	 */
	public OAuth2AccessToken getAccessToken() {
		return this.accessToken;
	}
}
