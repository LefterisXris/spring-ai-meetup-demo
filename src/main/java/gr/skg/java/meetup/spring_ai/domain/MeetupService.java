package gr.skg.java.meetup.spring_ai.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Data
@Service
public class MeetupService {

   private final MemberRepo memberRepo;

   public List<Member> getAllMembers() {
      log.info("Retrieving all members...");
      return memberRepo.findAll();
   }

   public List<Member> searchMembersByName(String name) {
      log.info("Searching members by name: {}", name);
      return memberRepo.findAllByFullNameContainingIgnoreCase(name);
   }

   public Member findMemberByEmail(String email) {
      log.info("Finding member by email: {}", email);
      return memberRepo.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new RuntimeException("Member not found with email: " + email));
   }

   public String rsvpToEvent(String email) {
      log.info("RSVPing member with email: {}", email);
      Member member = findMemberByEmail(email);
      member.setNumberOfRsvps(member.getNumberOfRsvps() + 1);
      memberRepo.save(member);
      return member.getFullName() + " has RSVPed! Total RSVPs: " + member.getNumberOfRsvps();
   }

   public List<Member> getTopAttendees(int limit) {
      log.info("Getting top {} attendees", limit);
      return memberRepo.findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "numberOfRsvps")))
            .getContent();
   }

   public List<Member> getMembersSince(LocalDate date) {
      log.info("Getting members since {}", date);
      return memberRepo.findByJoinedAtAfterOrderByJoinedAtDesc(date);
   }
}