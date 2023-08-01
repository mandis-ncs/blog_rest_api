package com.mandis.blog.demo.service.impl;

import com.mandis.blog.demo.entity.Comment;
import com.mandis.blog.demo.entity.Post;
import com.mandis.blog.demo.exception.BlogAPIException;
import com.mandis.blog.demo.exception.ResourceNotFoundException;
import com.mandis.blog.demo.payload.CommentDto;
import com.mandis.blog.demo.repository.CommentRepository;
import com.mandis.blog.demo.repository.PostRepository;
import com.mandis.blog.demo.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        comment.setPost(post);

        Comment newComment = commentRepository.save(comment);

        return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentByPostId(long postId) {

        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, long commentId, CommentDto commentRequest) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);
        return mapToDto(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }

        commentRepository.delete(comment);
    }

    private CommentDto mapToDto(Comment comment) {
        return mapper.map(comment, CommentDto.class);
    }

    private Comment mapToEntity(CommentDto commentDto) {
        return mapper.map(commentDto, Comment.class);
    }
}

// Old mode
//private Comment mapToEntity(CommentDto commentDto) {
//    Comment comment = new Comment();
//    comment.setId(commentDto.getId());
//    comment.setName(commentDto.getName());
//    comment.setEmail(commentDto.getEmail());
//    comment.setBody(commentDto.getBody());
//    return comment;
//}