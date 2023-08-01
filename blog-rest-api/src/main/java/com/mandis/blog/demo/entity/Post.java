package com.mandis.blog.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "posts", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})})

public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();
}


/*
* {
    "title" : "algum",
    "description" : "algum",
    "content" : "algum"
}
* */
// @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
// cascade -> determine that all operations of Post is applicable for Comments
// orphanRemoval -> determine that if the parent Post is excluded of bd, Comment will too

