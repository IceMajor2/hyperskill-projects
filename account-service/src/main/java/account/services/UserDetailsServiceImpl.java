package account.services;

import account.repositories.UserRepository;
import account.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import account.models.User;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmailIgnoreCase(email);
        return user.map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException
                        ("User '%s' was not found".formatted(email)));
    }
}