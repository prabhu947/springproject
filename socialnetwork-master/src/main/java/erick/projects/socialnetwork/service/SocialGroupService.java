//package erick.projects.socialnetwork.service;
//
//import erick.projects.socialnetwork.model.Message;
//import org.springframework.stereotype.Service;
//import org.springframework.beans.factory.annotation.Autowired;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class SocialGroupService {
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private GroupRepository groupRepository;
//
//    @Autowired
//    private MemberGroupRepository memberGroupRepository;
//
//    @Autowired
//    private MessageRepository messageRepository;
//
//    public String joinGroup(Long memberId, Long groupId) {
//        Optional<Member> member = memberRepository.findById(memberId);
//        Optional<Group> group = groupRepository.findById(groupId);
//
//        if (member.isPresent() && group.isPresent()) {
//            MemberGroup memberGroup = new MemberGroup();
//            memberGroup.setMember(member.get());
//            memberGroup.setGroup(group.get());
//            memberGroupRepository.save(memberGroup);
//            return "Member joined the group successfully!";
//        }
//        return "Member or Group not found!";
//    }
//
//    public String leaveGroup(Long memberId, Long groupId) {
//        memberGroupRepository.deleteByMemberIdAndGroupId(memberId, groupId);
//        return "Member left the group successfully!";
//    }
//
//    public String sendMessage(Long memberId, Long groupId, String text) {
//        Optional<Member> member = memberRepository.findById(memberId);
//        Optional<Group> group = groupRepository.findById(groupId);
//
//        if (member.isPresent() && group.isPresent()) {
//            // Check if the member is part of the group before sending a message
//            if (!memberGroupRepository.existsByMemberIdAndGroupId(memberId, groupId)) {
//                return "Member is not part of this group!";
//            }
//
//            Message message = new Message();
//            message.setMember(member.get());
//            message.setGroup(group.get());
//            message.setText(text);
//            message.setTimestamp(LocalDateTime.now());
//            messageRepository.save(message);
//
//            return "Message sent successfully!";
//        }
//        return "Member or Group not found!";
//    }
//
//    public List<Message> getMessages(Long groupId) {
//        return messageRepository.findByGroupIdOrderByTimestampAsc(groupId);
//    }
//}
//
