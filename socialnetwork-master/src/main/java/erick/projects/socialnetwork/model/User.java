package erick.projects.socialnetwork.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.util.List;

/**
 * A JPA entity representing a user.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String username;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    private String biography;

    @Column(nullable = false)
    private boolean isActive;

    @OneToOne
    @JoinColumn(name = "profile_image_id")
    private Image profileImage;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "follower")
    private List<Follow> following;

    @OneToMany(mappedBy = "followed")
    private List<Follow> followers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Like> likes;

    public User() {
    }

    public List<Like> getLikes() {
        return likes;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void setFollowing(List<Follow> following) {
        this.following = following;
    }

    public List<Follow> getFollowing() {
        return following;
    }

    public List<Follow> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follow> followers) {
        this.followers = followers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", biography='" + biography + '\'' +
                ", isActive=" + isActive +
                '}';
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Image getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Image profileImage) {
        this.profileImage = profileImage;
    }
}
"""package erick.projects.socialnetwork.controller;

import erick.projects.socialnetwork.model.User;
import erick.projects.socialnetwork.repository.UserRepository;
import org.springframework.web.bind.annotation.*;



import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/password")
public class ResetPasswordController {

    private final UserRepository userRepository;
    private final Map<String, String> resetTokens = new HashMap<>();

    public ResetPasswordController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1. Request Password Reset (Generates a token)
    @PostMapping("/request-reset")
    public String requestReset(@RequestParam String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return "User not found";
        }
        String token = UUID.randomUUID().toString();
        resetTokens.put(token, email);
        System.out.println("Reset Token for " + email + ": " + token);
        return "Reset token generated. Check console.";
    }

    // 2. Reset Password using Token
    @PostMapping("/reset")
    public String resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        if (!resetTokens.containsKey(token)) {
            return "Invalid token";
        }
        String email = resetTokens.get(token);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return "User not found";
        }

        User existingUser = user.get();
        existingUser.setPassword(newPassword); // In real cases, hash this password
        userRepository.save(existingUser);
        resetTokens.remove(token);
        return "Password reset successful for " + email;
    }
}"""
