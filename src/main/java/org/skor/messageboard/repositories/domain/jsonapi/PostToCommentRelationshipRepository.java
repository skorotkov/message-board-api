package org.skor.messageboard.repositories.domain.jsonapi;

import io.katharsis.repository.RelationshipRepositoryBase;
import org.skor.messageboard.models.domain.Comment;
import org.skor.messageboard.models.domain.Post;
import org.springframework.stereotype.Component;

@Component
public class PostToCommentRelationshipRepository extends RelationshipRepositoryBase<Post, Long, Comment, Long> {
    protected PostToCommentRelationshipRepository() {
        super(Post.class, Comment.class);
    }
}
