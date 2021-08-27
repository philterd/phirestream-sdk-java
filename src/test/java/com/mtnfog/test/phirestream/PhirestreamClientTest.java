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
package com.mtnfog.test.phirestream;

import com.mtnfog.phirestream.PhirestreamClient;
import com.mtnfog.phirestream.model.ProduceResponse;
import com.mtnfog.phirestream.model.Record;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Ignore
public class PhirestreamClientTest {

    private static final Logger LOGGER = LogManager.getLogger(PhirestreamClientTest.class);

    private static final String ENDPOINT = "https://10.0.2.227:8080/";

    @Test
    public void getFilterProfiles() throws Exception {

        final PhirestreamClient client = new PhirestreamClient.PhirestreamClientBuilder()
                .withEndpoint(ENDPOINT)
                .withOkHttpClientBuilder(getUnsafeOkHttpClientBuilder())
                .build();

        final List<Record> records = Arrays.asList(new Record("key", "value"));
        final ProduceResponse produceResponse = client.produce(records, "topic", false, "profile", "context", "id");

        Assert.assertTrue(produceResponse != null);
        Assert.assertFalse(produceResponse.getOffsets().isEmpty());

    }

    @Test(expected = SSLHandshakeException.class)
    public void getFilterProfilesNoCertificate() throws Exception {

        final PhirestreamClient client = new PhirestreamClient.PhirestreamClientBuilder()
                .withEndpoint(ENDPOINT)
                .build();

        final List<Record> records = Arrays.asList(new Record("key", "value"));
        final ProduceResponse produceResponse = client.produce(records, "topic", false, "profile", "context", "id");

    }

    // This is used to test against Phirestream running with a self-signed certificate.
    private OkHttpClient.Builder getUnsafeOkHttpClientBuilder() throws NoSuchAlgorithmException, KeyManagementException {

        final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

        } };

        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
        builder.connectTimeout(PhirestreamClient.DEFAULT_TIMEOUT_SEC, TimeUnit.SECONDS);
        builder.writeTimeout(PhirestreamClient.DEFAULT_TIMEOUT_SEC, TimeUnit.SECONDS);
        builder.readTimeout(PhirestreamClient.DEFAULT_TIMEOUT_SEC, TimeUnit.SECONDS);
        builder.connectionPool(new ConnectionPool(PhirestreamClient.DEFAULT_MAX_IDLE_CONNECTIONS, PhirestreamClient.DEFAULT_KEEP_ALIVE_DURATION_MS, TimeUnit.MILLISECONDS));
        builder.hostnameVerifier((hostname, session) -> true);

        return builder;

    }

}
