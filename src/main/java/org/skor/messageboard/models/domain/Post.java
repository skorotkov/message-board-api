package org.skor.messageboard.models.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.katharsis.resource.annotations.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@JsonApiResource(type = "posts")
public class Post {

    @JsonApiId
    private Long id;

    @JsonProperty
    private String subject;

    @JsonProperty
    private String body;

    @JsonApiRelation(opposite = "posts")
    private Author author;

    @JsonApiRelation(opposite = "post")
    private List<Comment> comments;
}
