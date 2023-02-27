package IO.SampleWeek3_SpringDataJPA.order.repository;

import IO.SampleWeek3_SpringDataJPA.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
