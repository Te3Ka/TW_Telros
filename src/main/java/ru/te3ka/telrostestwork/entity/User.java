package ru.te3ka.telrostestwork.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Класс для хранения данных по пользователю. Содержит следующие поля:
 * -- ID - значение типа Long, обязательное поле, автогенерируемое, уникальное
 * -- Фамилия - значение типа String, обязательное поле
 * -- Имя - значение типа String, обязательное поле
 * -- Отчество - значение типа String, не обязательное поле
 * -- Дата рождения - значение типа LocalDate, обязательное поле
 * -- Электронная почта - значение типа String, обязательное поле, уникальное
 * -- Номер телефона - значение типа String, обязательное поле, уникальное
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    @Column
    private String middleName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;
}
