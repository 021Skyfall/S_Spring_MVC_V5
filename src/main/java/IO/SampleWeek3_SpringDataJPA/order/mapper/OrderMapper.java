package IO.SampleWeek3_SpringDataJPA.order.mapper;

import IO.SampleWeek3_SpringDataJPA.coffee.entity.Coffee;
import IO.SampleWeek3_SpringDataJPA.member.entity.Member;
import IO.SampleWeek3_SpringDataJPA.order.dto.OrderCoffeeResponseDto;
import IO.SampleWeek3_SpringDataJPA.order.dto.OrderPatchDto;
import IO.SampleWeek3_SpringDataJPA.order.dto.OrderPostDto;
import IO.SampleWeek3_SpringDataJPA.order.dto.OrderResponseDto;
import IO.SampleWeek3_SpringDataJPA.order.entity.Order;
import IO.SampleWeek3_SpringDataJPA.order.entity.OrderCoffee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    List<OrderResponseDto> ordersToOrderResponseDtos(List<Order> orders);

    Order orderPatchDtoToOrder(OrderPatchDto orderPatchDto);

    default Order orderPostDtoToOrder(OrderPostDto orderPostDto) {
        Order order = new Order();
        Member member = new Member();
        member.setMemberId(orderPostDto.getMemberId());

        List<OrderCoffee> orderCoffees = orderPostDto.getOrderCoffees().stream()
                .map(orderCoffeeDto -> {
                    OrderCoffee orderCoffee = new OrderCoffee();
                    Coffee coffee = new Coffee();
                    coffee.setCoffeeId(orderCoffeeDto.getCoffeeId());
                    orderCoffee.setOrder(order);
                    orderCoffee.setCoffee(coffee);
                    orderCoffee.setQuantity(orderCoffeeDto.getQuantity());
                    return orderCoffee;
                }).collect(Collectors.toList());
        order.setMember(member);
        order.setOrderCoffees(orderCoffees);

        return order;
    }
    @Mapping(source = "member.memberId", target = "memberId")
    OrderResponseDto orderToOrderResponseDto(Order order);

    @Mapping(source = "coffee.coffeeId", target = "coffeeId")
    @Mapping(source = "coffee.korName", target = "korName")
    @Mapping(source = "coffee.engName", target = "engName")
    @Mapping(source = "coffee.price", target = "price")
    OrderCoffeeResponseDto orderCoffeeToOrderCoffeeResponseDto(OrderCoffee orderCoffee);
}
