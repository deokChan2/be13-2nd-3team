package com.beyond3.yyGang.user.dto;

import com.beyond3.yyGang.user.domain.Gender;
import com.beyond3.yyGang.user.domain.Role_name;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class UserModifyDto {
    // 회원 정보 수정은 일단 비밀번호 변경 없이

    private String name;

    private Role_name role;

    @Range(min = 0, max = 150)
    private Integer age;

    private Gender gender;  // 성별 -> MALE, FEMALE

    private String phone; // 전화번호

    private String address; // 주소
}