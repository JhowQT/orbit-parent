package com.orbitbook.aiservice.config;

import com.orbitbook.aiservice.ai.DestinationTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Expõe os @Tool de DestinationTools como MCP resources.
 * Outros agentes de IA podem descobrir e invocar essas ferramentas
 * via protocolo MCP no endpoint /mcp/sse.
 */
@Configuration
public class McpConfig {

    @Bean
    public ToolCallbackProvider destinationToolsProvider(
            DestinationTools destinationTools) {

        return MethodToolCallbackProvider
                .builder()
                .toolObjects(destinationTools)
                .build();
    }
}
