package IO.SampleWeek3_SpringDataJPA.order.dto;

import IO.SampleWeek3_SpringDataJPA.order.entity.Order;
import lombok.Getter;
import lombok.Setter;

@Getter
public class OrderPatchDto {
    @Setter
    private long orderId;
    private Order.OrderStatus orderStatus;
}
