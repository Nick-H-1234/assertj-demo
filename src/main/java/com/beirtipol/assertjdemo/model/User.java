package com.beirtipol.assertjdemo.model;

import lombok.*;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private String username;
    private String password;
    private String address;
}
