package ru.te3ka.telrostestwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.te3ka.telrostestwork.entity.User;
import ru.te3ka.telrostestwork.service.UserService;

import java.util.List;

/**
 * Класс для взаимодействия между командами от пользователя и базой данных через Сервис
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Пост-метод для отправки данных о новом пользователе.
     *
     * @param user - данные нового пользователя.
     * @return OK, если пользователь создан, либо ошибку, если пользователь не создан.
     */
    @PostMapping("/")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createUser = userService.createUser(user);
        return ResponseEntity.ok(createUser);
    }

    /**
     * Гет-метод для получения данных обо всех пользователях.
     *
     * @return OK и список всех пользователей. Либо ошибка, если метод выполнен неверно.
     */
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        return ResponseEntity.ok(userList);
    }

    /**
     * Гет-метод для полученния данных по пользователю, чей номер указан в пути.
     *
     * @param id - номер пользователя.
     * @return данные по пользователю. Либо ошибка, если пользователь не найден.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        System.out.println("Получен запрос на пользователя с id=" + id);
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Пут-метод для замены данных по пользователю, чей номер передаётся в пути.
     * 
     * @param id - номер пользователя, чьи данные нужно обновить.
     * @param userUpdate - данные, на которые нужно обновить.
     * @return обновлённые данные по пользователю. Либо ошибка, если пользователь не найден.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userUpdate) {
        User updatedUser = userService.updateUser(id, userUpdate);
        if (updatedUser != null)
            return ResponseEntity.ok(updatedUser);
        else
            return ResponseEntity.notFound().build();
    }

    /**
     * Делит-метод для удаления пользователя по его номеру.
     *
     * @param id - номер пользователя, которого нужно удалить.
     * @return ничего не возрващает.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
