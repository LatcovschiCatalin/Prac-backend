package com.naqqa.Ledger.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Getter
@Component
public class EmailMessages {
    private static String APP_URL;
    private static String APP_NAME;

    private static final String hexColor = "2EA855";
    private static final String emailConfirmationUrl = APP_URL + "/auth/register/confirm?token=";
    public static final String registrationConfirmationMessage = "<h1 style='color: #" + hexColor +";'>Bine ai venit la " + APP_NAME + "!</h1>"
            + "<p>Bună,</p>"
            + "<p>Îți mulțumim că te-ai înregistrat la noi! Pentru a finaliza înregistrarea, te rugăm să faci clic pe linkul de mai jos pentru a-ți activa contul:</p>"
            + "<a href='" + emailConfirmationUrl + "' style='background-color: #" + hexColor +"; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Activează Contul</a>"
            + "<p>Mulțumim,</p>"
            + "<p>Echipa " + APP_NAME + "</p>";

    public static final String emailAddressConfirmationSubject = "Email address confirmation";
    public static final String registerSuccessfulSubject = "Registration successful!";
    public static final String resetPasswordSubject = "Password reset successful!";

    public static String getEmailAddressConfirmationMessage(String uuid, String email) {
        String link = APP_URL + "/auth/register/confirm";
        try {
            String encodedUuid = URLEncoder.encode(uuid, "UTF-8");
            String encodedEmail = URLEncoder.encode(email, "UTF-8");

            link += "?uuid=" + encodedUuid
                    + "&email=" + encodedEmail;

            return "<h1 style='color: #" + hexColor +";'>Welcome to " + APP_NAME + "!</h1>"
                    + "<p>Hi,</p>"
                    + "<p>To confirm your email address follow this link: </p>"
                    + "<a href='" + link + "' style='background-color: #" + hexColor +"; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Confirm email address</a>"
                    + "<p>Team " + APP_NAME + "</p>";

//            return "<h1 style='color: #" + hexColor + ";'>Bine ai venit la " + APP_NAME + "!</h1>"
//                    + "<p>Pentru a finaliza înregistrarea, fă clic pe linkul de mai jos pentru a-ți activa contul:</p>"
//                    + "<a href='" + link + "' style='background-color: #" + hexColor + "; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Activează Contul</a>";

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getResetPasswordEmailMessage(String email, String uuid) {
        String resetButtonUrl = APP_URL + "/auth/reset-password?uuid=" + uuid
                + "&email=" + email;

        return "<h1 style='color: #" + hexColor + ";'>Reset your password</h1>"
                + "<p>Hi,</p>"
                + "<p>To reset your password please follow the link below:</p>"
                + "<a href='" + resetButtonUrl + "' style='background-color: #" + hexColor +"; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Reset password</a>"
                + "<p>Thank you,</p>"
                + "<p>Team " + APP_NAME +"</p>";
    }

    // The following setters are used to initialize static variables as the @Value annotation does not support value injection into static fields
    @Value("${app.url}")
    public void setStaticAppUrl(String appUrl) {
        APP_URL = appUrl;
    }
    @Value("${app.name}")
    public void setStaticAppName(String appName) { APP_NAME = appName; }
}
