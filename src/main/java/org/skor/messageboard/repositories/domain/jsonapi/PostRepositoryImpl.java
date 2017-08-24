package org.skor.messageboard.repositories.domain.jsonapi;

import io.katharsis.queryspec.QuerySpec;
import io.katharsis.repository.ResourceRepositoryBase;
import io.katharsis.resource.links.PagedLinksInformation;
import io.katharsis.resource.list.ResourceListBase;
import io.katharsis.resource.meta.PagedMetaInformation;
import lombok.Getter;
import lombok.Setter;
import org.skor.messageboard.models.domain.Comment;
import org.skor.messageboard.models.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class PostRepositoryImpl extends ResourceRepositoryBase<Post, Long> {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(124);

    private Map<Long, Post> posts = new HashMap<>();
    private final CommentRepositoryImpl commentRepository;

    @Autowired @Lazy
    public PostRepositoryImpl(CommentRepositoryImpl commentRepository) {
        super(Post.class);
        this.commentRepository = commentRepository;
        save(Post.builder()
                .id(123L)
                .body("Body")
                .subject("Subject")
                .build());
    }

    @Override
    public synchronized void delete(Long id) {
        posts.remove(id);
    }

    @Override
    public synchronized <S extends Post> S save(S post) {
        if (post.getId() == null) {
            post.setId(ID_GENERATOR.getAndIncrement());
        }
        posts.put(post.getId(), post);
        return post;
    }

    @Override
    public Post findOne(Long id, QuerySpec querySpec) {
        Post post = super.findOne(id, querySpec);
        post.setComments(commentRepository.findAll(new QuerySpec(Comment.class)));
        return post;
    }

    @Override
    public synchronized PostList findAll(QuerySpec querySpec) {
        PostList list = new PostList();
        list.setMeta(new PostListMeta());
        list.setLinks(new PostListLinks());
        querySpec.apply(posts.values(), list);
        return list;
    }

    public class PostListMeta implements PagedMetaInformation {
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
    public class PostListLinks implements PagedLinksInformation {
        private String first;
        private String last;
        private String next;
        private String prev;
    }

    public class PostList extends ResourceListBase<Post, PostListMeta, PostListLinks> {  }
}