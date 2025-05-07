package com.mallquidev.expense_tracker_api.controller;

import com.mallquidev.expense_tracker_api.dto.expense.CreateExpenseDto;
import com.mallquidev.expense_tracker_api.dto.expense.ExpenseDto;
import com.mallquidev.expense_tracker_api.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public List<ExpenseDto> getExpenseDetails() {
        return expenseService.getAllExpenses();
    }

    @PostMapping
    public ResponseEntity<Void> createExpense(@RequestBody CreateExpenseDto dto) {
        expenseService.saveExpense(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }
}
