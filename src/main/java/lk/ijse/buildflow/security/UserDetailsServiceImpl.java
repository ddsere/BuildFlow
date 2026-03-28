package lk.ijse.buildflow.security;

import lk.ijse.buildflow.entity.User;
import lk.ijse.buildflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Database එකෙන් Email එකට අදාළ User ව හොයනවා
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // 2. Spring Security එකට ගැලපෙන විදිහට UserDetails Object එකක් හදලා දෙනවා
        // මෙතන user.getEmail() සහ user.getPassword() වැඩ කරන්නේ Lombok නිසා.
        // Lombok වැඩ නැත්නම් Manual Getters දාන්න අමතක කරන්න එපා.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>() // දැනට Roles (Permissions) හිස්ව යවමු
        );
    }
}