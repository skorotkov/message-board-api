package org.skor.messageboard.controllers.oauth;

import org.skor.messageboard.models.oauth.TokenRequest;
import org.skor.messageboard.models.oauth.TokenResponse;
import org.skor.messageboard.services.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.async.DeferredResult;


@RestController
public class OAuth2Controller {
    private static final Logger log = LoggerFactory.getLogger(OAuth2Controller.class);

    private final OAuthService oAuthService;

    @Autowired
    public OAuth2Controller(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @RequestMapping(value = "/github-login", method = RequestMethod.POST)
    @CrossOrigin
    @ResponseBody
    public DeferredResult<ResponseEntity<TokenResponse>> getGitHubToken(@RequestBody TokenRequest tokenRequest) {
        DeferredResult<ResponseEntity<TokenResponse>> result = new DeferredResult<>();
        oAuthService
            .getTokenAsync(tokenRequest)
            .thenAccept(tokenResponse -> {
                if (result.isSetOrExpired()) {
                    log.warn("processing of non-blocking request already expired");
                } else {
                    if (tokenResponse != null) {
                        log.info("token obtained: token={}; request={}", tokenResponse.toString(), tokenRequest);
                        result.setResult(new ResponseEntity<>(tokenResponse, HttpStatus.OK));
                    } else {
                        log.error("token wasn't obtained: request={}", tokenRequest);
                        result.setResult(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                    }
                }
            });

        log.trace("servlet thread released");

        return result;
    }
}
