package com.beyond3.yyGang.user;

import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.nsupplement.repository.NSupplementRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(false)
class UserControllerTest {

    @Autowired
    private NSupplementRepository nsupplementRepository;

    @Test
    void dbConnectionTest(){
        Assertions.assertThat(true).isTrue();
    }

    @Test
    void asd() {
        NSupplement supplement = new NSupplement("비타민D", "saklfjalskf", "으으", 10000, 10);
        nsupplementRepository.save(supplement);
    }
}