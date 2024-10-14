package ru.te3ka.telrostestwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.te3ka.telrostestwork.entity.User;
import ru.te3ka.telrostestwork.repository.UserRepository;
import ru.te3ka.telrostestwork.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * Класс для взаимодействия между командами от пользователя и базой данных через Сервис
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final String UPLOAD_PHOTO_DIR = "users_photos/";

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

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

    /**
     * Пост-метод для загрузки фотографии пользователя отдельно от создания самого пользователя.
     *
     * @param id - номер пользователя, к которому нужно прикрепить фотографию.
     * @param file - файл, который необходимо прикрепить в качестве фотографии к пользователю.
     * @return сообщение об успехе загрузки фотографии, либо сообщение об ошибке.
     * @throws IOException непредвиденные ошибки.
     */
    @PostMapping("/{id}/photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable Long id,
                                              @RequestParam("file")MultipartFile file) throws IOException {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_PHOTO_DIR + fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            user.setPathToPhoto(filePath.toString());
            userRepository.save(user);

            return ResponseEntity.ok("Photo upload successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    /**
     * Гет-метод для получения фотографии пользователя по его номеру.
     *
     * @param id - номер пользователя, чью фотографию нужно получить.
     * @return фотография пользователя, если она существет. В противном случае ничего.
     * @throws IOException непредвиденные ошибки.
     */
    @GetMapping("/{id}/photo")
    public ResponseEntity<Resource> getUserPhoto(@PathVariable Long id) throws IOException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String photoPath = user.getPathToPhoto();
            if (photoPath != null && Files.exists(Paths.get(photoPath))) {
                Resource file = new UrlResource(Paths.get(photoPath).toUri());
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(file);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Делит-метод для удаления фотографии пользователя по его номеру.
     *
     * @param id - номер пользователя, чью фотографию нужно удалить.
     * @return ОК, если фотография удалено успешно, либо NOT FOUND, если пользователь или фотография не найдены.
     * @throws IOException непредвиденные ошибки.
     */
    @DeleteMapping("/{id}/photo")
    public ResponseEntity<String> deletePhoto(@PathVariable Long id) throws IOException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String photoPath = user.getPathToPhoto();
            if (photoPath != null && Files.exists(Paths.get(photoPath))) {
                Files.delete(Paths.get(photoPath));
                user.setPathToPhoto(null);
                userRepository.save(user);
                return ResponseEntity.ok("Photo deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Photo not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
