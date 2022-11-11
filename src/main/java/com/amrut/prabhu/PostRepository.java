package com.amrut.prabhu;

import com.amrut.prabhu.domain.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<BlogPost, String> {
}
