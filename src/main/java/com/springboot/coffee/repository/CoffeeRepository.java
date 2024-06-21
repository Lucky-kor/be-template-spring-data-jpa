package com.springboot.coffee.repository;

import com.springboot.coffee.entity.Coffee;
import com.springboot.coffee.service.CoffeeService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoffeeRepository extends JpaRepository<Coffee,Long> {
//커피코드로 DB에서 찾을 수 있는 메서드 구현
    public Optional<Coffee> findByCoffeeCode(String coffeeCode);
}
