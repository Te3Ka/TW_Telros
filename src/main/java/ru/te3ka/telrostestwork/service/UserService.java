package ru.te3ka.telrostestwork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.te3ka.telrostestwork.entity.User;
import ru.te3ka.telrostestwork.repository.UserRepository;

import java.util.List;
import java.util.Optional;


/**
 * Сервис для работы с базой данных User.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Метод для создания нового пользователя.
     *
     * @param user - заполненный профиль пользователя.
     * @return заполненный профиль пользователя, если он создан успешно. В противном случае, значение null.
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Метод для получения данных по всем пользователяи.
     *
     * @return список всех пользователей, хранящихся в базе данных.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Метод для получения данных конкретного пользователя по его номеру id.
     *
     * @param id - номер пользователя.
     * @return все данные по пользователю.
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Метод для изменения данных по уже существующему пользователю.
     *
     * @param id - номер пользователя, данные которого нужно изменить.
     * @param userUpdate - новые данные пользователя, на которые будет проводиться замена.
     * @return изменённые данные пользователя. Если происходит ошибка, то возвращается значение null.
     */
    public User updateUser(Long id, User userUpdate) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setLastName(userUpdate.getLastName());
            user.setFirstName(userUpdate.getFirstName());
            user.setMiddleName(userUpdate.getMiddleName());
            user.setDateOfBirth(userUpdate.getDateOfBirth());
            user.setEmail(userUpdate.getEmail());
            user.setPhoneNumber(userUpdate.getPhoneNumber());
            return userRepository.save(user);
        }
        return null;
    }

    /**
     * Метод для удаления пользователя по номеру.
     *
     * @param id - номер пользователя для удаления.
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
