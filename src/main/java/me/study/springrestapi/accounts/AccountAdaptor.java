package me.study.springrestapi.accounts;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountAdaptor extends User {

    private Account account;

    public AccountAdaptor(Account account) {
        super(account.getEmail(), account.getPassword(), autorites(account.getRoles()));
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    private static Collection<? extends GrantedAuthority> autorites(Set<AccountRole> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.name())).collect(Collectors.toSet());
    }
}
