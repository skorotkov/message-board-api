package org.skor.messageboard.models.oauth;

import lombok.*;

@Getter
@Setter
@ToString
public class TokenRequest {
    public String client_id;
    public String client_secret;
    public String code;
}
