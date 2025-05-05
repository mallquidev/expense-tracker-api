package com.mallquidev.expense_tracker_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    //10.1
    //datos que le damos al frontend
    private String token;//el token generado
    private String userName;//el nombre de usuario
    private String role;//el rol
}
