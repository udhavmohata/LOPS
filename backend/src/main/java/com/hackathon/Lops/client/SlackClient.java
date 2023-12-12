package com.hackathon.Lops.client;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SlackClient {
    final String slackToken = "xoxb-4215409533-3452298435542-to1WSDCdP2VaXo0ZAwGvcAhY";
    final String slackChannelNameDefaultGroup = "lops-alert";
    final String slackChannelName = "#lops-alert";

    public void sendMessageToSlack(String message) {

        log.info("Sending slack message: {}, to channel {}", message, slackChannelName);
        try {
            MethodsClient client = Slack.getInstance().methods();

            List<LayoutBlock> messages = new ArrayList<>();
            messages.add(SectionBlock
                    .builder()
                    .text(MarkdownTextObject
                            .builder()
                            .text(message)
                            .build())
                    .build());
            String channelName = StringUtils.hasText(slackChannelName) ?
                    slackChannelName : "#" + slackChannelNameDefaultGroup;
            ChatPostMessageResponse response = client.chatPostMessage(r -> r
                    .token(slackToken)
                    .channel(channelName)
                    .blocks(messages)
            );
            log.info("RESPONSE from Slack call {}", response.toString());
        } catch (Exception e) {
            log.error("EXCEPTION from Slack call {}", e.getMessage());
            throw new RuntimeException("EXCEPTION from Slack call " + e.getMessage());
        }
    }
}
