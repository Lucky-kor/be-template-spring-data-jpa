package com.springboot.coffee.dto;

import com.springboot.coffee.entity.Coffee;
import com.springboot.validator.NotSpace;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Pattern;

// TODO v10: Getter/Setter 수정
@Getter
public class CoffeePatchDto {
    private long coffeeId;
    @NotSpace(message = "커피명(한글)은 공백이 아니어야 합니다.")
    private String korName;
    @Pattern(regexp = "^([A-Za-z])(\\s?[A-Za-z])*$", message = "커피명(영문)은 영문이어야 합니다. 예) Cafe Latte")
    private String engName;
    @Range(min = 100, max = 50000)
    private Integer price;
    //커피의 판매 상태는 바뀔 수 있어야한다.
    @Setter
    private Coffee.CoffeeStatus coffeeStatus;

//    결국 업데이트의  기본 값으로는 판매중으로 해두겠다.


    public void setCoffeeId(long coffeeId) {
        this.coffeeId = coffeeId;
    }

    public Integer getPrice() {
        return price;
    }
}
