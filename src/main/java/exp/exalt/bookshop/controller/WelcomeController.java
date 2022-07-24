package exp.exalt.bookshop.controller;

import exp.exalt.bookshop.model.AuthenticationRequest;
import exp.exalt.bookshop.model.AuthenticationResponse;
import exp.exalt.bookshop.service.MyUserDetailsService;
import exp.exalt.bookshop.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

// this just for test Jwt, will to implement full code on the soon

@RestController
public class WelcomeController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    MyUserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtTokenUtil;

    @GetMapping("/")
    public String welcome() {
        return "Welcome";
    }

    @GetMapping("/admin")
    public String welcomeAdmin() {
        return "<h1>Welcome Admin</h1>";
    }

    @GetMapping("/user")
    public String welcomeUser() {
        return "<h1>Welcome User</h1>";
    }

    @PostMapping(value = "/authentication")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException badCredentialsException) {
            throw new Exception("Incorrect username or password", badCredentialsException);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

}
