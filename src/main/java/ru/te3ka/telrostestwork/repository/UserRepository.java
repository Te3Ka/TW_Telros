package ru.te3ka.telrostestwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.te3ka.telrostestwork.entity.User;

/**
 * Стандартный автогенерируемый интерфейс для работы с репозиторием на основе JpaRepository
 * Работает с ключом User по значению ID.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}