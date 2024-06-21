package com.springboot.coffee.service;

import com.springboot.coffee.entity.Coffee;
import com.springboot.coffee.mapper.CoffeeMapper;
import com.springboot.coffee.repository.CoffeeRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.order.entity.Order;
import com.springboot.order.entity.OrderCoffee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CoffeeService {

    //    데이터엑세스계층 접속을 위해 의존성주입
    private final CoffeeRepository coffeeRepository;

    public CoffeeService(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
    }

    public Coffee createCoffee(Coffee coffee) {
//        보통 고유한 값은 일관되게 만들어줌 현재는 대문자로
        String coffeeCode = coffee.getCoffeeCode().toUpperCase();
//        그걸 대문자로 바꿔서 넣어줌.
        coffee.setCoffeeCode(coffeeCode);
        verifyExistCoffee(coffee.getCoffeeCode());
        return coffeeRepository.save(coffee);
    }

    //요청이 들어올 떄 patchDto에는 변경하고싶지않은 속성(null이 들어올 수 있다는걸 가시화함)
//    매개변수로 들어온 커피는 patchDto가 mapper를 통한 entity형태이고
//    안에는 (변경하지않은)null값이 들어있을 수도 있음
    public Coffee updateCoffee(Coffee coffee) {
//       유효성 검증
        Coffee findCoffee = findVerifiedCoffee(coffee.getCoffeeId());
//       변경 가능한 사항은 korName; engName, price,status
        Optional.ofNullable(coffee.getKorName()).ifPresent(korName -> findCoffee.setKorName(korName));
        Optional.ofNullable(coffee.getEngName()).ifPresent(engName -> findCoffee.setEngName(engName));
//        가격은 int type이라 null값이 안들어오니 of로 바꿔주었다.
        Optional.of(coffee.getPrice()).ifPresent(price -> findCoffee.setPrice(price));

        findCoffee.setModifiedAt(LocalDateTime.now());

        return coffeeRepository.save(findCoffee);
    }


    public Coffee findCoffee(long coffeeId) {
        return findVerifiedCoffee(coffeeId);
    }

    // 주문에 해당하는 커피 정보 조회
    public List<Coffee> findOrderedCoffees(Order order) {
        List<Coffee> orderCoffees = order.getOrderCoffees()
                .stream()
                .map(orderCoffee -> orderCoffee.getCoffee()
                ).collect(Collectors.toList());
        return orderCoffees;
    }

    public Page<Coffee> findCoffees(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.Direction.DESC, "coffeeId");
        Page<Coffee> findCoffee = coffeeRepository.findAll(pageRequest);
        return findCoffee;
        // return coffeeRepository.findAll(pageRequest); 같은 코드인데 동작과정을 보기위해 적어둠.
    }

    public void deleteCoffee(long coffeeId) {
        Coffee findcoffee = findVerifiedCoffee(coffeeId);
        findcoffee.setCoffeeStatus(Coffee.CoffeeStatus.COFFEE_SOLD_OUT);
        coffeeRepository.save(findcoffee);
    }

    public Coffee findVerifiedCoffee(long coffeeId) {
        Optional<Coffee> optionalCoffee = coffeeRepository.findById(coffeeId);
//        예외를 던져주려는데 없어서 커피 존재 x 예외코드 생성할 것 이다
        Coffee findCoffee = optionalCoffee.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COFFEE_NOT_FOUND));
        return findCoffee;
    }

    private void verifyExistCoffee(String coffeeCode) {
//        커피코드로 찾는 메서드를 커피레포지토리에서 생성해야한다.
        Optional<Coffee> optionalCoffee = coffeeRepository.findByCoffeeCode(coffeeCode);
//       메서드를 생성하고 와서 실행을 하면 커피코드를 찾아서 있다면 예외를 던져주었다.
        if (optionalCoffee.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.COFFEE_CODE_EXISTS);
        }

    }
}


//          위에 메서드와 동일해서 작성 안했음.
//    private Coffee findVerifiedCoffeeByQuery(long coffeeId) {
////         커피 아이디로 커피를 찾는 메서드호출
//        Optional<Coffee> optionalCoffee = coffeeRepository.findById(coffeeId);
//        Coffee coffee
//        throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
//    }
//}
