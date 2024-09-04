package com.example.demo.controllers.mvc;

import com.example.demo.models.dto.ContestViewDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contests")
public class ContestMVCController {

    @GetMapping()
    public String contests() {
        return "contests";
    }
    @GetMapping("/{id}")
    public String contest() {
        return "contest-details";
    }
//    @PutMapping("/{id}")
//    public ContestViewDto updateContest(@PathVariable Long id) {
//        return new ContestViewDto();
//    }

}
