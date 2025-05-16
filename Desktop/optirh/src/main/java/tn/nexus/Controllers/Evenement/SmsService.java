package tn.nexus.Controllers.Evenement;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import io.github.cdimascio.dotenv.Dotenv;

public class SmsService {
    private static final String ACCOUNT_SID = "ACda308b1fc42f472cf5d61accbd9209b8";
    private static final String AUTH_TOKEN = "af7f9261d5c2d159a7fd908c8e748849";
    private static final String TWILIO_PHONE_NUMBER = "+19787059136"; // Ex: +123456789

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public static void sendSms(String to, String message) {
        try {
            Message.creator(
                    new PhoneNumber(to),   // Numéro du destinataire
                    new PhoneNumber(TWILIO_PHONE_NUMBER),  // Numéro Twilio
                    message // Contenu du SMS
            ).create();
            System.out.println("SMS envoyé avec succès à " + to);
        } catch (Exception e) {
            System.out.println("Erreur lors de l'envoi du SMS : " + e.getMessage());
        }
    }
}
