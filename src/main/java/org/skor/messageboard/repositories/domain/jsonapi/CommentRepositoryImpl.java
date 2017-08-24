package org.skor.messageboard.repositories.domain.jsonapi;

import io.katharsis.core.internal.utils.PreconditionUtil;
import io.katharsis.errorhandling.exception.ResourceNotFoundException;
import io.katharsis.queryspec.FilterOperator;
import io.katharsis.queryspec.FilterSpec;
import io.katharsis.queryspec.QuerySpec;
import io.katharsis.repository.ResourceRepositoryBase;
import io.katharsis.resource.links.PagedLinksInformation;
import io.katharsis.resource.list.ResourceListBase;
import io.katharsis.resource.meta.PagedMetaInformation;
import io.katharsis.resource.registry.RegistryEntry;
import lombok.Getter;
import lombok.Setter;
import org.skor.messageboard.models.domain.Comment;
import org.skor.messageboard.models.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class CommentRepositoryImpl extends ResourceRepositoryBase<Comment, Long> {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(124);

    private Map<Long, Comment> comments = new HashMap<>();
    private final PostRepositoryImpl postRepository;

    @Autowired
    public CommentRepositoryImpl(PostRepositoryImpl postRepository) {
        super(Comment.class);
        this.postRepository = postRepository;

        save(Comment.builder()
                .id(123L)
                .body("Comment Body")
                .postId(123L)
                .build());
    }

    @Override
    public synchronized void delete(Long id) {
        comments.remove(id);
    }

    @Override
    public synchronized <S extends Comment> S save(S comment) {
        if (comment.getId() == null) {
            comment.setId(ID_GENERATOR.getAndIncrement());
        }
        comments.put(comment.getId(), comment);
        return comment;
    }

    @Override
    public Comment findOne(Long id, QuerySpec querySpec) {
        Comment comment = super.findOne(id, querySpec);
        comment.setPost(postRepository.findOne(comment.getPostId(), new QuerySpec(Post.class)));
        return comment;
    }

    @Override
    public synchronized CommentList findAll(QuerySpec querySpec) {
        CommentList list = new CommentList();
        list.setMeta(new CommentListMeta());
        list.setLinks(new CommentListLinks());
        querySpec.apply(comments.values(), list);
        return list;
    }

    public class CommentListMeta implements PagedMetaInformation {
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
    public class CommentListLinks implements PagedLinksInformation {
        private String first;
        private String last;
        private String next;
        private String prev;
    }

    public class CommentList extends ResourceListBase<Comment, CommentListMeta, CommentListLinks> {  }
}