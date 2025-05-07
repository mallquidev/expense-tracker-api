package com.mallquidev.expense_tracker_api.dto.expense;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateExpenseDto {
    private String productName;
    private Integer quantity;
    private Integer categoryId;
}
