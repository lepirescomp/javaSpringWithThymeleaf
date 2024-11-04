package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    Credential getCredential(Long credentialId);

    @Select("SELECT * FROM `CREDENTIALS` WHERE userId = #{userId}")
    List<Credential> getCredentials(Integer userId);

    @Select("DELETE FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    void deleteCredential(Long noteId);

    @Insert("INSERT INTO `CREDENTIALS` (url, `username`, `key`, `password`, userId) VALUES(#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    void insert(Credential credential);

    @Update("UPDATE `CREDENTIALS` SET url=#{url}, username=#{username}, password=#{password} WHERE credentialId = #{credentialId}")
    void edit(Credential credential);

}
