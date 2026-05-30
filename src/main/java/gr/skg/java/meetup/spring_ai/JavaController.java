package gr.skg.java.meetup.spring_ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class JavaController {

   private final ChatClient chatClient;

   public JavaController(ChatClient.Builder builder,
                         ChatMemory chatMemory) {
      this.chatClient = builder
            .defaultSystem("""
                  You are a sarcastic QA engineer, that hates all Developers.
                  Speak only with riddles and be rude. Be direct and short.
                  """)
            .defaultAdvisors(new SimpleLoggerAdvisor(),
                  MessageChatMemoryAdvisor.builder(chatMemory).build())
            .build();
   }

   @GetMapping("/hi")
   public String hello() {
      return "Hello SKG Java Meetup!";
   }

   @GetMapping("/chat")
   public String chat(@RequestParam String message) {
      return chatClient.prompt()
            .user(message)
            .call()
            .content();
   }

   @GetMapping("/chat/suggest")
   public String suggestion(@RequestParam String message) {
      return chatClient.prompt()
            // further enhancement of a given prompt. Great for having a more
            // and complete prompt that will give hints to the LLM about the request
            .user(u -> u.text("""
                        Based on the question of a developer, reply, but add something
                        that will make them question their skills. For example the
                        answer could have been obvious and no need to ask, but
                        since they asked, maybe they are not worthy. Reply with a
                        funny and sarcastic style. Developer's Question: {message}
                        """).param("message", message))
            .call()
            .content();
   }

   @GetMapping("/chat/suggest/dto")
   public ProgrammingLanguageSuggestion suggestionDto(
         @RequestParam String message,
         @RequestParam(defaultValue = "default") String conversationId
         ) {
      return chatClient.prompt()
            // further enhancement of a given prompt. Great for having a more
            // and complete prompt that will give hints to the LLM about the request
            .user(u -> u.text("""
                        Based on the question of a developer, reply, but add something
                        that will make them question their skills. For example the
                        answer could have been obvious and no need to ask, but
                        since they asked, maybe they are not worthy. Reply with a
                        funny and sarcastic style. Developer's Question: {message}
                        """).param("message", message))
            // The conversationId, tells SpringAI to give to the LLM the previous related
            // messages, so it always "remember" the context, based on the conversationId
            .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
            .call()
            .entity(ProgrammingLanguageSuggestion.class);
   }

   @GetMapping("/chat/mem")
   public String chat(@RequestParam String message,
                      @RequestParam(defaultValue = "default") String conversationId) {
      return chatClient.prompt()
            .user(message)
            // The conversationId, tells SpringAI to give to the LLM the previous related
            // messages, so it always "remember" the context, based on the conversationId
            .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
            .call()
            .content();
   }

   record ProgrammingLanguageDTO(String name, String creator, LocalDate createdAt, Double popularity){}
   record ProgrammingLanguageSuggestion(String reason, ProgrammingLanguageDTO language){}
}