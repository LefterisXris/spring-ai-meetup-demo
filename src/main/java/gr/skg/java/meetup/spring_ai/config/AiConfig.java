package gr.skg.java.meetup.spring_ai.config;

import gr.skg.java.meetup.spring_ai.domain.MeetupTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

   /*
   // Note: This is not required, when there is only one
   // AI provider in the dependencies, but it is needed
   // when there are more (e.g. both OpenAI, Anthropic etc)
   @Bean
   ChatClient.Builder chatClient(
         Map<String, ChatModel>  chatModels
   ) {
      return ChatClient
            .builder(chatModels.get("openAiChatModel"));
   }
   */

   @Bean
   ToolCallbackProvider meetupMcpTools(MeetupTools meetupTools) {
      return MethodToolCallbackProvider
            .builder()
            .toolObjects(meetupTools)
            .build();
   }

}