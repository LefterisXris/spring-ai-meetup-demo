package gr.skg.java.meetup.spring_ai.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Represents Java Meetup Member
 */
@Data
@Entity
public class Member {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(nullable = false)
   private Long id;
   private String fullName;
   private String bio;
   private String email;
   private LocalDate joinedAt;
   private Integer numberOfRsvps;
}