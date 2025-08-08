package com.msn.SDLCAutonomus.agents;

import com.google.adk.agents.LlmAgent;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import com.msn.SDLCAutonomus.service.UtilityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class URLCheckAgent {

    private final UtilityService utilityService;

    public String runUrlCheckAgent(String url) {


        String prompt = """
You are an Authentication and Security Scanner Agent.

Goal:
Given a URL, check if it is reachable, whether it uses HTTPS, and detect the authentication mechanism applied.

Steps:
1. Make an HTTP request to the given URL without credentials.
2. If unreachable, report "Unreachable".
3. If reachable:
   a. Record HTTP status code.
   b. If the scheme is HTTPS, mark as "Secure" and extract TLS certificate details (issuer, expiry, TLS version).
   c. If the scheme is HTTP, mark as "Not Secure".
4. If the response has status 401 Unauthorized or 403 Forbidden:
   a. Read the `WWW-Authenticate` header.
   b. Identify authentication type:
      - Basic → "Basic Authentication"
      - Bearer → "OAuth2 / JWT"
      - Digest → "Digest Authentication"
      - NTLM → "NTLM / Windows Auth"
      - Negotiate → "Kerberos / SPNEGO"
      - APIKey → "API Key based Authentication"
      - SAML → "SAML-based Authentication"
      - If not matched, mark as "Unknown / Custom".
5. If no `WWW-Authenticate` header but there is a login form in HTML, try to infer from form fields (e.g., `SAMLRequest`, `csrf_token`, `access_token`).
6. Return output in the following JSON format:
{
  "url": "<given URL>",
  "status": "<Open | Closed | Unreachable>",
  "secure": "<Secure | Not Secure>",
  "tls_details": {
      "version": "<TLS version>",
      "issuer": "<certificate issuer>",
      "expiry": "<expiry date>"
  },
  "http_status": <status code>,
  "auth_required": <true|false>,
  "auth_type": "<type>",
  "notes": "<additional details>"
}

Example Input:
https://httpbin.org/basic-auth/user/passwd

Expected Output:
{
  "url": "https://httpbin.org/basic-auth/user/passwd",
  "status": "Open",
  "secure": "Secure",
  "tls_details": {
      "version": "TLS 1.3",
      "issuer": "Let's Encrypt",
      "expiry": "2025-09-01"
  },
  "http_status": 401,
  "auth_required": true,
  "auth_type": "Basic Authentication",
  "notes": "Requires username and password in Base64 encoded format."
}
""";


        log.info("--- 🤖 Starting Excel Agent ---");
        LlmAgent excelAgent = LlmAgent.builder()
                .name("urlCheckAgent")
                .description("Analyzes Excel mapping data and generates Java code for data transformation logic and validation.")
                .instruction(prompt)
                .model("gemini-2.0-flash")
                .build();

        try {
            final InMemoryRunner runner = new InMemoryRunner(excelAgent);
            final Content userMsg = Content.fromParts(
                Part.fromText("URl:\n" + url)
            );

            Event finalEvent = utilityService.retryWithBackoff(() -> {
                Session session = runner.sessionService().createSession(runner.appName(), "user-excel-mapper-logic-generator").blockingGet();
                return runner.runAsync(session.userId(), session.id(), userMsg).blockingLast();
            });
            log.info("--- ✅ Finished URL CHeck  Agent ---"+ finalEvent);
            return finalEvent != null ? finalEvent.stringifyContent() : "Excel Agent failed to produce mapping logic code.";
        } catch (Exception e) {
            log.error("❌ The Excel Agent itself failed to run.", e);
            log.info("--- ❌ Finished Excel Agent with error ---");
            return "Excel Agent execution failed: " + e.getMessage();
        }
    }
} 
