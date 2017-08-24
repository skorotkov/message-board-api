package org.skor.messageboard.repositories.domain.jsonapi;

import io.katharsis.repository.RelationshipRepositoryBase;
import org.skor.messageboard.models.domain.Comment;
import org.skor.messageboard.models.domain.Post;
import org.springframework.stereotype.Component;

@Component
public class CommentToPostRelationshipRepository extends RelationshipRepositoryBase<Comment, Long, Post, Long> {
    protected CommentToPostRelationshipRepository() {
        super(Comment.class, Post.class);
    }
}
