package org.skor.messageboard.repositories.domain.jsonapi;

import io.katharsis.repository.RelationshipRepositoryBase;
import org.skor.messageboard.models.domain.Author;
import org.skor.messageboard.models.domain.Comment;
import org.skor.messageboard.models.domain.Post;
import org.springframework.stereotype.Component;

@Component
public class AuthorToCommentRelationshipRepository extends RelationshipRepositoryBase<Author, Long, Comment, Long> {
    protected AuthorToCommentRelationshipRepository() {
        super(Author.class, Comment.class);
    }
}
