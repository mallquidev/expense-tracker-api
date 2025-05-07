package com.mallquidev.expense_tracker_api.dto.expense;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDto {
    private Integer expenseId;
    private String productName;
    private Integer quantity;
    private LocalDateTime expenseDate;
    private String userName;
    private String categoryName;
}
