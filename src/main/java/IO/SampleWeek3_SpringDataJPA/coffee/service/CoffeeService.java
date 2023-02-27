package IO.SampleWeek3_SpringDataJPA.coffee.service;

import IO.SampleWeek3_SpringDataJPA.coffee.entity.Coffee;
import IO.SampleWeek3_SpringDataJPA.coffee.repository.CoffeeRepository;
import IO.SampleWeek3_SpringDataJPA.exception.BusinessLogicException;
import IO.SampleWeek3_SpringDataJPA.exception.ExceptionCode;
import IO.SampleWeek3_SpringDataJPA.order.entity.Order;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CoffeeService {
    private final CoffeeRepository repository;

    public Coffee createCoffee(Coffee coffee) {
        String coffeeCode = coffee.getCoffeeCode().toUpperCase();

        verifyExistCoffee(coffeeCode);
        coffee.setCoffeeCode(coffeeCode);

        return repository.save(coffee);
    }

    public Coffee updateCoffee(Coffee coffee) {
        Coffee findCoffee = findVerifiedCoffee(coffee.getCoffeeId());

        Optional.ofNullable(coffee.getKorName())
                .ifPresent(findCoffee::setKorName);
        Optional.ofNullable(coffee.getEngName())
                .ifPresent(findCoffee::setEngName);
        Optional.ofNullable(coffee.getPrice())
                .ifPresent(findCoffee::setPrice);
        Optional.ofNullable(coffee.getCoffeeStatus())
                .ifPresent(findCoffee::setCoffeeStatus);

        return repository.save(findCoffee);
    }

    public Coffee findCoffee(long coffeeId) {
        return findVerifiedCoffeeByQuery(coffeeId);
    }

    // 주문에 해당하는 커피 정보 조회
//    public List<Coffee> findOrderedCoffees(Order order) {
//    }

    public Page<Coffee> findCoffees(int page, int size) {
        return repository.findAll(PageRequest.of(page, size,
                Sort.by("coffeeId").descending()));
    }

    public void deleteCoffee(long coffeeId) {
        Coffee coffee = findVerifiedCoffee(coffeeId);
        repository.delete(coffee);
    }

    public Coffee findVerifiedCoffee(long coffeeId) {
        Optional<Coffee> optionalCoffee = repository.findById(coffeeId);
        Coffee findCoffee =
                optionalCoffee.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.COFFEE_NOT_FOUND));
        return findCoffee;
    }

    private void verifyExistCoffee(String coffeeCode) {
        Optional<Coffee> coffee = repository.findByCoffeeCode(coffeeCode);
        if (coffee.isPresent())
            throw new BusinessLogicException(ExceptionCode.COFFEE_CODE_EXISTS);
    }

    private Coffee findVerifiedCoffeeByQuery(long coffeeId) {
        Optional<Coffee> optionalCoffee = repository.findByCoffee(coffeeId);
        Coffee findCoffee =
                optionalCoffee.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.COFFEE_NOT_FOUND));
        return findCoffee;
    }
}
