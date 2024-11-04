package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public void addCredential(Credential credential) {
        credentialMapper.insert(credential);
    }

    public List<Credential> getCredential(Integer userId) {
        return credentialMapper.getCredentials(userId);
    }

    public void deleteCredential(Long id){
        credentialMapper.deleteCredential(id);
    }

    public void updateCredential(Credential credential){
        credentialMapper.edit(credential);
    }

    public Credential findById(Long id){
        return credentialMapper.getCredential(id);
    }
}