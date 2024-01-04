package com.kjh.k8s.network.service

import javax.servlet.http.HttpServletRequest

interface AclService {
    fun registrationIp(httpServletRequest: HttpServletRequest): String
    fun initIp()
    fun removeIp(cidr: String)
}