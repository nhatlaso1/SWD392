package com.free.swd_392.config.startup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Test purpose
 */
// @Component
@RequiredArgsConstructor
public class FirebaseAuthChangePassword implements CommandLineRunner {


    private final FirebaseAuth firebaseAuth;

    @Override
    public void run(String... args) throws Exception {
        firebaseAuth.updateUser(new UserRecord.UpdateRequest("DvlFahYVtJWFy1Ije4iJCbNqtvl2").setPassword("123456"));
    }
}
