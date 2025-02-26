//package erick.projects.socialnetwork.service;
//import erick.projects.socialnetwork.repository.GroupRepository;
//import erick.projects.socialnetwork.repository.MemberRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.beans.factory.annotation.Autowired;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class GroupService {
//    @Autowired private MemberRepository memberRepo;
//    @Autowired private GroupRepository groupRepo;
//    @Autowired private MessageRepository messageRepo;
//
//    public String sendMessage(Long memberId, Long groupId, String text) {
//        Optional<Member> member = memberRepo.findById(memberId);
//        Optional<Group> group = groupRepo.findById(groupId);
//        if (member.isPresent() && group.isPresent()) {
//            messageRepo.save(new Message(null, member.get(), group.get(), text, LocalDateTime.now()));
//            return "Message sent!";
//        }
//        return "Member or Group not found!";
//    }
//
//    public List<Message> getMessages(Long groupId) {
//        return messageRepo.findByGroupIdOrderByTimestampAsc(groupId);
//    }
//}
