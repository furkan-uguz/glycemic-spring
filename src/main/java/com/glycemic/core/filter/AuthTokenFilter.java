
package com.glycemic.core.filter;

import com.glycemic.core.handler.JwtExceptionHandler;
import com.glycemic.core.jwt.JwtUtils;
import com.glycemic.core.security.UserDetailsServiceImpl;
import com.glycemic.entity.model.glycemic.JwtSession;
import com.glycemic.entity.model.glycemic.Users;
import com.glycemic.entity.repository.glycemic.JwtSessionRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@NoArgsConstructor(force = true)
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JwtExceptionHandler jwtException;

    @Autowired
    private JwtSessionRepository jwtRepo;

    private List<String> authUrls;

    private static final String[] REGEX_URLS = {
            "\\/food\\/get\\?name=[\\s\\S]\\w{1,}\\w[&]{1}status=[\\s\\S]\\w{1,}"
    };

    public AuthTokenFilter(String... authUrls) {
        this.authUrls = Arrays.asList(authUrls);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        String fullPath = path + "?" + request.getQueryString();
        boolean upper = false;
        for (String s : authUrls) {
            if (s.contains(path) || s.equals("/**")) {
                upper = true;
                break;
            }
        }

        for (String s : REGEX_URLS) {
            if (fullPath.matches(s)) {
                upper = true;
                break;
            }
        }

        return !upper;
    }


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && !jwt.isEmpty()) {
                jwtUtils.validateToken(jwt);
                Optional<JwtSession> jwtOpt = jwtRepo.findByJwttoken(jwt);

                if (jwtOpt.isPresent()) {
                    JwtSession session = jwtOpt.get();
                    String fingerprint = request.getHeader("Fingerprint");
                    String id = jwtUtils.getIdFromJwtToken(jwt);
                    String email = jwtUtils.getEmailFromJwtToken(jwt);
                    Users user = session.getUsers();

                    if (!session.getFingerPrint().equals(fingerprint)) {
                        log.info("Request is coming from different client except that logged client.{from: {}, logged: {}}", fingerprint, session.getFingerPrint());
                    }

                    if (user.getId() == Long.parseLong(id) && user.getEmail().equals(email)) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        } catch (MalformedJwtException | UnsupportedJwtException | ExpiredJwtException e) {
            jwtException.jwtException(response, e);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}