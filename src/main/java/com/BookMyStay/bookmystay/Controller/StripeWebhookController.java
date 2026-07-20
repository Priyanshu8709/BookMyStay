package com.BookMyStay.bookmystay.Controller;

import com.BookMyStay.bookmystay.Service.BookingService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webhooks")
public class StripeWebhookController {
    private final BookingService bookingService;

    @Value("${stripe.webhook.secret:}")
    private String webhookSecret;

    @PostMapping("/stripe")
    @Operation(summary = "Handle Stripe webhook events", tags = {"Payments"})
    public ResponseEntity<Void> handleStripeWebhook(@RequestBody String payload,
                                                    @RequestHeader(value = "Stripe-Signature", required = false) String signature)
            throws SignatureVerificationException {
        if (webhookSecret == null || webhookSecret.isBlank() || signature == null || signature.isBlank()) {
            return ResponseEntity.noContent().build();
        }

        Event event = Webhook.constructEvent(payload, signature, webhookSecret);
        bookingService.capturePayment(event);
        return ResponseEntity.noContent().build();
    }
}
