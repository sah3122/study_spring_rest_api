package me.study.springrestapi.configs;

import me.study.springrestapi.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AccountService accountService;

    @Autowired
    TokenStore tokenStore;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(passwordEncoder); // Secret 설정시 인코딩 해서 관리.
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory() // jdbc를 사용해 db에서 관리하는게 이상적이다.
                .withClient("myApp") // Client Id
                .authorizedGrantTypes("password", "refresh_token")//refresh_token을 받기위해
                .scopes("read","write")
                .secret(this.passwordEncoder.encode("pass")) // app secret
                .accessTokenValiditySeconds(10 * 60)
                .refreshTokenValiditySeconds(6 * 10 * 60);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager) // authenticationManager : user인증 정보를 알고 있는 객체
                .userDetailsService(accountService)
                .tokenStore(tokenStore);

    }
}
