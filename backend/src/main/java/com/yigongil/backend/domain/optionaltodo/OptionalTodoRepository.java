package com.yigongil.backend.domain.optionaltodo;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface OptionalTodoRepository extends Repository<OptionalTodo, Long> {

    Optional<OptionalTodo> findById(Long id);

    OptionalTodo save(OptionalTodo optionalTodo);

    void deleteById(Long id);
}
