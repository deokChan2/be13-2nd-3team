package com.beyond3.yyGang.nsupplement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/nsupplement")
@RequiredArgsConstructor
public class NSupplementController {

    private final NSupplementService nSupplementService;
    private final NSupplementRepository nSupplementRepository;

    @PostMapping("/")
    public ResponseEntity<Void> register(@RequestBody NSupplementRegisterDto nSupplementRegisterDto) {
        log.info("Registering nSupplement {}", nSupplementRegisterDto);
        NSupplement nsupplement = nSupplementRegisterDto.toEntity();

        nSupplementService.save(nsupplement);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/info")
    public ResponseEntity<List<NSupplementRegisterDto>> info() {
        return ResponseEntity.ok(nSupplementService.getAllNSupplements());
    }
}