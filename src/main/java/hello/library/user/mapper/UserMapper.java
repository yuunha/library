package hello.library.user.mapper;

import hello.library.user.dto.UserRequest;
import hello.library.user.dto.UserResponse;
import hello.library.user.entity.User;


public class UserMapper {

    //userRequest -> User
    public static User toEntity(UserRequest userRequest) {
        return User.builder()
            .username(userRequest.getUsername())
            .email(userRequest.getEmail())
            .build();
    }
    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
            .userId(user.getUserId())
            .username(user.getUsername())
            .email(user.getEmail())
            .build();
    }
}
