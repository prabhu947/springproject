package erick.projects.socialnetwork.controller;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


// ======================= Entities ============================
@Entity
class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    public Long getId() { return id; }
    public String getName() { return name; }
}

@Entity
class Group {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    public Long getId() { return id; }
    public String getName() { return name; }
}

@Entity
class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne private Member member;
    @ManyToOne private Group group;
    private String text;
    private LocalDateTime timestamp;

    public Message() {}
    public Message(Member member, Group group, String text) {
        this.member = member;
        this.group = group;
        this.text = text;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getText() { return text; }
    public String getMemberName() { return member.getName(); }
    public LocalDateTime getTimestamp() { return timestamp; }
}

// ======================= Repositories ============================
interface MemberRepository extends JpaRepository<Member, Long> {}
interface GroupRepository extends JpaRepository<Group, Long> {}
interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByGroupIdOrderByTimestampAsc(Long groupId);
}

// ======================= Service ============================
@Service
class GroupService {
    @Autowired private MemberRepository memberRepo;
    @Autowired private GroupRepository groupRepo;
    @Autowired private MessageRepository messageRepo;

    public String sendMessage(Long memberId, Long groupId, String text) {
        Optional<Member> member = memberRepo.findById(memberId);
        Optional<Group> group = groupRepo.findById(groupId);
        if (member.isPresent() && group.isPresent()) {
            messageRepo.save(new Message(member.get(), group.get(), text));
            return "Message sent!";
        }
        return "Member or Group not found!";
    }

    public List<Message> getMessages(Long groupId) {
        return messageRepo.findByGroupIdOrderByTimestampAsc(groupId);
    }
}

// ======================= Controller ============================
@RestController
@RequestMapping("/groups")
class SocialGroup {
    @Autowired private GroupService groupService;

    @PostMapping("/{groupId}/send/{memberId}")
    public String sendMessage(@PathVariable Long memberId, @PathVariable Long groupId, @RequestBody String text) {
        return groupService.sendMessage(memberId, groupId, text);
    }

    @GetMapping("/{groupId}/messages")
    public List<Message> getMessages(@PathVariable Long groupId) {
        return groupService.getMessages(groupId);
    }
}
