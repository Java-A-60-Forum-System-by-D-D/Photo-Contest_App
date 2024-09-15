package com.example.demo.controllers.mvc;

import com.example.demo.models.Contest;
import com.example.demo.models.Notification;
import com.example.demo.models.Type;
import com.example.demo.models.User;
import com.example.demo.models.dto.ContestDto;
import com.example.demo.models.dto.ContestViewDto;
import com.example.demo.models.dto.JuryDTO;
import com.example.demo.models.mappers.ContestMapper;
import com.example.demo.services.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class OrganizerDashboardMVCController {
    private final CategoryService categoryService;
    private final ContestService contestService;
    private final UserService userService;
    private final ContestMapper contestMapper;
    private final NotificationService notificationService;
    private final CloudinaryImageService cloudinaryImageService;


    public OrganizerDashboardMVCController(CategoryService categoryService, ContestService contestService, UserService userService, ContestMapper contestMapper, NotificationService notificationService, CloudinaryImageService cloudinaryImageService) {
        this.categoryService = categoryService;
        this.contestService = contestService;
        this.userService = userService;
        this.contestMapper = contestMapper;
        this.notificationService = notificationService;
        this.cloudinaryImageService = cloudinaryImageService;
    }

    @ModelAttribute("notifications")
    public List<Notification> getNotifications(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        return notificationService.getUnreadNotifications(user);
    }
    @GetMapping("/usernames")
    @ResponseBody
    public List<String> findUsernames(@RequestParam("term") String term) {
//        List<String> usernames = userService.findUsernamesByTerm(term);
////        return usernames.stream()
//                .map(username -> username.toLowerCase(Locale.ROOT))
//                .collect(Collectors.toList());
        return List.of("test1@example.com", "test2@example.com", "test3@example.com");
    }


    @GetMapping("/createContest")
    public String createContest(Model model) {
        model.addAttribute("contestDto", new ContestDto());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("types", Type.values());
        return "create-contest";
    }

    @PostMapping("/createContest")
    public String createContest(@Valid @ModelAttribute("contestDto") ContestDto contestDto, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                Model model, Principal principal) throws IOException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.contestDto", bindingResult);
            redirectAttributes.addFlashAttribute("contestDto", contestDto);
            return "create-contest";
        }
        String coverPhoto = null;
        switch (contestDto.getCoverPhotoOption()) {
            case "upload":
                coverPhoto = saveUploadedFile(contestDto.getCoverPhotoUpload());
                break;
            case "url":
                coverPhoto = savePhotoUrl(contestDto.getCoverPhotoUrl());
                break;
        }
        User user = userService.getUserByEmail(principal.getName());
        Contest contest = contestMapper.createContestFromDto(contestDto, user, coverPhoto);
        List<User> users = userService.getAllUsers();
        contest = contestService.createContest(contest, user);
//        List<String> photos = contestService.getAllPhotos();
//        model.addAttribute("photos", photos);
        redirectAttributes.addAttribute("id", contest.getId());

        return "redirect:/admin/editContest/{id}";
    }
    private String saveUploadedFile(MultipartFile file) throws IOException {
       return cloudinaryImageService.uploadImage(file);
    }
    private String savePhotoUrl(String url) throws IOException {
        return cloudinaryImageService.uploadImageFromUrl(url);
    }

    @GetMapping("/editContest/{id}")
    public String editContest(@PathVariable long id, Model model) {
        Contest contest = contestService.getContestById(id);
        ContestViewDto contestViewDto = contestMapper.createContestViewDto(contest);
        String type = contest.getType()
                             .toString()
                             .toUpperCase();
        List<User> invitedUsers = contest.getInvitedUsers();
        List<User> jury = contest.getJurorContests();

        model.addAttribute("invitedUsers", invitedUsers);
        model.addAttribute("jury", jury);
        model.addAttribute("contestDto", contest);
        model.addAttribute("contestViewDto", contestViewDto);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("types", Type.values());
        model.addAttribute("type", type);
        return "edit-contest";
    }


    @PostMapping("/editContest/{id}/addJuryMember")
    public String addJuryMember(@PathVariable long id, @RequestParam("email") String email, Model model, Principal principal) {
        //todo catch if user hasn't got needed role - BindingResult?
        User jury = userService.getUserByEmail(email);
        User user = userService.getUserByEmail(principal.getName());
        Contest contest = contestService.addJury(id, jury, user);
        model.addAttribute("id", contest.getId());
        return "redirect:/admin/editContest/{id}";
    }

    @PostMapping("/editContest/{id}/addInvitationalUser")
    public String addInvitationalUser(@PathVariable long id, @RequestParam("email") String email, Model model, Principal principal) {
        User userToInvite = userService.getUserByEmail(email);
        User user = userService.getUserByEmail(principal.getName());
        Contest contest = contestService.inviteUserToContest(id, userToInvite, user);

        model.addAttribute("id", contest.getId());

        return "redirect:/admin/editContest/{id}";
    }

    @GetMapping("/notStarted")
    public String notStarted(Model model) {
        List<Contest> notStartedContest = contestService.getNotStartedContests();
        model.addAttribute("contests", notStartedContest);
        return "not-started";
    }

    @PostMapping("/notStarted/{id}")
    public String deleteContest(@PathVariable long id) {
        contestService.deleteContest(id);
        return "redirect:/admin/notStarted";
    }

//    @PostMapping()

    @GetMapping("/phaseOne")
    public String phaseOne(Model model, Principal principal) {


        List<Contest> phaseOneContests = contestService.getPhaseOneContests();
        model.addAttribute("contests", phaseOneContests);
        return "phase-one";
    }

    @GetMapping("/phaseTwo")
    public String phaseTwo(Model model) {
        List<Contest> phaseTwoContests = contestService.getPhaseTwoContests();
        model.addAttribute("contests", phaseTwoContests);
        return "phase-two";
    }

    @GetMapping("/finished")
    public String finished(Model model) {
        List<Contest> finishedContests = contestService.geFinishedContests();
        model.addAttribute("contests", finishedContests);
        return "finished";
    }
    @GetMapping("/users")
    public String users(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);

        return "users";
    }
}
