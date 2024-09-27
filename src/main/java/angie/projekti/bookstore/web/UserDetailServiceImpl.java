package angie.projekti.bookstore.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import angie.projekti.bookstore.model.AppUser;
import angie.projekti.bookstore.model.AppUserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser currentUser = appUserRepository.findByUsername(username);
        if (currentUser == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                username,
                currentUser.getPasswordHash(),
                AuthorityUtils.createAuthorityList(currentUser.getRole()));
    }
}