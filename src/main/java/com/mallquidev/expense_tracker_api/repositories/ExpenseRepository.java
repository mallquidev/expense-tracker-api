package com.mallquidev.expense_tracker_api.repositories;

import com.mallquidev.expense_tracker_api.dto.expense.ExpenseDto;
import com.mallquidev.expense_tracker_api.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    @Query("""
       SELECT new com.mallquidev.expense_tracker_api.dto.expense.ExpenseDto(
           e.id,
           e.productName,
           e.quantity,
           e.expenseDate,
           e.user.userName,
           e.category.name
       )
       FROM Expense e
       JOIN e.user u
       JOIN e.category c
    """)
    List<ExpenseDto> getExpenseByDetails();
}
