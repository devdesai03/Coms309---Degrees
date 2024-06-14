package degreesapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author Mohammed Abdalgader
 */
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(nullable = false)
    private String userName;
    private String userAddress;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userPassword;


    @Email(message = "it should be a real email!")
    @Column(unique = true)
    private String userEmail;

    private String phoneNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String verificationCode;

    /* Back references */
    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties(value = {"user"}, allowSetters = true)
    private IsuRegistration isuRegistration;

    /* Methods */
    public User() {
    }

    public User(Long userId, String userName, String userAddress, String userEmail, String userPassword, String phoneNumber) {
        this.userId = userId;
        this.userName = userName;
        this.userAddress = userAddress;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.phoneNumber = phoneNumber;
    }

    public static String generateVerificationCode(int length) {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * characters.length());
            code.append(characters.charAt(randomIndex));
        }

        return code.toString();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public void sendVerificationCodeAndSet(String userEmail, JavaMailSender javaMailSender) {
        // Generate a verification code
        String verificationCode = generateVerificationCode(6); // Adjust the length as needed
//        String verificationCode = "12345";
        // Send the verification code to the user's email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("Verification Code");
        message.setText("Your verification code is: " + verificationCode);
//        javaMailSender.send(message);

        // Set the verification code in the user's record
        this.verificationCode = verificationCode;

    }



    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }



    public String getVerificationCode() {
        return verificationCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public IsuRegistration getIsuRegistration() {
        return this.isuRegistration;
    }

    public void setIsuRegistration(IsuRegistration isuRegistration) {
        this.isuRegistration = isuRegistration;
//        // Hacks to fix the "setting to null" bug
//        if (isuRegistration != null) {
//            this.isuRegistration = isuRegistration;
//            isuRegistration.setUser(this);
//        } else {
//            this.isuRegistration.setUser(null);
//            this.isuRegistration = isuRegistration;
//        }
    }
}
