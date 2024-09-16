package com.example.demo.controllers.mvc;

import com.example.demo.models.*;
import com.example.demo.models.dto.ContestSummaryDTO;
import com.example.demo.models.mappers.ContestMapper;
import com.example.demo.services.CategoryService;
import com.example.demo.services.ContestService;
import com.example.demo.services.NotificationService;
import com.example.demo.services.UserService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/user")
public class UsersDashboardMVCController {
    private final UserService userService;
    private final ContestService contestService;
    private final ContestMapper contestMapper;
    private final CategoryService categoryService;
    private final NotificationService notificationService;

    public UsersDashboardMVCController(UserService userService, ContestService contestService, ContestMapper contestMapper, CategoryService categoryService, NotificationService notificationService) {
        this.userService = userService;
        this.contestService = contestService;
        this.contestMapper = contestMapper;
        this.categoryService = categoryService;
        this.notificationService = notificationService;
    }

    @ModelAttribute("categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @ModelAttribute("notifications")
    public List<Notification> getNotifications(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        return notificationService.getUnreadNotifications(user);
    }

    @GetMapping("/openContests")
    public String openContests(Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        List<Contest> openContests = contestService.getPhaseOneContestsAndTypeOpen();
        model.addAttribute("contests", openContests);
        List<User> participants = openContests.stream()
                                              .map(Contest::getParticipants)
                                              .flatMap(List::stream)
                                              .toList();

        model.addAttribute("user", user);
        model.addAttribute("participants", participants);


        return "user-open-contests";
    }

    @GetMapping("/participatedContests")
    public String participatedContests(Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        List<Contest> participatedContests = contestService.getAllParticipatedContests(user);
        List<Contest> invitedContests = contestService.getInvitedContests(user);
        model.addAttribute("invitedContests", invitedContests);
        model.addAttribute("contests", participatedContests);

        return "user-participated-contests";
    }

    @GetMapping("/finishedContests")
    public String finishedContests(Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        List<Contest> finishedContests = contestService.getFinishedContests(user);
//        List<PhotoSubmission> sortedSubmissions = finishedContests.stream()
//                                                                  .map(Contest::getSubmissions)
//                                                                  .flatMap(List::stream)
//                                                                  .sorted(Comparator.comparing(PhotoSubmission::getReviewScore))
//                                                                  .collect(Collectors.toList());

//        Map<Contest, TreeMap<Integer, List<User>>> top3 = new HashMap<>();
//        for(Contest contest: finishedContests){
//            top3.put(contest,c)
//        }


        model.addAttribute("contests", finishedContests);

        return "user-finished-contests";
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal, @ModelAttribute("notifications") List<Notification> notifications) {
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("notifications", notifications);
        return "user-profile";
    }

    @PostMapping("/profile/update/firstName")
    public String updateFirstName(@ModelAttribute("firstName") String firstName,
                                  BindingResult bindingResult,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("firstNameError", bindingResult.getFieldError("firstName"));
            return "redirect:/user/profile";
        }
        User user = userService.getUserByEmail(principal.getName());
        userService.updateFirstName(user.getId(), firstName);
        return "redirect:/user/profile";
    }

    @PostMapping("/profile/update/lastName")
    public String updateLastName(@ModelAttribute("lastName") String lastName,
                                 BindingResult bindingResult,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("lastNameError", bindingResult.getFieldError("lastName"));
            return "redirect:/user/profile";
        }
        User user = userService.getUserByEmail(principal.getName());
        userService.updateLastName(user.getId(), lastName);
        return "redirect:/user/profile";
    }

}
