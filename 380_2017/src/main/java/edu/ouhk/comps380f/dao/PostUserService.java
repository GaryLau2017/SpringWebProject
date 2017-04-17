package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.PostUser;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PostUserService implements UserDetailsService {
    @Autowired
    PostUserRepository postUserRepo;
    
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        PostUser postUser = postUserRepo.findByUsername(username);
        if (postUser == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found.");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : postUser.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return new User(postUser.getUsername(), postUser.getPassword(), authorities);
    }
    
}
