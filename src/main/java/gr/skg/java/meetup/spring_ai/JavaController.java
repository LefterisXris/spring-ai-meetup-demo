package gr.skg.java.meetup.spring_ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JavaController {

   private final ChatClient chatClient;

   public JavaController(ChatClient.Builder builder) {this.chatClient = builder.build();}

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
}