package com.beyond3.yyGang;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {
    @PostMapping("/data")
    public ResponseEntity<String> createData(@RequestBody String data) {
        return ResponseEntity.ok("데이터가 성공적으로 추가되었습니다.");
    }

    @GetMapping("/data")
    public Map<String, Object> getData() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "데이터 가져오기 성공!");
        response.put("data", Arrays.asList("item1", "item2", "item3")); // 샘플 데이터
        return response;
    }
}
