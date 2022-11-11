package com.amrut.prabhu;

import com.amrut.prabhu.domain.BlogPost;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WebController {
    private final PostRepository postRepository;
    public WebController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/blog")
    public List<BlogPost> get(){
        return postRepository.findAll();
    }

    @PostMapping("/blog")
    public List<BlogPost> save(){
        return postRepository.findAll();
    }
}
