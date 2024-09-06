package com.example.demo.controllers.mvc;

import com.example.demo.models.Contest;
import com.example.demo.models.Type;
import com.example.demo.models.User;
import com.example.demo.models.dto.ContestDto;
import com.example.demo.models.dto.JuryDTO;
import com.example.demo.models.mappers.ContestMapper;
import com.example.demo.services.CategoryService;
import com.example.demo.services.ContestService;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/admin")
public class OrganizerDashboardMVCController {
    private final CategoryService categoryService;
    private final ContestService contestService;
    private final UserService userService;
    private final ContestMapper contestMapper;

    public OrganizerDashboardMVCController(CategoryService categoryService, ContestService contestService, UserService userService, ContestMapper contestMapper) {
        this.categoryService = categoryService;
        this.contestService = contestService;
        this.userService = userService;
        this.contestMapper = contestMapper;
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
                                Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.contestDto", bindingResult);
            redirectAttributes.addFlashAttribute("contestDto", contestDto);
            return "create-contest";
        }
        User user = userService.getUserByEmail(principal.getName());
        Contest contest = contestMapper.createContestFromDto(contestDto, user);
        List<User> users = userService.getAllUsers();
        contest = contestService.createContest(contest, user);
        redirectAttributes.addAttribute("id", contest.getId());

        return "redirect:/admin/editContest/{id}";
    }

    @GetMapping("/editContest/{id}")
    public String editContest(@PathVariable long id, Model model) {
        Contest contest = contestService.getContestById(id);
        model.addAttribute("contestDto", contest);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("types", Type.values());
        return "edit-contest";
    }

    @PostMapping("/editContest/{id}/addJuryMember")
    public String addJuryMember(@PathVariable long id, @RequestParam String email, Model model, Principal principal) {
        User jury = userService.getUserByEmail(email);
        User user = userService.getUserByEmail(principal.getName());
        Contest contest = contestService.addJury(id, jury, user);
        model.addAttribute("id", contest.getId());
        return "redirect:/admin/editContest/{id}";
    }
    @PostMapping("/editContest/{id}/addInvitationalUser")
    public String addInvitationalUser(@PathVariable long id, @RequestParam String email, Model model, Principal principal) {
        User userToInvite = userService.getUserByEmail(email);
        User user = userService.getUserByEmail(principal.getName());
        Contest contest = contestService.inviteUserToContest(id, userToInvite, user);
        model.addAttribute("id", contest.getId());
        return "redirect:/admin/editContest/{id}";
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
        return "phase-one";
    }

    @GetMapping("/finished")
    public String finished(Model model) {
        List<Contest> finishedContests = contestService.geFinishedContests();
        model.addAttribute("contests", finishedContests);
        return "phase-one";
    }
}
