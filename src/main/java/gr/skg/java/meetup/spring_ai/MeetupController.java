package gr.skg.java.meetup.spring_ai;

import gr.skg.java.meetup.spring_ai.domain.Member;
import gr.skg.java.meetup.spring_ai.domain.MeetupService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Data
@RestController
@RequestMapping("/meetup")
public class MeetupController {

   private final MeetupService meetupService;

   @GetMapping("/members")
   public List<Member> getAllMembers() {
      return meetupService.getAllMembers();
   }

   @GetMapping("/members/search")
   public List<Member> searchMembersByName(@RequestParam String name) {
      return meetupService.searchMembersByName(name);
   }

   @GetMapping("/members/find")
   public Member findMemberByEmail(@RequestParam String email) {
      return meetupService.findMemberByEmail(email);
   }

   @PostMapping("/members/rsvp")
   public String rsvpToEvent(@RequestParam String email) {
      return meetupService.rsvpToEvent(email);
   }

   @GetMapping("/members/top")
   public List<Member> getTopAttendees(@RequestParam(defaultValue = "5") int limit) {
      return meetupService.getTopAttendees(limit);
   }

   @GetMapping("/members/since")
   public List<Member> getMembersSince(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
      return meetupService.getMembersSince(date);
   }
}