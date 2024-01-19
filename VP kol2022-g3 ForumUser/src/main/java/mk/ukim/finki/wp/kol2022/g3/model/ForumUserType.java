package mk.ukim.finki.wp.kol2022.g3.model;

import org.springframework.security.core.GrantedAuthority;

public enum ForumUserType implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_REGULAR,
    ROLE_GOLDEN;

    @Override
    public String getAuthority() {
        return name();
    }
}