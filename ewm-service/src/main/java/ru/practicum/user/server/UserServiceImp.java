package ru.practicum.user.server;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.AlreadyExistsException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImp implements UserService {

    private final UserStorage userStorage;


    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        try {
            return UserMapper.toUserDto(userStorage.saveAndFlush(user));
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException(String.format(
                    "Пользователь с %s уже зарегистрирован", userDto.getEmail()
            ));
        }
    }

    @Override
    public List<UserDto> get(Integer[] ids, Integer from, Integer size) {
        int page = from / size;

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        List<User> users;

        if (ids[0] == 0) {
            Page<User> userPage = userStorage.findAll(pageable);
            users = userPage.getContent();
        } else {
            users = userStorage.findAllByIds(ids, pageable);
        }
        return UserMapper.toUserDtoList(users);
    }

    @Override
    public void delete(Integer userId) {
        if (!userStorage.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        userStorage.deleteById(userId);
    }
}
