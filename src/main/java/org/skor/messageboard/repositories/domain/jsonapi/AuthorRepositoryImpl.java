package org.skor.messageboard.repositories.domain.jsonapi;

import io.katharsis.queryspec.QuerySpec;
import io.katharsis.repository.ResourceRepositoryBase;
import io.katharsis.resource.links.PagedLinksInformation;
import io.katharsis.resource.list.ResourceListBase;
import io.katharsis.resource.meta.PagedMetaInformation;
import lombok.Getter;
import lombok.Setter;
import org.skor.messageboard.models.domain.Author;
import org.skor.messageboard.models.domain.Comment;
import org.skor.messageboard.models.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class AuthorRepositoryImpl extends ResourceRepositoryBase<Author, Long> {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(124);

    private Map<Long, Author> authors = new HashMap<>();
    private final CommentRepositoryImpl commentRepository;

    @Autowired
    public AuthorRepositoryImpl(CommentRepositoryImpl commentRepository) {
        super(Author.class);
        this.commentRepository = commentRepository;
        save(Author.builder()
                .id(123L)
                .name("Author")
                .build());
    }

    @Override
    public synchronized void delete(Long id) {
        authors.remove(id);
    }

    @Override
    public synchronized <S extends Author> S save(S author) {
        if (author.getId() == null) {
            author.setId(ID_GENERATOR.getAndIncrement());
        }
        authors.put(author.getId(), author);
        return author;
    }

//    @Override
//    public Author findOne(Long id, QuerySpec querySpec) {
//        Author post = super.findOne(id, querySpec);
//        post.setComments(commentRepository.findAll(new QuerySpec(Comment.class)));
//        return post;
//    }

    @Override
    public synchronized AuthorList findAll(QuerySpec querySpec) {
        AuthorList list = new AuthorList();
        list.setMeta(new AuthorListMeta());
        list.setLinks(new AuthorListLinks());
        querySpec.apply(authors.values(), list);
        return list;
    }

    public class AuthorListMeta implements PagedMetaInformation {
        private Long totalResourceCount;

        @Override
        public Long getTotalResourceCount() {
            return totalResourceCount;
        }
        @Override
        public void setTotalResourceCount(Long totalResourceCount) {
            this.totalResourceCount = totalResourceCount;
        }
    }

    @Getter
    @Setter
    public class AuthorListLinks implements PagedLinksInformation {
        private String first;
        private String last;
        private String next;
        private String prev;
    }

    public class AuthorList extends ResourceListBase<Author, AuthorListMeta, AuthorListLinks> {  }
}