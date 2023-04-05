package io.pixelsdb.pixels.rover.security;

import io.pixelsdb.pixels.rover.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static java.util.Objects.requireNonNull;

/**
 * Created at: 4/5/23
 * Author: hank
 */
public class PixelsUserDetails implements UserDetails
{
    private final User user;

    public PixelsUserDetails(User user)
    {
        this.user = requireNonNull(user, "user is null");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return null;
    }

    @Override
    public String getPassword()
    {
        return user.getPassword();
    }

    @Override
    public String getUsername()
    {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    public String getName()
    {
        return user.getName();
    }
}
