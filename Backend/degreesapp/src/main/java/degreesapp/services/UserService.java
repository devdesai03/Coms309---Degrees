package degreesapp.services;


import degreesapp.models.User;

import java.util.List;

/**
 * @author Mohammed Abdalgader
 */

public interface UserService {
    public User saveUser(User user);

    public List<User> fetchUserList();

    public User fetchUserById(Long userId);

    public void deleteUserById(Long userId);

    public User updateUser(Long userId, User user);

    public User login(String userEmail, String userPassword);

   public String getStoredVerificationCode(String userEmail);

   public User fetchUserByEmail(String userEmail);

   public User fetchUserByUserName(String userName);
}
