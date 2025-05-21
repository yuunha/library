package hello.library.user.service;

import org.springframework.stereotype.Service;

import hello.library.common.exception.BusinessException;
import hello.library.common.exception.ErrorCode;
import hello.library.user.dto.UserRequest;
import hello.library.user.entity.User;
import hello.library.user.mapper.UserMapper;
import hello.library.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public long registerUser(UserRequest request) {
        User user = UserMapper.toEntity(request);
        return userRepository.save(user).getUserId();
    }

    public void deleteUserById(long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }else{
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
    }
}
