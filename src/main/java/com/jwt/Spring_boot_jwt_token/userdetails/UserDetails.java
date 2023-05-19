package com.jwt.Spring_boot_jwt_token.userdetails;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDetails {
    String name;
    String password;
}
