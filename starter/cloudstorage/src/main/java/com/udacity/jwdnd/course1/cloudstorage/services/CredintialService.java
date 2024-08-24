package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.CredintialMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.Credential;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import static java.security.DrbgParameters.nextBytes;

@Service
@RequiredArgsConstructor
public class CredintialService {

    private final CredintialMapper credintialMapper;

    private final EncryptionService encryptionService;

    public List<Credential> getCredentialByUserId(Long userId){
        return credintialMapper.getCredentialByUserId(userId);

    }


    public int inertCredential(Credential credential, Long userId){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey= Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        Credential newCredential = buildCredential(credential,userId,encodedKey,encryptedPassword);

        return credintialMapper.insertCredential(newCredential);
    }

    private Credential buildCredential(Credential credential, Long userId, String key, String encryptedPassword) {
        return new Credential(
                null,  // ID
                credential.getUrl(),
                credential.getUsername(),
                key,
                encryptedPassword,
                userId
        );
    }

    public void deleteCredential(Long credentialId){
        credintialMapper.deleteCredential(credentialId);
    }

    public void updateCredential(Credential credential) {

        Credential savedCredential = credintialMapper.getCredentialById(credential.getCredentialid());
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), savedCredential.getKey());

        updateCredentialEncryption(credential, savedCredential.getKey(), encryptedPassword);

        credintialMapper.updateCredential(credential);
    }
    private void updateCredentialEncryption(Credential credential, String key, String encryptedPassword) {
        credential.setKey(key);
        credential.setPassword(encryptedPassword);
    }
    }
