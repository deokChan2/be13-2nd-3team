package com.beyond3.yyGang.user.dto;

import com.beyond3.yyGang.user.domain.Gender;
import com.beyond3.yyGang.user.domain.Role_name;
import com.beyond3.yyGang.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
public class UserInfoDto {

    private String email;

    private String name;

    private String password;

    // CUSTOMER, SELLER, PHARMACIST, ADMIN -> default : CUSTOMER
    private Role_name role;

    private int age;  // 생년 월일로 받을까?

    private Gender gender;  // 성별 -> MALE, FEMALE -> Null 가능

    private String phone; // 전화번호

    private String address; // 주소

    private LocalDateTime createdDate;  // 가입 일자


    public UserInfoDto(User user){
        this.email = user.getEmail();
        this.name = user.getName();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.age = user.getAge();
        this.gender = user.getGender();
        this.phone = user.getPhone();
        this.address = user.getAddress();
        this.createdDate = user.getCreatedDate();
    }

}