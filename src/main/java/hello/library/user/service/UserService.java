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
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL , "이미 존재하는 email입니다.");
        }
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

    public User getUserById(long id) {
        if(userRepository.existsById(id)) {
            return userRepository.findById(id).get();
        }
        else{
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
    }
}
