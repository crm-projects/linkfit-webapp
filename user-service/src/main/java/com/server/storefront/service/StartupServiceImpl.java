package com.server.storefront.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.storefront.dto.BaseProfile;
import com.server.storefront.dto.CreatorProfile;
import com.server.storefront.dto.Credentials;
import com.server.storefront.repository.RedisRepository;
import com.server.storefront.util.JWTUtil;
import com.server.storefront.util.OTPUtil;
import com.server.storefront.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public boolean generateOtpByEmail(Credentials credentials) {
        if (!StringUtils.hasText(credentials.getKey()) &&
                !credentials.getKey().matches(EMAIL_PATTERN)) {
            log.error("");
        }

        String otp = OTPUtil.generateOTP();
        try {
            publishToRedis(credentials, otp);
            publishToKafka(credentials.getKey(), otp);
            publishToAmazonSES();
            return true;
        } catch (Exception ex) {
            //TODO : Throw dedicated exceptions.
            log.error(ex.getMessage());
        }

        return false;
    }

    private void publishToAmazonSES() {
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

    private void publishToKafka(String key, String otp) {
        Map<String, String> kafkaMessage = Map.of(key, otp);
        log.info("Started publishing to Kafka");
    }

    @Override
    public boolean validateUsername(String userName) {
        // TODO : Write logic to query Elasticsearch for the data rather than Postgres.
        return false;
    }

    @Override
    @Transactional
    public Map<String, Object> processUserRegistration(Map<String, String> userDetails, String profile) {

        if(userDetails.isEmpty()){
            //TODO : Throw dedicated exceptions.
            log.error("");
        }

        String email = userDetails.get(EMAIL);
        String otp = userDetails.get(OTP);

        if (!StringUtils.hasText(email)) {
            //TODO : Throw dedicated exceptions.
            log.error("");
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

    public Optional<Credentials> getValidCredentials(String email, String otp) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info("Validating OTP for: {}", email);
            Map<String, Object> value = objectMapper.convertValue(redisRepository.get(email), new TypeReference<Map<String, Object>>() {
            });
            return value.get(OTP).equals(otp) ? Optional.ofNullable((Credentials) value.get(USER_CREDENTIALS)) : Optional.empty();
        } catch (Exception ex) {
            //TODO : Throw dedicated exceptions.
            log.error(ex.getMessage());
        }
        return Optional.empty();
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
