package com.beyond3.yyGang.nsupplement.service;

import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.nsupplement.dto.NSupplementRegisterDto;
import com.beyond3.yyGang.nsupplement.repository.NSupplementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NSupplementService {

    private final NSupplementRepository nsupplementRepository;

    @Transactional
    public NSupplement save(NSupplement nsupplement) {
        // 기존에 등록되어 있는 상품인지 확인 해야 하나?
        return nsupplementRepository.save(nsupplement);
    }

    @Transactional
    public List<NSupplementRegisterDto> getAllNSupplements() {
        return nsupplementRepository.findAll().stream()
                .map(nsupplement -> nsupplement.toDto())
                .collect(Collectors.toList());
    }

}