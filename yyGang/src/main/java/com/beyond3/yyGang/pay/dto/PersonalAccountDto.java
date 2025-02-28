package com.beyond3.yyGang.pay.dto;

import com.beyond3.yyGang.pay.PersonalAccount;
import com.beyond3.yyGang.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonalAccountDto {

    @NotBlank(message = "은행 이름은 필수입니다.")
    private String bankName;

    private int balance;    // 잔고

    public PersonalAccount toEntity(User user) {
        return PersonalAccount.builder()
                .bankName(bankName)
                .balance(balance)
                .build();
    }

}