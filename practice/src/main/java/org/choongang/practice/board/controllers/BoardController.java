package org.choongang.practice.board.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.practice.board.entities.Post;
import org.choongang.practice.board.services.PostDeleteService;
import org.choongang.practice.board.services.PostInfoService;
import org.choongang.practice.board.services.PostSaveService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final PostInfoService postInfoService;
    private final PostSaveService postSaveService;
    private final PostDeleteService postDeleteService;

    // 게시글 목록 조회
    @GetMapping("/list")
    public String list(Model model) {
        List<Post> posts =  postInfoService.getPosts();
        model.addAttribute("posts", posts);

        return "board/list";
    }

    // 게시글 1개 조회
    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable("id") Long id, Model model) {
        Post post =  postInfoService.getPost(id);
        model.addAttribute("post", post);

        return "board/post";
    }

    // 게시글 작성
    @GetMapping("/write")
    public String writePost(@ModelAttribute RequestPost form, Model model) {
        // @ModelAttribute RequestPost form -> 이거 까먹지 말자...제발
        model.addAttribute("post", new Post());

        return "board/write";
    }

    // 게시글 수정
    @GetMapping("/update/{id}")
    public String editPost(@PathVariable("id") Long id, Model model) {
        RequestPost post =  postInfoService.getForm(id);
        model.addAttribute("requestPost", post);

        return "board/update";
    }

    // 게시글 삭제
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id, Model model) {
        postDeleteService.delete(id);

        return "redirect:/board/list";
    }

    // 게시글 저장✨
    @PostMapping("/save")
    public String savePost(@Valid RequestPost form, Errors errors) {
        String mode = form.getId() == null ? "write" : "update";

        if (errors.hasErrors()) {
            return "board/" + mode;
        }

        /*
        if (form.getId() != null && postInfoService.getPost(form.getId()) != null) {
            postSaveService.save(form);
        } else {
            postSaveService.save(form);
        }
         */

        postSaveService.save(form);
  
        return "redirect:/board/list";
    }
}
