package erick.projects.socialnetwork.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;  // The sender of the message

    @ManyToOne
    private Group group;    // The group where the message is sent

    private String text;

    private LocalDateTime timestamp;
}
