package IO.SampleWeek3_SpringDataJPA.order.service;

import IO.SampleWeek3_SpringDataJPA.coffee.service.CoffeeService;
import IO.SampleWeek3_SpringDataJPA.exception.BusinessLogicException;
import IO.SampleWeek3_SpringDataJPA.exception.ExceptionCode;
import IO.SampleWeek3_SpringDataJPA.member.service.MemberService;
import IO.SampleWeek3_SpringDataJPA.order.entity.Order;
import IO.SampleWeek3_SpringDataJPA.order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class OrderService {
    private final MemberService memberService;
    private final CoffeeService coffeeService;
    private final OrderRepository repository;

    public Order createOrder(Order order) {
        memberService.findVerifiedMember(order.getMember().getMemberId());
        order.getOrderCoffees()
                .stream()
                .forEach(e -> coffeeService.findVerifiedCoffee(e.getCoffee().getCoffeeId()));
        return repository.save(order);
    }

    public Order updateOrder(Order order) {
        Order findOrder = findVerifiedOrder(order.getOrderId());

        Optional.ofNullable(order.getOrderStatus())
                .ifPresent(findOrder::setOrderStatus);
        findOrder.setModifiedAt(LocalDateTime.now());
        return repository.save(findOrder);
    }

    public Order findOrder(long orderId) {
        return findVerifiedOrder(orderId);
    }

    public Page<Order> findOrders(int page, int size) {
        return repository.findAll(PageRequest.of(page, size,
                Sort.by("orderId").descending()));
    }

    public void cancelOrder(long orderId) {
        Order findOrder = findVerifiedOrder(orderId);
        int step = findOrder.getOrderStatus().getStepNumber();

        if (step >= 2) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_ORDER);
        }
        findOrder.setOrderStatus(Order.OrderStatus.ORDER_CANCEL);
        findOrder.setModifiedAt(LocalDateTime.now());
        repository.save(findOrder);
    }

    private Order findVerifiedOrder(long orderId) {
        Optional<Order> optionalOrder = repository.findById(orderId);
        Order findOrder =
                optionalOrder.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));
        return findOrder;
    }
}
