package com.kjh.k8s.network.service

interface SlackService {
    fun sendMessage(text: String)
}