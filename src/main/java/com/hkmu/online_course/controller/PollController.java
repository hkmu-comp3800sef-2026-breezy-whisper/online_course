package com.hkmu.online_course.controller;

import com.hkmu.online_course.model.Poll;
import com.hkmu.online_course.model.Vote;
import com.hkmu.online_course.service.IPollService;
import com.hkmu.online_course.service.IVoteService;
import com.hkmu.online_course.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller for Poll operations.
 *
 * Security is enforced at METHOD LEVEL via @PreAuthorize:
 * - create(), delete() require TEACHER role
 * - view(), vote() only require authentication (any role)
 */
@Controller
@RequestMapping("/poll")
public class PollController {

    private final IPollService pollService;
    private final IVoteService voteService;
    private final ICommentService commentService;

    @Autowired
    public PollController(IPollService pollService,
                         IVoteService voteService,
                         ICommentService commentService) {
        this.pollService = pollService;
        this.voteService = voteService;
        this.commentService = commentService;
    }

    // ========== Public/Authenticated User Endpoints ==========

    @GetMapping("/list")
    public String list(Model model) {
        List<Poll> polls = pollService.findAll();
        model.addAttribute("polls", polls);
        return "poll/list";
    }

    @GetMapping("/{pollId}")
    public String view(@PathVariable Long pollId,
                       @AuthenticationPrincipal UserDetails userDetails,
                       Model model) {
        Poll poll = pollService.findById(pollId)
                .orElseThrow(() -> new IllegalArgumentException("Poll not found: " + pollId));

        List<Vote> votes = voteService.findByPollId(pollId);
        Map<Integer, Long> voteCounts = votes.stream()
                .collect(Collectors.groupingBy(Vote::getSelectedOption, Collectors.counting()));

        String userVote = null;
        if (userDetails != null) {
            userVote = voteService.findByPollIdAndUsername(pollId, userDetails.getUsername())
                    .map(v -> String.valueOf(v.getSelectedOption()))
                    .orElse(null);
        }

        model.addAttribute("poll", poll);
        model.addAttribute("voteCounts", voteCounts);
        model.addAttribute("userVote", userVote);
        model.addAttribute("comments", commentService.findByTarget(pollId, "POLL"));
        return "poll/view";
    }

    @PostMapping("/{pollId}/vote")
    @PreAuthorize("isAuthenticated()")
    public String vote(@PathVariable Long pollId,
                       @RequestParam Integer selectedOption,
                       @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            voteService.vote(userDetails.getUsername(), pollId, selectedOption);
        }
        return "redirect:/poll/" + pollId;
    }

    // ========== Teacher Only Endpoints ==========

    @GetMapping("/create")
    @PreAuthorize("hasRole('TEACHER')")
    public String createForm() {
        return "poll/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('TEACHER')")
    public String create(@RequestParam String question,
                        @RequestParam String option1,
                        @RequestParam String option2,
                        @RequestParam String option3,
                        @RequestParam String option4,
                        @RequestParam String option5,
                        @RequestParam(required = false) Long closeTime) {
        pollService.create(question, option1, option2, option3, option4, option5,
                closeTime != null ? closeTime : -1L);
        return "redirect:/poll/list";
    }

    @PostMapping("/{pollId}/delete")
    @PreAuthorize("hasRole('TEACHER')")
    public String delete(@PathVariable Long pollId) {
        pollService.deleteById(pollId);
        return "redirect:/poll/list";
    }
}
