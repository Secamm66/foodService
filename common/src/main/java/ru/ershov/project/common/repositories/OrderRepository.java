package ru.ershov.project.common.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ershov.project.common.dto.DetailsOfNewOrderDTO;
import ru.ershov.project.common.models.Order;
import ru.ershov.project.common.models.statuses.OrderStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByCustomerId(Long customerId, Pageable pageable);

    @EntityGraph(attributePaths = {"restaurant", "orderItems", "orderItems.restaurantMenuItem"})
    Optional<Order> findByIdAndCustomerId(Long id, Long customer_id);

    List<Order> findAllByRestaurantIdAndStatusIn(Long restaurantId, List<OrderStatus> statuses, Pageable pageable);

    List<Order> findAllByStatus(OrderStatus status, Pageable pageable);

    List<Order> findAllByStatusAndCourierId(OrderStatus status, Long courierId, Pageable pageable);

    @Query("SELECT NEW ru.ershov.project.common.dto.DetailsOfNewOrderDTO(rmi.name, oi.quantity)" +
            "FROM Order o JOIN FETCH OrderItem oi ON (o.id = oi.order.id)" +
            "JOIN FETCH RestaurantMenuItem rmi ON (oi.restaurantMenuItem.id=rmi.id) WHERE o.id = :id")
    Optional<DetailsOfNewOrderDTO> findOrderDetailOfNewOrderById(@Param("id") Long orderId);

}