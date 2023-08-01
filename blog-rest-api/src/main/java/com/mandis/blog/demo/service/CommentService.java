package com.mandis.blog.demo.service;

import com.mandis.blog.demo.payload.CommentDto;
import org.hibernate.annotations.Comment;

import java.util.List;

public interface CommentService {
    CommentDto createComment(long postId, CommentDto commentDto);

    List<CommentDto> getCommentByPostId(long postId);

    CommentDto getCommentById(Long postId, Long commentId);

    CommentDto updateComment(Long postId, long commentId, CommentDto commentRequest);

    void deleteComment(Long postId, Long commentId);
}
