/*******************************************************************************
 * Copyright 2021 Mountain Fog, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.mtnfog.phirestream;

import com.mtnfog.phirestream.model.ProduceRequest;
import com.mtnfog.phirestream.model.ProduceResponse;
import com.mtnfog.phirestream.model.Record;
import com.mtnfog.phirestream.model.exceptions.NotFoundException;
import com.mtnfog.phirestream.model.exceptions.PhirestreamException;
import com.mtnfog.phirestream.services.PhirestreamService;
import nl.altindag.sslcontext.SSLFactory;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Client class for Phirestream's API.
 * Phirestream redacts PHI and PII from data streams.
 */
public class PhirestreamClient {

	public static final int DEFAULT_TIMEOUT_SEC = 30;
	public static final int DEFAULT_MAX_IDLE_CONNECTIONS = 20;
	public static final int DEFAULT_KEEP_ALIVE_DURATION_MS = 30 * 1000;

	private PhirestreamService service;

	public static class PhirestreamClientBuilder {

		private String endpoint;
		private OkHttpClient.Builder okHttpClientBuilder;
		private long timeout = DEFAULT_TIMEOUT_SEC;
		private int maxIdleConnections = DEFAULT_MAX_IDLE_CONNECTIONS;
		private int keepAliveDurationMs = DEFAULT_KEEP_ALIVE_DURATION_MS;
		private String keystore;
		private String keystorePassword;
		private String truststore;
		private String truststorePassword;

		public PhirestreamClientBuilder withEndpoint(String endpoint) {
			this.endpoint = endpoint;
			return this;
		}

		public PhirestreamClientBuilder withOkHttpClientBuilder(OkHttpClient.Builder okHttpClientBuilder) {
			this.okHttpClientBuilder = okHttpClientBuilder;
			return this;
		}

		public PhirestreamClientBuilder withTimeout(long timeout) {
			this.timeout = timeout;
			return this;
		}

		public PhirestreamClientBuilder withMaxIdleConnections(int maxIdleConnections) {
			this.maxIdleConnections = maxIdleConnections;
			return this;
		}

		public PhirestreamClientBuilder withKeepAliveDurationMs(int keepAliveDurationMs) {
			this.keepAliveDurationMs = keepAliveDurationMs;
			return this;
		}

		public PhirestreamClientBuilder withSslConfiguration(String keystore, String keystorePassword, String truststore, String truststorePassword) {
			this.keystore = keystore;
			this.keystorePassword = keystorePassword;
			this.truststore = truststore;
			this.truststorePassword = truststorePassword;
			return this;
		}

		public PhirestreamClient build() throws Exception {
			return new PhirestreamClient(endpoint, okHttpClientBuilder, timeout, maxIdleConnections, keepAliveDurationMs, keystore,
					keystorePassword, truststore, truststorePassword);
		}

	}

	private PhirestreamClient(String endpoint, OkHttpClient.Builder okHttpClientBuilder, long timeout, int maxIdleConnections, int keepAliveDurationMs,
							  String keystore, String keystorePassword, String truststore, String truststorePassword) throws Exception {

		if(okHttpClientBuilder == null) {

			okHttpClientBuilder = new OkHttpClient.Builder()
					.connectTimeout(timeout, TimeUnit.SECONDS)
					.writeTimeout(timeout, TimeUnit.SECONDS)
					.readTimeout(timeout, TimeUnit.SECONDS)
					.connectionPool(new ConnectionPool(maxIdleConnections, keepAliveDurationMs, TimeUnit.MILLISECONDS));

		}

		if(StringUtils.isNotEmpty(keystore)) {
			configureSSL(okHttpClientBuilder, keystore, keystorePassword, truststore, truststorePassword);
		}

		final OkHttpClient okHttpClient = okHttpClientBuilder.build();

		final Retrofit.Builder builder = new Retrofit.Builder()
				.baseUrl(endpoint)
				.client(okHttpClient)
				.addConverterFactory(ScalarsConverterFactory.create())
				.addConverterFactory(GsonConverterFactory.create());

		final Retrofit retrofit = builder.build();

		service = retrofit.create(PhirestreamService.class);

	}

	private void configureSSL(final OkHttpClient.Builder okHttpClientBuilder, String keystore, String keystorePassword,
							 String truststore, String truststorePassword) {

		final SSLFactory sslFactory = SSLFactory.builder()
				.withIdentityMaterial(Paths.get(keystore), keystorePassword.toCharArray())
				.withTrustMaterial(Paths.get(truststore), truststorePassword.toCharArray())
				.build();

		okHttpClientBuilder.sslSocketFactory(sslFactory.getSslSocketFactory(), sslFactory.getTrustManager().get());

	}

	public ProduceResponse produce(List<Record> records, String topic) throws IOException {

		final ProduceRequest produceRequest = new ProduceRequest(records);

		final String context = UUID.randomUUID().toString();
		final String documentId = UUID.randomUUID().toString();

		final Response response = service.produce(topic, false, topic, context, documentId, produceRequest).execute();

		if(response.isSuccessful()) {

			return (ProduceResponse) response.body();

		} else {

			if(response.code() == 404) {

				throw new NotFoundException("Topic not found");

			} else {

				throw new PhirestreamException("Error: HTTP " + response.code());

			}

		}

	}

	public ProduceResponse produce(List<Record> records, String topic, boolean async, String profile, String context, String documentId) throws IOException {

		final ProduceRequest produceRequest = new ProduceRequest(records);

		final Response response = service.produce(topic, async, profile, context, documentId, produceRequest).execute();

		if(response.isSuccessful()) {

			return (ProduceResponse) response.body();

		} else {

			if(response.code() == 404) {

				throw new NotFoundException("Topic not found");

			} else {

				throw new PhirestreamException("Error: HTTP " + response.code());

			}

		}

	}

}
