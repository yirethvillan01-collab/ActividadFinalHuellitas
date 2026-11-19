package co.com.usc.imperative.jpa.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class DBSecret {
    private String url;
    private String username;
    private String password;

}
