package com.kjh.k8s.network.service

import io.kubernetes.client.custom.V1Patch
import io.kubernetes.client.openapi.ApiException
import io.kubernetes.client.openapi.apis.CoreV1Api
import io.kubernetes.client.openapi.models.V1Service
import io.kubernetes.client.util.PatchUtils
import org.springframework.stereotype.Service
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest

@Service
class AclServiceImpl(private val coreV1Api: CoreV1Api, private val slackService: SlackService): AclService {
    private val namespace = "ingress"
    private val ingressNginx = "ingress-nginx-controller"
    private val apiClient = coreV1Api.apiClient

    @PostConstruct
    fun initializeIpList() {
        initIp()
    }
    override fun registrationIp(httpServletRequest: HttpServletRequest): String {
        println("registration start")
        val ingressService = coreV1Api.readNamespacedService(ingressNginx, namespace, null)
        val ip = httpServletRequest.getHeader("X-Forwarded-For") ?: httpServletRequest.remoteAddr
        val ranges = ingressService.spec!!.loadBalancerSourceRanges!!
        val cidr = "$ip/32"

        if (ranges.contains(cidr)) return "이미 등록된 ip 입니다."

        ranges.add(cidr)

        patch(ingressService.metadata!!.name!!, V1Patch(apiClient.json.serialize(ingressService)))
        println("patch success: $cidr")
        slackService.sendMessage("$ip 가 등록 되었습니다.")

        val executors = Executors.newScheduledThreadPool(1)
        executors.schedule({
            removeIp(cidr)
        }, 2, TimeUnit.HOURS)

        return "정상 등록되었습니다."
    }

    override fun initIp() {
        println("init start")

        try {
            val ingressService = coreV1Api.readNamespacedService(ingressNginx, namespace, null)
            ingressService.spec!!.loadBalancerSourceRanges(arrayListOf("0.0.0.0/16", ""))

            patch(ingressService.metadata!!.name!!, V1Patch(apiClient.json.serialize(ingressService)))

            println("init end")
        } catch (e: ApiException) {
            println(e.responseBody)
        }
    }

    override fun removeIp(cidr: String) {
        println("remove start")

        val ingressService = coreV1Api.readNamespacedService(ingressNginx, namespace, null)
        val ranges = ingressService.spec!!.loadBalancerSourceRanges!!
        ranges.remove(cidr)

        patch(ingressService.metadata!!.name!!, V1Patch(apiClient.json.serialize(ingressService)))

        println("remove ip: $cidr")
    }

    private fun patch(name: String, patch: V1Patch) {
        PatchUtils.patch(
            V1Service::class.java,
            { coreV1Api.patchNamespacedServiceCall(name, namespace, patch, null, null, null, null, null, null) },
            V1Patch.PATCH_FORMAT_STRATEGIC_MERGE_PATCH, apiClient
        )
    }
}