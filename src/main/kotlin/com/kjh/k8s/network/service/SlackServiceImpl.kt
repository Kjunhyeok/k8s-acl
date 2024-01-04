package com.kjh.k8s.network.service

import com.slack.api.Slack
import com.slack.api.model.Attachment
import com.slack.api.model.Field
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Service
class SlackServiceImpl: SlackService {
    override fun sendMessage(text: String) {
        val client = Slack.getInstance().methods()
        val attachment = Attachment()
        val now = LocalDateTime.now().atZone(ZoneId.of("KST"))

        val pattern = DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분")
        val startField = Field()
        startField.title = "등록 시간"
        startField.value = now.format(pattern)

        val endField = Field()
        endField.title = "완료 시간"
        endField.value = now.plusHours(2).format(pattern)
        attachment.fields = arrayListOf(startField, endField)

        client.chatPostMessage {
            it.token("")
                .channel("")
                .text(text)
                .attachments(arrayListOf(attachment))
        }
    }
}