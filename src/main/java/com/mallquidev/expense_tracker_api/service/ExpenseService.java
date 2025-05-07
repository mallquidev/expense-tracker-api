package com.mallquidev.expense_tracker_api.service;

import com.mallquidev.expense_tracker_api.dto.expense.CreateExpenseDto;
import com.mallquidev.expense_tracker_api.dto.expense.ExpenseDto;
import com.mallquidev.expense_tracker_api.entities.Category;
import com.mallquidev.expense_tracker_api.entities.Expense;
import com.mallquidev.expense_tracker_api.entities.User;
import com.mallquidev.expense_tracker_api.repositories.CategoryRepository;
import com.mallquidev.expense_tracker_api.repositories.ExpenseRepository;
import com.mallquidev.expense_tracker_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    //list
    public List<ExpenseDto> getAllExpenses() {
        return expenseRepository.getExpenseByDetails();
    }

    //registrar
    public Expense saveExpense(CreateExpenseDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(username)
                .orElseThrow(()-> new RuntimeException("User not found"));

        //buscar la categoria por id
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()-> new RuntimeException("Category not found"));

        //crear la entidad expense
        Expense expense = new Expense();
        expense.setProductName(dto.getProductName());
        expense.setQuantity(dto.getQuantity());
        expense.setExpenseDate(LocalDateTime.now());
        expense.setUser(user);
        expense.setCategory(category);

        //guardar
        return expenseRepository.save(expense);

    }


}
