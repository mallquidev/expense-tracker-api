package com.mallquidev.expense_tracker_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    //3
    private String userName;
    private String password;
}
