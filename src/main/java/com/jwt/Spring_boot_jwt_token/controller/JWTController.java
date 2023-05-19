package com.jwt.Spring_boot_jwt_token.controller;

import com.jwt.Spring_boot_jwt_token.UserService;
import com.jwt.Spring_boot_jwt_token.jwtutil.JwtUtils;
import com.jwt.Spring_boot_jwt_token.req_res.JwtRequest;
import com.jwt.Spring_boot_jwt_token.req_res.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class JWTController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserService userService;
    @GetMapping("/")
    public ResponseEntity<Map<String, HttpStatus>> home(){
        Map<String, HttpStatus> map = new HashMap<>();
        map.put("vb",HttpStatus.OK);
        return ResponseEntity.ok(map);
    }
    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {
       try {
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           jwtRequest.getName(),
                           jwtRequest.getPassword()
                   )
           );
       }catch (BadCredentialsException badCredentialsException){
           throw  new Exception("Bad Credientials");
       }

       final UserDetails userDetails= userService.loadUserByUsername(jwtRequest.getName());
       final String token = jwtUtils.generateToken(userDetails);
        return new JwtResponse(token);
    }
}
