package com.springboot.member.dto;

import com.springboot.member.entity.Member;
import com.springboot.order.dto.OrderResponseDto;
import com.springboot.order.entity.Order;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// TODO 변경: Builder 패턴 적용
@Builder
@Getter
public class MemberResponseDto {
    private long memberId;
    private String email;
    private String name;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Member.MemberStatus memberStatus;
    private List<OrderResponseDto> orders;
 }
