package gr.skg.java.meetup.spring_ai;

import gr.skg.java.meetup.spring_ai.domain.MeetupTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meetup/ai")
public class MeetupAiAssistant {

   private final ChatClient chatClient;

   public MeetupAiAssistant(ChatClient.Builder builder,
                         ChatMemory chatMemory,
                         MeetupTools meetupTools) {
      this.chatClient = builder
            .defaultSystem("""
                  You are the SKG Java Meetup Assistant — the friendly, knowledgeable helper
                  for the Thessaloniki Java community. Our meetup group has been active for
                  over 14 years and has hosted more than 60 events on meetup.com.

                  Your role:
                  - Help members find information about the community and its members.
                  - Search, look up, and provide details about members when asked.
                  - RSVP members to events when requested.
                  - Share insights about community engagement such as top attendees and new members.

                  Guidelines:
                  - Always use the available tools to look up real data. Never make up member information.
                  - Be warm, welcoming, and concise. We are a developer community — skip the fluff.
                  - When a member is not found, suggest searching by a different term.
                  - Format lists and data in a clean, readable way.
                  - Reply in the same language the user writes in (e.g. Greek or English).
                  """)
            .defaultAdvisors(
                  new SimpleLoggerAdvisor(),
                  MessageChatMemoryAdvisor.builder(chatMemory).build()
            )
            .defaultTools(meetupTools)
            .build();
   }

   @GetMapping("/chat")
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
}