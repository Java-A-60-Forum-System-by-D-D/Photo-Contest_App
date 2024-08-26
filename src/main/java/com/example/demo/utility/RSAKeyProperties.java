package com.example.demo.utility;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Component
@Getter
@Setter
@EqualsAndHashCode
public class RSAKeyProperties {


    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public RSAKeyProperties() {
        KeyPair pair = KeyGeneratorUtility.generateRSAKey();
        this.privateKey = (RSAPrivateKey) pair.getPrivate();
        this.publicKey = (RSAPublicKey) pair.getPublic();
    }
}
