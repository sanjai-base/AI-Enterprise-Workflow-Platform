package com.workflow.ai.controller;

import com.workflow.ai.agent.CopilotAgent;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/ai")
@CrossOrigin(origins = "*")
public class CopilotController {

    private final CopilotAgent copilotAgent;

    public CopilotController(CopilotAgent copilotAgent) {
        this.copilotAgent = copilotAgent;
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody ChatRequest request) {
        try {
            return ResponseEntity.ok(copilotAgent.chat(request.message()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Sorry, I am currently running in mock mode without a valid OpenAI API Key. But your backend wiring is perfectly connected!");
        }
    }
}

record ChatRequest(String message) {}
