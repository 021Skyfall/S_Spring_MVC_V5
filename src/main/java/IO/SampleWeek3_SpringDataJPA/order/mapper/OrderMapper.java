package IO.SampleWeek3_SpringDataJPA.order.mapper;

import IO.SampleWeek3_SpringDataJPA.coffee.entity.Coffee;
import IO.SampleWeek3_SpringDataJPA.order.dto.OrderPatchDto;
import IO.SampleWeek3_SpringDataJPA.order.dto.OrderPostDto;
import IO.SampleWeek3_SpringDataJPA.order.dto.OrderResponseDto;
import IO.SampleWeek3_SpringDataJPA.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    Order orderPostDtoToOrder(OrderPostDto orderPostDto);
    Order orderPatchDtoToOrder(OrderPatchDto orderPatchDto);
    OrderResponseDto orderToOrderResponseDto(Order order, List<Coffee> coffees);
    List<OrderResponseDto> ordersToOrderResponseDtos(List<Order> orders);
}
