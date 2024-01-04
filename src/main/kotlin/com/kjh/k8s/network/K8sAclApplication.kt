package com.kjh.k8s.network

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class K8sAclApplication

fun main(args: Array<String>) {
    runApplication<K8sAclApplication>(*args)
}
