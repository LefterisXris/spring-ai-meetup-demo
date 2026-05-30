package gr.skg.java.meetup.spring_ai;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JavaController {

   @GetMapping("/hi")
   public String hello() {
      return "Hello SKG Java Meetup!";
   }

}