package com.kjh.k8s.network.controller

import com.kjh.k8s.network.service.AclService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/acl")
class AclController (private val aclService: AclService) {

    @GetMapping
    fun registration(httpServletRequest: HttpServletRequest): String {
        return aclService.registrationIp(httpServletRequest)
    }
}