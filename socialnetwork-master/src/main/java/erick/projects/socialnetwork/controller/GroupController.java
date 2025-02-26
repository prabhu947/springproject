//package erick.projects.socialnetwork.controller;
//
//import erick.projects.socialnetwork.model.Group;
//import erick.projects.socialnetwork.model.Message;
//import erick.projects.socialnetwork.service.GroupService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/groups")
//public class GroupController {
//
//    @Autowired
//    private GroupService groupService;
//
//    @PostMapping("/create")
//    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
//        return ResponseEntity.ok(groupService.createGroup(group));
//    }
//
//    @PostMapping("/{groupId}/addMember/{userId}")
//    public ResponseEntity<String> addMember(@PathVariable Long groupId, @PathVariable Long userId) {
//        groupService.addMember(groupId, userId);
//        return ResponseEntity.ok("Member added successfully");
//    }
//
//    @PostMapping("/{groupId}/postMessage/{userId}")
//    public ResponseEntity<Message> postMessage(@PathVariable Long groupId, @PathVariable Long userId, @RequestBody Message message) {
//        return ResponseEntity.ok(groupService.postMessage(groupId, userId, message));
//    }
//
//    @GetMapping("/{groupId}/messages")
//    public ResponseEntity<List<Message>> getMessages(@PathVariable Long groupId) {
//        return ResponseEntity.ok(groupService.getMessages(groupId));
//    }
//}
