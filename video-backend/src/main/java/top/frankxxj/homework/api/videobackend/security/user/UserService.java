package top.frankxxj.homework.api.videobackend.security.user;

public interface UserService {
    User create(UserDto userDto);

    void updatePassword(UserDto userDto);

    Boolean checkUsername(String username);

    Boolean existAnyUser();
}
