package com.kjh.k8s.network.config

import io.kubernetes.client.openapi.ApiClient
import io.kubernetes.client.openapi.apis.CoreV1Api
import io.kubernetes.client.util.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KubeConfig {

    @Bean
    fun coreV1Api(): CoreV1Api {
        return CoreV1Api(client())
    }

    fun client(): ApiClient {
        return Config.fromCluster()
    }
}