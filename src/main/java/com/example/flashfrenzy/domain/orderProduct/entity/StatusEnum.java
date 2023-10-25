package com.example.flashfrenzy.domain.orderProduct.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusEnum {
    SUCCESS,        // 성공
    FAIL,           // 실패
    PENDING         // 진행 중
}
