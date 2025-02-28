package com.server.storefront.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.storefront.dto.BaseProfile;
import com.server.storefront.dto.CreatorProfile;
import com.server.storefront.dto.Credentials;
import com.server.storefront.exception.StartupException;
import com.server.storefront.repository.CreatorProfileRepository;
import com.server.storefront.repository.RedisRepository;
import com.server.storefront.util.JWTUtil;
import com.server.storefront.util.OTPUtil;
import com.server.storefront.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class StartupServiceImpl implements StartupService {


    private static final String EMAIL_PATTERN = "^[a-z0-9_-]+@gmail.com$";

    private static final String USER_CREDENTIALS = "credentials";

    private static final String OTP = "otp";

    private static final String EMAIL = "email";

    private static final String USER = "user";

    private static final String JWT = "jwt";

    private static final String USER_ID = "userId";

    Predicate<String> isBasicProfile = p -> p.equals(USER);

    private final RedisRepository redisRepository;

    private final CreatorProfileRepository creatorRepository;

    @Override
    public boolean generateOtpByEmail(Credentials credentials) throws StartupException {
        if (!StringUtils.hasText(credentials.getKey()) &&
                !credentials.getKey().matches(EMAIL_PATTERN)) {
            log.error("Email address is not valid");
            throw new StartupException("Email address is not valid. Please try again");
        }

        String otp = OTPUtil.generateOTP();
        try {
            processUserOtp(credentials, otp);
//          publishToRedis(credentials, otp);
            publishToAmazonSES();
            return true;
        } catch (Exception ex) {
            //TODO : Throw dedicated exceptions.
            log.error(ex.getMessage());
            throw new StartupException(ex.getMessage());
        }
    }

    private void processUserOtp(Credentials credentials, String otp) {
        String encryptedPassword = PasswordUtil.hashPassword(credentials.getValue());
        credentials.setValue(encryptedPassword);
        Map<String, Object> userCredentials = Map.of(
                USER_CREDENTIALS, credentials,
                OTP, otp
        );
        prepareCache(credentials.getKey(), userCredentials);
    }

    @CachePut(value = "userCache", key = "#key")
    private void prepareCache(String key, Map<String, Object> userCredentials) {
        log.info("Updated cache for {}", key);
    }

    private void publishToAmazonSES() {
        log.info("Started publishing to Amazon SES");
    }

    private void publishToRedis(Credentials credentials, String otp) {
        String encryptedPassword = PasswordUtil.hashPassword(credentials.getValue());
        credentials.setValue(encryptedPassword);
        Map<String, Object> userCredentials = Map.of(
                USER_CREDENTIALS, credentials,
                OTP, otp
        );
        log.info("Started publishing to Redis");
      redisRepository.put(credentials.getKey(), userCredentials);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateUsername(String userName) {
        return creatorRepository.existsByUserName(userName);
    }

    @Override
    @Transactional
    public Map<String, Object> processUserRegistration(Map<String, String> userDetails, String profile) throws StartupException {

        if(userDetails.isEmpty()){
            log.error("Invalid user input");
            throw new StartupException("Invalid user input. Please try again");
        }

        String email = userDetails.get(EMAIL);
        String otp = userDetails.get(OTP);

        if (!StringUtils.hasText(email)) {
            log.error("Email address is empty. Please try again with valid email");
            throw new StartupException("Email address is empty. Please try again with valid email");
        }

        Optional<Credentials> validUserCredentials = getValidCredentials(email, otp);
        if (validUserCredentials.isEmpty()) return Map.of();

        String authToken = generateAuthToken(email);
        String userId = registerUserProfile(validUserCredentials.get(), profile);
        return Map.of(
                JWT, authToken,
                USER_ID, userId
        );
    }

    private String registerUserProfile(Credentials credentials, String profile) {
        BaseProfile baseProfile = createUserProfile(credentials, profile);
        publishUserProfileToKafka(baseProfile, profile);
        return baseProfile.getId();
    }

    private void publishUserProfileToKafka(BaseProfile baseProfile, String profile) {
        //TODO : Come up with generic logic for publishing messages to Kafka.
    }


    private BaseProfile createUserProfile(Credentials credentials, String profile) {
        //TODO : To write logic for storing data into Postgres.
        if (isBasicProfile.test(profile)) {
            return new BaseProfile(credentials.getKey(), credentials.getValue());
        }
        return new CreatorProfile(credentials.getKey(), credentials.getValue());
    }

    public Optional<Credentials> getValidCredentials(String email, String otp) throws StartupException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info("Validating OTP for: {}", email);
            Map<String, Object> value = objectMapper.convertValue(redisRepository.get(email), new TypeReference<Map<String, Object>>() {
            });
            return value.get(OTP).equals(otp) ? Optional.ofNullable((Credentials) value.get(USER_CREDENTIALS)) : Optional.empty();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new StartupException(ex.getMessage());
        }
    }

    private String generateAuthToken(String email) {
        return JWTUtil.generate(email);
    }

    @Override
    public String authorizeUserLogin(Credentials credentials) {
        try {
            //This boolean will decide whether to add email or username in Elastic Query,
            boolean isEmail = credentials.getKey().matches(EMAIL_PATTERN);

            //TODO : Logic to create custom Elastic Query for fetching user records and validate.
        } catch (Exception ex) {
            //TODO : Throw dedicated exception
            log.error(ex.getMessage());
        }
        return "";
    }
}
