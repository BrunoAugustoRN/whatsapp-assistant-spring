package project.whatsassist.example.demo.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message; // Import correto da Twilio!
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotifierService {

    @Value("${TWILIO_ACCOUNT_SID}")
    private String accountSid;

    @Value("${TWILIO_AUTH_TOKEN}")
    private String authToken;

    @Value("${twilio.whatsapp-from}")
    private String fromNumber;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void send(String toNumber, String text) {
        // Garante que o toNumber já tenha o prefixo "whatsapp:" se não tiver
        String toFormatted = toNumber.startsWith("whatsapp:") ? toNumber : "whatsapp:" + toNumber;

        Message.creator(
                new PhoneNumber(toFormatted),
                new PhoneNumber(fromNumber),
                text
        ).create();
    }
}