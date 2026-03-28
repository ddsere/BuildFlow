package lk.ijse.buildflow.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService; // මේක අපි ඊළඟට හදනවා

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Request එකේ Header එකෙන් Authorization කොටස ගන්නවා
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // 2. Token එක පටන් ගන්නේ "Bearer " වලින්ද කියලා බලනවා
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // "Bearer " කෑල්ල අයින් කරලා token එක විතරක් ගන්නවා
            username = jwtUtil.extractUsername(token); // Token එකෙන් email/username එක ගන්නවා
        }

        // 3. Username එකක් තියෙනවා නම් සහ දැනටමත් ලොග් වෙලා නැත්නම් විතරක් ඉස්සරහට යනවා
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Database එකෙන් User ගේ විස්තර ගන්නවා
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 4. Token එක Valid ද කියලා බලනවා
            if (jwtUtil.validateToken(token, userDetails)) {

                // Token එක හරි නම්, Spring Security එකට කියනවා "මේ User ව ඇතුළට ගන්න" කියලා
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 5. ඊළඟ Filter එකට Request එක යවනවා (වැඩේ ඉවරයි)
        filterChain.doFilter(request, response);
    }
}