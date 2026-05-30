package gr.skg.java.meetup.spring_ai.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Data
@Component
public class MeetupTools {

   private final MeetupService meetupService;

   @Tool(description = "Get all members of the SKG Java Meetup community")
   public List<Member> getAllMembers() {
      return meetupService.getAllMembers();
   }

   @Tool(description = "Search members by name. Returns a list of matching members or an empty list if none found")
   public List<Member> searchMembersByName(@ToolParam(description = "The name or part of the name to search for") String name) {
      return meetupService.searchMembersByName(name);
   }

   @Tool(description = "Find a specific member by their email address")
   public Member findMemberByEmail(@ToolParam(description = "The email address of the member") String email) {
      return meetupService.findMemberByEmail(email);
   }

   @Tool(description = "RSVP a member to the next meetup event, incrementing their attendance count")
   public String rsvpToEvent(@ToolParam(description = "The email address of the member to RSVP") String email) {
      return meetupService.rsvpToEvent(email);
   }

   @Tool(description = "Get the top attendees of the meetup community, ranked by number of RSVPs")
   public List<Member> getTopAttendees(@ToolParam(description = "The number of top attendees to return") int limit) {
      return meetupService.getTopAttendees(limit);
   }

   @Tool(description = "Get members who joined the community after a specific date")
   public List<Member> getMembersSince(@ToolParam(description = "The date in ISO format (yyyy-MM-dd) to filter members who joined after") LocalDate date) {
      return meetupService.getMembersSince(date);
   }
}
