package org.thirty.app.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thirty.app.model.BlogUser;
import org.thirty.app.model.FriendRequest;
import org.thirty.app.service.BlogUserService;
import org.thirty.app.service.FriendRequestService;

@Controller
@RequestMapping("/friend-request")
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private BlogUserService blogUserService;

    @PostMapping("/send")
    public String sendFriendRequest(@RequestParam("senderId") Long senderId, @RequestParam("receiverUsername") String receiverUsername, RedirectAttributes redirectAttributes) {
        BlogUser sender = blogUserService.findUserById(senderId);
        Optional<BlogUser> receiverOptional = blogUserService.findByUsername(receiverUsername);
        if (sender != null && receiverOptional.isPresent()) {
            BlogUser receiver = receiverOptional.get();
            
            boolean alreadySent = friendRequestService.findBySenderAndReceiverAndStatus(sender, receiver, FriendRequest.FriendRequestStatus.PENDING).isPresent();
            boolean alreadyReceived = friendRequestService.findBySenderAndReceiverAndStatus(receiver, sender, FriendRequest.FriendRequestStatus.PENDING).isPresent();
            
            if (blogUserService.areFriends(sender, receiver)) {
                redirectAttributes.addFlashAttribute("areFriends", true);
                return "redirect:/home";
            } else if (!alreadySent && !alreadyReceived) {
                friendRequestService.sendFriendRequest(sender, receiver);
                redirectAttributes.addFlashAttribute("message", "La solicitud de amistad se ha enviado correctamente.");
                return "redirect:/home";
            }
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Error al enviar la solicitud de amistad.");
        return "redirect:/error";
    }


    @PostMapping("/accept")
    public String acceptFriendRequest(@RequestParam("friendRequestId") Long friendRequestId) {
        FriendRequest friendRequest = friendRequestService.findFriendRequestById(friendRequestId);

        if (friendRequest != null) {
            friendRequestService.acceptFriendRequest(friendRequest);
            return "redirect:/home";
        }

        return "redirect:/error";
    }

    @PostMapping("/reject")
    public String rejectFriendRequest(@RequestParam("friendRequestId") Long friendRequestId) {
        FriendRequest friendRequest = friendRequestService.findFriendRequestById(friendRequestId);

        if (friendRequest != null) {
            friendRequestService.rejectFriendRequest(friendRequest);
            return "redirect:/friend-request/friends";
        }

        return "redirect:/error";
    }
    @GetMapping("/friends")
    public String showFriends(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        BlogUser currentUser = blogUserService.findByUsername(principal.getName()).orElse(null);
        if (currentUser == null) {
            return "redirect:/error";
        }

        List<BlogUser> friends = blogUserService.findFriends(currentUser);
        model.addAttribute("friends", friends);

        List<FriendRequest> pendingFriendRequests = friendRequestService.findPendingFriendRequests(currentUser);
        model.addAttribute("pendingFriendRequests", pendingFriendRequests);
        
        // Agregar la variable isAuthenticated al modelo
        model.addAttribute("isAuthenticated", principal != null);

        // Agregar la variable currentUser al modelo
        model.addAttribute("currentUser", currentUser);
        
        return "friends";
    }

    @GetMapping("/pending")
    public String getPendingFriendRequests(Model model, Principal principal) {
        BlogUser currentUser = blogUserService.findByUsername(principal.getName()).orElse(null);
        if (currentUser == null) {
            return "redirect:/error";
        }

        List<FriendRequest> pendingFriendRequests = friendRequestService.findPendingFriendRequests(currentUser);
        model.addAttribute("pendingFriendRequests", pendingFriendRequests);

        return "pendingFriendRequests";
    }

    @GetMapping("/search")
    public String searchFriendRequest(Model model, Principal principal, @RequestParam(name = "search", required = false) String searchTerm) {
        BlogUser currentUser = blogUserService.findByUsername(principal.getName()).orElse(null);
        if (currentUser == null) {
            return "redirect:/error";
        }

        if (searchTerm != null) {
            Optional<BlogUser> searchedUserOptional = blogUserService.findByUsername(searchTerm);
            if (searchedUserOptional.isPresent()) {
                BlogUser searchedUser = searchedUserOptional.get();
                boolean areFriends = blogUserService.areFriends(currentUser, searchedUser);
                model.addAttribute("searchedUser", searchedUser);
                model.addAttribute("areFriends", areFriends);
                return "friendRequestSearchResult";
            }
        }

        return "friendRequestSearch";
    }
}



