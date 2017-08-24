package org.skor.messageboard.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.katharsis.resource.annotations.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonApiResource(type = "comments")
public class Comment {

    @JsonApiId
    private Long id;

    @JsonProperty
    private String body;

    @JsonIgnore
    private Long postId;

    @JsonApiRelation(opposite = "comments")
    private Author author;

    @JsonApiRelation(opposite = "comments")
    private Post post;
}