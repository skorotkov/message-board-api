package org.skor.messageboard.repositories.domain.jsonapi;

import io.katharsis.repository.RelationshipRepositoryBase;
import org.skor.messageboard.models.domain.Author;
import org.skor.messageboard.models.domain.Comment;
import org.skor.messageboard.models.domain.Post;
import org.springframework.stereotype.Component;

@Component
public class CommentToAuthorRelationshipRepository extends RelationshipRepositoryBase<Comment, Long, Author, Long> {
    protected CommentToAuthorRelationshipRepository() {
        super(Comment.class, Author.class);
    }
}
