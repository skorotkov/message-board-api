package org.skor.messageboard.models.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiRelation;
import io.katharsis.resource.annotations.JsonApiResource;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@JsonApiResource(type = "authors")
public class Author {
    @JsonApiId
    private Long id;

    @JsonProperty
    private String name;

    @JsonApiRelation(opposite = "author")
    private List<Comment> comments;

    @JsonApiRelation(opposite = "author")
    private List<Post> posts;
}
