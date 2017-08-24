package org.skor.messageboard.repositories.domain.jsonapi;

import io.katharsis.repository.RelationshipRepositoryBase;
import org.skor.messageboard.models.domain.Author;
import org.skor.messageboard.models.domain.Post;
import org.springframework.stereotype.Component;

@Component
public class AuthorToPostRelationshipRepository extends RelationshipRepositoryBase<Author, Long, Post, Long> {
    protected AuthorToPostRelationshipRepository() {
        super(Author.class, Post.class);
    }
}
