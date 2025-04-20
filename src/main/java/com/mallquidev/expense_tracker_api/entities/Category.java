package com.mallquidev.expense_tracker_api.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categorie")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Expense> expenses;

    public Category() {
    }

    public Category(Integer id, String name, List<Expense> expenses) {
        this.id = id;
        this.name = name;
        this.expenses = expenses;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }
}
