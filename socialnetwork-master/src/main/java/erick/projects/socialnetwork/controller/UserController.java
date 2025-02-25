package erick.projects.socialnetwork.controller;

import erick.projects.socialnetwork.model.Image;
import erick.projects.socialnetwork.model.Post;
import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.repository.ImageRepository;
import erick.projects.socialnetwork.service.FollowService;
import erick.projects.socialnetwork.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {

    private final FollowService followService;
    private final UserService userService;
    private final ImageRepository imageRepository;

    @Autowired
    public UserController(FollowService followService, UserService userService, ImageRepository imageRepository) {
        this.followService = followService;
        this.userService = userService;
        this.imageRepository = imageRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        if (userService.isUsernameTaken(user.getUsername())) {
            response.put("error", "Username is already taken");
            return ResponseEntity.badRequest().body(response);
        } else if (userService.isEmailTaken(user.getEmail())) {
            response.put("error", "Email is already taken");
            return ResponseEntity.badRequest().body(response);
        } else {
            userService.createUser(user);
            response.put("message", "User registered successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody User user, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        boolean isAuthenticated = userService.authenticateUser(user);
        if (isAuthenticated) {
            User dbUser = userService.findByUsername(user.getUsername());
            session.setAttribute("user", dbUser);
            response.put("message", "Login successful");
            response.put("user", dbUser);
            return ResponseEntity.ok().body(response);
        } else {
            response.put("error", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logoutUser(HttpSession session) {
        session.invalidate();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable String username, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        Map<String, Object> response = new HashMap<>();
        if (sessionUser == null) {
            response.put("error", "Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        User user = userService.findByUsername(username);
        if (user == null) {
            response.put("error", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        boolean isFollowing = followService.isFollowing(sessionUser, user);
        List<Post> posts = user.getPosts().stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .collect(Collectors.toList());

        List<Map<String, Object>> postResponses = posts.stream().map(post -> {
            Map<String, Object> postMap = new HashMap<>();
            postMap.put("id", post.getId());
            postMap.put("content", post.getContent());
            postMap.put("createdAt", post.getCreatedAt());
            return postMap;
        }).collect(Collectors.toList());

        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("username", user.getUsername());
        userResponse.put("name", user.getName());
        userResponse.put("email", user.getEmail());
        userResponse.put("biography", user.getBiography());
        if (user.getProfileImage() != null) {
            userResponse.put("profileImageId", user.getProfileImage().getId());
        }

        response.put("user", userResponse);
        response.put("posts", postResponses);
        response.put("isFollowing", isFollowing);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers(HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        Map<String, Object> response = new HashMap<>();
        if (sessionUser == null) {
            response.put("error", "Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        List<User> users = userService.findAll();
        List<Map<String, Object>> userResponses = users.stream().map(user -> {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("username", user.getUsername());
            userMap.put("name", user.getName());
            userMap.put("email", user.getEmail());
            if (user.getProfileImage() != null) {
                userMap.put("profileImageId", user.getProfileImage().getId());
            }
            return userMap;
        }).collect(Collectors.toList());
        response.put("users", userResponses);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(value = "/profile/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> editProfile(
            @RequestParam("name") String name,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("biography") String biography,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
            HttpSession session) throws IOException {
        User sessionUser = (User) session.getAttribute("user");
        Map<String, Object> response = new HashMap<>();
        if (sessionUser == null) {
            response.put("error", "Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        User user = userService.findByUsername(sessionUser.getUsername());
        if (user == null) {
            response.put("error", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (!user.getUsername().equals(username) && userService.isUsernameTaken(username)) {
            response.put("error", "Username is already taken");
            return ResponseEntity.badRequest().body(response);
        }
        if (!user.getEmail().equals(email) && userService.isEmailTaken(email)) {
            response.put("error", "Email is already taken");
            return ResponseEntity.badRequest().body(response);
        }
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setBiography(biography);

        if (profilePicture != null && !profilePicture.isEmpty()) {
            Image img = new Image();
            img.setImageName(profilePicture.getOriginalFilename());
            img.setImageType(profilePicture.getContentType());
            img.setImage(profilePicture.getBytes());
            imageRepository.save(img);
            user.setProfileImage(img);
        }

        userService.save(user);
        if (!sessionUser.getUsername().equals(username)) {
            session.setAttribute("user", user);
        }

        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("username", user.getUsername());
        userResponse.put("name", user.getName());
        userResponse.put("email", user.getEmail());
        userResponse.put("biography", user.getBiography());
        if (user.getProfileImage() != null) {
            userResponse.put("profileImageId", user.getProfileImage().getId());
        }

        response.put("message", "Profile updated successfully");
        response.put("user", userResponse);
        return ResponseEntity.ok().body(response);
    }
}