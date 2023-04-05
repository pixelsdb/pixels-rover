package io.pixelsdb.pixels.rover.security;

import io.pixelsdb.pixels.rover.model.User;
import io.pixelsdb.pixels.rover.model.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created at: 4/5/23
 * Author: hank
 */
public class PixelsUserDetailsService implements UserDetailsService
{
    private final UserRepository userRepository;

    public PixelsUserDetailsService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepository.findByEmail(username);
        if (user == null)
        {
            throw new UsernameNotFoundException("User not found");
        }
        return new PixelsUserDetails(user);
    }
}
