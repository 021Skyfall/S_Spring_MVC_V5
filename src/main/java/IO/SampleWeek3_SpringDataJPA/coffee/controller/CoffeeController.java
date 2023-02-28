package IO.SampleWeek3_SpringDataJPA.coffee.controller;

import IO.SampleWeek3_SpringDataJPA.coffee.dto.CoffeePatchDto;
import IO.SampleWeek3_SpringDataJPA.coffee.dto.CoffeePostDto;
import IO.SampleWeek3_SpringDataJPA.coffee.entity.Coffee;
import IO.SampleWeek3_SpringDataJPA.coffee.mapper.CoffeeMapper;
import IO.SampleWeek3_SpringDataJPA.coffee.service.CoffeeService;
import IO.SampleWeek3_SpringDataJPA.dto.MultiResponseDto;
import IO.SampleWeek3_SpringDataJPA.utils.UriCreator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/coffees")
@Validated
@Slf4j
public class CoffeeController {
    private final static String COFFEE_DEFAULT_URL = "/coffees";
    private CoffeeService service;
    private CoffeeMapper mapper;

    @PostMapping
    public ResponseEntity postCoffee(@Validated @RequestBody CoffeePostDto coffeePostDto) {
        Coffee coffee = service.createCoffee(mapper.coffeePostDtoToCoffee(coffeePostDto));
        URI location = UriCreator.createUri(COFFEE_DEFAULT_URL, coffee.getCoffeeId());

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{coffee-id}")
    public ResponseEntity patchCoffee(@PathVariable("coffee-id") @Positive long coffeeId,
                                      @Validated @RequestBody CoffeePatchDto coffeePatchDto) {
        coffeePatchDto.setCoffeeId(coffeeId);
        Coffee coffee = service.updateCoffee(mapper.coffeePatchDtoToCoffee(coffeePatchDto));

        return new ResponseEntity<>(mapper.coffeeToCoffeeResponseDto(coffee), HttpStatus.OK);
    }

    @GetMapping("/{coffee-id}")
    public ResponseEntity getCoffee(@PathVariable("coffee-id") @Positive long coffeeId) {
        Coffee coffee = service.findCoffee(coffeeId);

        return new ResponseEntity<>(mapper.coffeeToCoffeeResponseDto(coffee),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getCoffees(@RequestParam @Positive int page,
                                     @RequestParam @Positive int size) {
        Page<Coffee> pageCoffees = service.findCoffees(page - 1, size);
        List<Coffee> coffees = pageCoffees.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(mapper.coffeesToCoffeeResponseDtos(coffees),
                        pageCoffees),
                HttpStatus.OK);
    }

    @DeleteMapping("/{coffee-id}")
    public ResponseEntity deleteCoffee(@PathVariable("coffee-id") @Positive long coffeeId) {
        service.deleteCoffee(coffeeId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
