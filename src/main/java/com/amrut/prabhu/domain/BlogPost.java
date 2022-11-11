package com.amrut.prabhu.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Post")
public class BlogPost {
    @Id
    private String postId;
    private String content;

}
