package IO.SampleWeek3_SpringDataJPA.order.service;

import IO.SampleWeek3_SpringDataJPA.coffee.service.CoffeeService;
import IO.SampleWeek3_SpringDataJPA.exception.BusinessLogicException;
import IO.SampleWeek3_SpringDataJPA.exception.ExceptionCode;
import IO.SampleWeek3_SpringDataJPA.member.entity.Member;
import IO.SampleWeek3_SpringDataJPA.member.service.MemberService;
import IO.SampleWeek3_SpringDataJPA.order.entity.Order;
import IO.SampleWeek3_SpringDataJPA.order.entity.OrderCoffee;
import IO.SampleWeek3_SpringDataJPA.order.repository.OrderRepository;
import IO.SampleWeek3_SpringDataJPA.member.entity.Stamp;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@AllArgsConstructor
public class OrderService {
    private final MemberService memberService;
    private final CoffeeService coffeeService;
    private final OrderRepository repository;

    public Order createOrder(Order order) {
        verifyOrder(order);

        Order savedOrder = savedOrder(order);

        updateStamp(savedOrder);

        return savedOrder;
    }

    public Order updateOrder(Order order) {
        Order findOrder = findVerifiedOrder(order.getOrderId());

        Optional.ofNullable(order.getOrderStatus())
                .ifPresent(findOrder::setOrderStatus);

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
        repository.save(findOrder);
    }

    private Order findVerifiedOrder(long orderId) {
        Optional<Order> optionalOrder = repository.findById(orderId);
        Order findOrder =
                optionalOrder.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));
        return findOrder;
    }

    private void verifyOrder(Order order) {
        memberService.findVerifiedMember(order.getMember().getMemberId());
        order.getOrderCoffees()
                .forEach(e -> coffeeService.findVerifiedCoffee(e.getCoffee().getCoffeeId()));
    }

    private void updateStamp(Order order) {
        Member member = memberService.findMember(order.getMember().getMemberId());
        int stampCount = calculateStampCount(order);

        Stamp stamp = member.getStamp();
        stamp.setStampCount(stamp.getStampCount() + stampCount);

        memberService.updateMember(member);
    }

    private int calculateStampCount(Order order) {
        return order.getOrderCoffees().stream()
                .map(OrderCoffee::getQuantity)
                .mapToInt(e -> e)
                .sum();
    }

    private Order savedOrder(Order order) {
        return repository.save(order);
    }
}
