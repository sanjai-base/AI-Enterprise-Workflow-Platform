package com.workflow.ai.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface CopilotAgent {

    @SystemMessage("You are a helpful AI Copilot for an Enterprise Workflow Automation Platform. You help users manage workflows, approve steps, and find documents.")
    String chat(@UserMessage String userMessage);
}
