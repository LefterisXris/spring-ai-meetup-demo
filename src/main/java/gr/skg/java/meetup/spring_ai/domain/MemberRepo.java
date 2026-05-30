package gr.skg.java.meetup.spring_ai.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberRepo extends JpaRepository<Member, Long> {
   Optional<Member> findByEmailIgnoreCase(String email);
   Optional<Member> findByFullNameContainingIgnoreCase(String fullName);
   List<Member> findAllByFullNameContainingIgnoreCase(String fullName);
   List<Member> findByJoinedAtAfterOrderByJoinedAtDesc(LocalDate date);
}
