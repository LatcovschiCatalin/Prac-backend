package com.naqqa.Ledger.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.naqqa.Ledger.configuration.EmailMessages;
import com.naqqa.Ledger.entities.RegisterRequestEntity;
import com.naqqa.Ledger.entities.ResetPasswordEntity;
import com.naqqa.Ledger.entities.UserEntity;
import com.naqqa.Ledger.enums.AuthMethod;
import com.naqqa.Ledger.model.*;
import com.naqqa.Ledger.repositories.RegisterRequestRepository;
import com.naqqa.Ledger.repositories.ResetPasswordRepository;
import com.naqqa.Ledger.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${app.url}")
    private String APP_URL;
    @Value("${app.name}")
    private String APP_NAME;
    @Value("${google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${facebook.app-id}")
    private String FACEBOOK_APP_ID;
    @Value("${facebook.app-secret}")
    private String FACEBOOK_APP_SECRET;

    private final EmailService emailService;

    private final UserRepository userRepository;
    private final RegisterRequestRepository registerRequestRepository;
    private final ResetPasswordRepository resetPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final HttpTransport httpTransport;
    private final JsonFactory jsonFactory;

    public void register(RegisterRequest request) {
        String uuid = UUID.randomUUID().toString();
        RegisterRequestEntity registerRequest = new RegisterRequestEntity(
                request.email(),
                request.username(),
                uuid,
                passwordEncoder.encode(request.password()));

        registerRequestRepository.save(registerRequest);

        try {
            emailService.sendEmail(request.email(), EmailMessages.emailAddressConfirmationSubject, EmailMessages.getEmailAddressConfirmationMessage(uuid, request.email()));
        } catch (IOException e) {
            log.info("Failed to send email address confirmation!");
        }
    }

    public AuthenticationResponse registerConfirm(RegisterConfirmRequest request) {
        Optional<RegisterRequestEntity> registerEntity = registerRequestRepository.findById(request.getEmail());

        if (registerEntity.isEmpty() || !registerEntity.get().getUuid().equals(request.getUuid()))
            return AuthenticationResponse.builder().message("Invalid request").build();

        UserEntity user = UserEntity.builder()
                .username(registerEntity.get().getUsername())
                .email(registerEntity.get().getEmail())
                .password(registerEntity.get().getPassword())
                .authMethod(AuthMethod.DEFAULT)
                .build();

        userRepository.save(user);
        registerRequestRepository.delete(registerEntity.get());

        return new AuthenticationResponse(jwtService.generateToken(user), "Registration successful!");
    }

    public AuthenticationResponse register(SocialLoginTokenInfo tokenInfo, AuthMethod authMethod) {
        UserEntity user = UserEntity.builder()
                .username(tokenInfo.getName())
                .email(tokenInfo.getEmail())
                .authMethod(authMethod)
                .externalId(tokenInfo.getExternalId())
                .build();

        userRepository.save(user);

        return new AuthenticationResponse(jwtService.generateToken(user), "Registration successful!");
    }

    public void sendEmailAddressConfirmation(String email, String uuid) {
        String colorCode = "2EA855";

        try {
            String encodedEmail = URLEncoder.encode(email, "UTF-8");
            String encodedUuid = URLEncoder.encode(uuid, "UTF-8");

            String url = APP_URL + "/auth/register/confirm?token="
                    + "email=" + encodedEmail
                    + "&uuid=" + encodedUuid;

            String message = "<h1 style='color: #" + colorCode + ";'>Bine ai venit la " + APP_NAME + "!</h1>"
                    + "<p>Pentru a finaliza înregistrarea, fă clic pe linkul de mai jos pentru a-ți activa contul:</p>"
                    + "<a href='" + url + "' style='background-color: #" + colorCode + "; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Activează Contul</a>";

            log.info(url);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
    }
    private void sendResetPasswordEmail(String email, String uuid) {
        String colorCode = "2EA855";

        try {
            String encodedEmail = URLEncoder.encode(email, "UTF-8");
            String encodedUuid = URLEncoder.encode(uuid, "UTF-8");

            String url = APP_URL + "/auth/register/confirm?token="
                    + "email=" + encodedEmail
                    + "&uuid=" + encodedUuid;

            String message = "<h1 style='color: #" + colorCode + ";'>" + APP_NAME + "</h1>"
                    + "<p>Pentru a resete parola, fă clic pe linkul de mai jos!</p>"
                    + "<a href='" + url + "' style='background-color: #" + colorCode + "; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Reseteaza parola</a>";

            log.info(url);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
    }

    public boolean isRegistered(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);

        if (user.isEmpty())
            return false;

        return user.get().getAuthMethod() == AuthMethod.DEFAULT;
    }
    public boolean isRegistered(SocialLoginTokenInfo tokenInfo, AuthMethod authMethod) {
        Optional<UserEntity> user = userRepository.findByEmailAndAuthMethod(tokenInfo.getEmail(), authMethod);

        return user.isPresent();

        //return user.get().getExternalId().equals(tokenInfo.getExternalId());
    }

    public SocialLoginTokenInfo validateSocialLoginRequest(SocialLoginRequest token) {
        if (token.getAuthMethod() == AuthMethod.GOOGLE)
            return validateGoogleToken(token.getToken());
//        else if (token.getAuthMethod() == AuthMethod.APPLE)
//            return validateAppleToken(token.getToken());
        else if (token.getAuthMethod() == AuthMethod.FACEBOOK)
            return validateFacebookToken(token);
        else
            return null;
    }

    private SocialLoginTokenInfo validateGoogleToken(String token) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                .build();

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(token);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            // Get profile information from payload
            return new SocialLoginTokenInfo(userId,
                    (String)payload.get("name"),
                    payload.getEmail());
        }
        else {
            System.out.println("token is null!");
        }
        return null;
    }

    private SocialLoginTokenInfo validateFacebookToken(SocialLoginRequest socialLoginRequest) {

        String accessToken = FACEBOOK_APP_ID + "|" + FACEBOOK_APP_SECRET;
        try {
            accessToken = URLEncoder.encode(accessToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String url = String.format(
                "https://graph.facebook.com/debug_token?input_token=%s&access_token=%s",
                socialLoginRequest.getToken(), accessToken);

        log.info(url);

        boolean tokenValid = false;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                log.info(result);

                JSONObject jsonResponse = new JSONObject(result);
                JSONObject data = jsonResponse.getJSONObject("data");

                boolean isValid = data.getBoolean("is_valid");
                String appId = data.getString("app_id");

                tokenValid = isValid && appId.equals(FACEBOOK_APP_ID);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!tokenValid)
            return null;

        // Get user information
        url = String.format(
                "https://graph.facebook.com/me?fields=id,name,email&access_token=%s",
                socialLoginRequest.getToken());
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);

                JSONObject jsonResponse = new JSONObject(result);

                String id = jsonResponse.getString("id");
                String name = jsonResponse.getString("name");

                String email = jsonResponse.has("email") ? jsonResponse.getString("email") : null;

                log.info("id: " + id);
                log.info("name: " + name);
                log.info("email: " + email);

                return new SocialLoginTokenInfo(id, name, email);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthenticationResponse login(LoginRequest request) {
        Optional<UserEntity> user = userRepository.findByEmail(request.getEmail());

        if (user.isEmpty())
            return AuthenticationResponse.builder().message("Something went wrong!").build();

        if (!passwordEncoder.matches(request.getPassword(), user.get().getPassword()))
            return AuthenticationResponse.builder().message("Incorrect password!").build();

        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(user.get()))
                .message("Login successful!")
                .build();
    }

    public AuthenticationResponse login(SocialLoginTokenInfo tokenInfo, AuthMethod authMethod) {
        Optional<UserEntity> user = userRepository.findByEmailAndAuthMethod(tokenInfo.getEmail(), authMethod);

        if (user.isEmpty())
            return AuthenticationResponse.builder().message("Something went wrong!").build();

        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(user.get()))
                .message("Login successful!")
                .build();
    }

    public void forgetPassword(ForgotPasswordRequest request) {
        String uuid = UUID.randomUUID().toString();
        ResetPasswordEntity resetPasswordEntity = ResetPasswordEntity.builder()
                .email(request.email())
                .uuid(uuid)
                .build();

        resetPasswordRepository.save(resetPasswordEntity);
        sendResetPasswordEmail(request.email(), uuid);
    }


    public AuthenticationResponse resetPassword(ResetPasswordRequest request) {
        Optional<ResetPasswordEntity> resetPasswordEntity = resetPasswordRepository.findById(request.getEmail());
        Optional<UserEntity> user = userRepository.findByEmail(request.getEmail());

        if (resetPasswordEntity.isEmpty() || !resetPasswordEntity.get().getUuid().equals(request.getUuid())
        || user.isEmpty())
            return AuthenticationResponse.builder().message("Something went wrong!").build();

        user.get().setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user.get());
        resetPasswordRepository.delete(resetPasswordEntity.get());

        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(user.get()))
                .message("Password reset successfully!")
                .build();
    }
}
