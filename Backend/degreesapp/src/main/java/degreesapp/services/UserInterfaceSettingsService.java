package degreesapp.services;

import degreesapp.models.User;
import degreesapp.models.UserInterfaceSettings;

import java.util.List;

public interface UserInterfaceSettingsService {
    public UserInterfaceSettings saveUserInterfaceSettings(UserInterfaceSettings userInterfaceSettings);

    public List<UserInterfaceSettings> fetchUserInterfaceSettingsList();

    public UserInterfaceSettings fetchUserInterfaceSettingsByUser(User user);

    public UserInterfaceSettings fetchUserInterfaceSettingsById(Long userId);

    public void deleteUserInterfaceSettingsByUser(User user);

    public void deleteUserInterfaceSettingsById(Long userId);

    public UserInterfaceSettings updateUserInterfaceSettings(User user, UserInterfaceSettings userInterfaceSettings);

    public UserInterfaceSettings updateUserInterfaceSettings(Long userId, UserInterfaceSettings userInterfaceSettings);
}
