package org.thirty.app.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.thirty.app.model.BlogUser;
import org.thirty.app.repository.BlogUserRepository;

@Controller
public class ProfileController {

    @Autowired
    private BlogUserRepository blogUserRepository;

    @GetMapping("/profile")
    public String showProfile(Authentication authentication, Model model) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        BlogUser blogUser = blogUserRepository.findByUsername(userDetails.getUsername()).orElse(null);

        if (blogUser != null) {
            model.addAttribute("user", blogUser);
            return "profile";
        } else {
            return "redirect:/error";
        }
    }

    @GetMapping("/profile/{id}")
    public String showProfileById(@PathVariable Long id, Model model) {
        Optional<BlogUser> optionalBlogUser = blogUserRepository.findById(id);
        
        if (optionalBlogUser.isPresent()) {
            BlogUser blogUser = optionalBlogUser.get();
            model.addAttribute("profileUser", blogUser);
            return "profile";
        } else {
            model.addAttribute("error", "El perfil solicitado no existe.");
            return "error";
        }
    }



}
