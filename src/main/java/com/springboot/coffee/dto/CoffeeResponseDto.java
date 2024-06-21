package com.springboot.coffee.dto;

import com.springboot.coffee.entity.Coffee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CoffeeResponseDto {
//    현재 커피 필드에 상태, 만든시간, 수정시간이 추가되었기때문에
//    responseDto역시 보내줄것이다. 커피코드는 숨기겠다.
    private long coffeeId;
    private String korName;
    private String engName;
    private int price;
    private Coffee.CoffeeStatus coffeeStatus;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
