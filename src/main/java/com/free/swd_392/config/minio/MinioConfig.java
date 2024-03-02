package com.free.swd_392.config.minio;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@EnableConfigurationProperties(MinioConfigurationProperties.class)
public class MinioConfig {

    private final MinioConfigurationProperties minioConfigurationProperties;

    @Bean
    @SneakyThrows
    public MinioClient minioClient() {

        MinioClient minioClient;
        if (!configuredProxy()) {
            minioClient = MinioClient.builder()
                    .endpoint(minioConfigurationProperties.getUrl())
                    .credentials(minioConfigurationProperties.getAccessKey(), minioConfigurationProperties.getSecretKey())
                    .build();
        } else {
            minioClient = MinioClient.builder()
                    .endpoint(minioConfigurationProperties.getUrl())
                    .credentials(minioConfigurationProperties.getAccessKey(), minioConfigurationProperties.getSecretKey())
                    .httpClient(client())
                    .build();
        }
        minioClient.setTimeout(
                minioConfigurationProperties.getConnectTimeout().toMillis(),
                minioConfigurationProperties.getWriteTimeout().toMillis(),
                minioConfigurationProperties.getReadTimeout().toMillis()
        );

        if (minioConfigurationProperties.isCheckBucket()) {
            try {
                log.debug("Checking if bucket {} exists", minioConfigurationProperties.getBucket());
                BucketExistsArgs existsArgs = BucketExistsArgs.builder()
                        .bucket(minioConfigurationProperties.getBucket())
                        .build();
                if (!minioClient.bucketExists(existsArgs)) {
                    if (minioConfigurationProperties.isCreateBucket()) {
                        createBucket(minioClient);
                    } else {
                        throw new IllegalStateException("Bucket does not exist: " + minioConfigurationProperties.getBucket());
                    }
                }
            } catch (Exception e) {
                log.error("Error while checking bucket", e);
                throw e;
            }
        }

        return minioClient;
    }

    private void createBucket(MinioClient minioClient) throws MinioException {
        try {
            MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder()
                    .bucket(minioConfigurationProperties.getBucket())
                    .build();
            minioClient.makeBucket(makeBucketArgs);
        } catch (Exception e) {
            throw new MinioException("Cannot create bucket " + minioConfigurationProperties.getBucket());
        }
    }

    private boolean configuredProxy() {
        String httpHost = System.getProperty("http.proxyHost");
        String httpPort = System.getProperty("http.proxyPort");
        return httpHost != null && httpPort != null;
    }

    private OkHttpClient client() {
        String httpHost = System.getProperty("http.proxyHost");
        String httpPort = System.getProperty("http.proxyPort");

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (httpHost != null)
            builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(httpHost, Integer.parseInt(httpPort))));
        return builder
                .build();
    }

}
