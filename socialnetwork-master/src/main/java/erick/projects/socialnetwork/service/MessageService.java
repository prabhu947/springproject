package erick.projects.socialnetwork.service;


import erick.projects.socialnetwork.model.Group;
import erick.projects.socialnetwork.model.Message;
import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.repository.GroupRepository;
import erick.projects.socialnetwork.repository.MessageRepository;
import erick.projects.socialnetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    public Message sendMessage(Long userId, Long groupId, String content) {
        User sender = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));

        Message message = new Message();
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setSender(sender);
        message.setGroup(group);

        return messageRepository.save(message);
    }
}

