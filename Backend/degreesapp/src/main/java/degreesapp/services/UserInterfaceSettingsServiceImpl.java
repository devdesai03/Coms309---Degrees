package degreesapp.services;

import degreesapp.models.User;
import degreesapp.models.UserInterfaceSettings;
import degreesapp.repositories.UserInterfaceSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserInterfaceSettingsServiceImpl implements UserInterfaceSettingsService {
    @Autowired
    UserInterfaceSettingsRepository userInterfaceSettingsRepository;

    @Autowired
    UserService userService;

    public UserInterfaceSettings saveUserInterfaceSettings(UserInterfaceSettings userInterfaceSettings) {
        return userInterfaceSettingsRepository.save(userInterfaceSettings);
    }

    public List<UserInterfaceSettings> fetchUserInterfaceSettingsList() {
        return userInterfaceSettingsRepository.findAll();
    }

    public UserInterfaceSettings fetchUserInterfaceSettingsById(Long userId) {
        return userInterfaceSettingsRepository.findById(userId).orElseGet(
            () -> UserInterfaceSettings.getDefault(userService.fetchUserById(userId))
        );
    }

    public UserInterfaceSettings fetchUserInterfaceSettingsByUser(User user) {
        return userInterfaceSettingsRepository.findById(user.getUserId()).orElseGet(
                () -> UserInterfaceSettings.getDefault(user)
        );
    }

    public void deleteUserInterfaceSettingsById(Long userId) {
        userInterfaceSettingsRepository.deleteById(userId);
    }

    public void deleteUserInterfaceSettingsByUser(User user) {
        this.deleteUserInterfaceSettingsById(user.getUserId());
    }

    @Transactional
    public UserInterfaceSettings updateUserInterfaceSettings(Long userId, UserInterfaceSettings userInterfaceSettings) {
        userInterfaceSettings.setUser(userService.fetchUserById(userId));
        return userInterfaceSettingsRepository.save(userInterfaceSettings);
    }

    @Transactional
    public UserInterfaceSettings updateUserInterfaceSettings(User user, UserInterfaceSettings userInterfaceSettings) {
        userInterfaceSettings.setUser(user);
        return userInterfaceSettingsRepository.save(userInterfaceSettings);
    }
}
