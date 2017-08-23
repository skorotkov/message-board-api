package org.skor.messageboard.services.oauth;

import org.skor.messageboard.models.oauth.TokenRequest;
import org.skor.messageboard.models.oauth.TokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class OAuthService {
    private static final Logger log = LoggerFactory.getLogger(OAuthService.class);

    @Value("${security.oauth2.client.clientId}")
    String clientId;

    @Value("${security.oauth2.client.clientSecret}")
    String clientSecret;

    public CompletableFuture<TokenResponse> getTokenAsync(TokenRequest request) {
        AsyncRestTemplate restTemplate = new AsyncRestTemplate();
        request.client_id = clientId;
        request.client_secret = clientSecret;
        return toCompletable(restTemplate.postForEntity(
            "https://github.com/login/oauth/access_token",
            new HttpEntity<>(request), TokenResponse.class))
            .thenApply(responseEntity -> {
                if (responseEntity.getStatusCode() != HttpStatus.OK) {
                    log.error("can not get token from GitHub: status code= {} {}; request={}",
                            responseEntity.getStatusCodeValue(), responseEntity.getStatusCode(), request.toString());
                    return null;
                } else {
                    TokenResponse response = responseEntity.getBody();
                    log.info("token obtained from GitHub: response={}", response);
                    return responseEntity.getBody();
                }
            });
    }

    private static <T> CompletableFuture<T> toCompletable(final ListenableFuture<T> listenableFuture) {
        //create an instance of CompletableFuture
        CompletableFuture<T> completable = new CompletableFuture<T>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                // propagate cancel to the listenable future
                boolean result = listenableFuture.cancel(mayInterruptIfRunning);
                super.cancel(mayInterruptIfRunning);
                return result;
            }
        };

        // add callback
        listenableFuture.addCallback(new ListenableFutureCallback<T>() {
            @Override
            public void onSuccess(T result) {
                completable.complete(result);
            }

            @Override
            public void onFailure(Throwable t) {
                completable.completeExceptionally(t);
            }
        });
        return completable;
    }
}
