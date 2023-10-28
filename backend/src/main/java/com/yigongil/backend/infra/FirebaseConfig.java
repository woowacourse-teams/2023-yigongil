package com.yigongil.backend.infra;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

@Profile(value = {"prod", "dev"})
@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseMessaging messagingService(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    @Bean
    public FirebaseApp firebaseApp(GoogleCredentials credentials) {
        FirebaseOptions options = FirebaseOptions.builder()
                                                 .setCredentials(credentials)
                                                 .build();
        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public GoogleCredentials googleCredentials() throws IOException {
        final var inputStream = new ClassPathResource("yigongil-private/firebase-adminsdk.json").getInputStream();
        return GoogleCredentials.fromStream(inputStream);
    }
}
