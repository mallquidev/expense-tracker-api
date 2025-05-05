package com.mallquidev.expense_tracker_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    //4
    private String userName;
    private String password; //Campos que el usuario debe enviar para registrarse a la BD

}
