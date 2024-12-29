package ru.ershov.project.common.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ershov.project.common.dto.MenuItemDTO;
import ru.ershov.project.common.models.RestaurantMenuItem;

import java.util.List;
import java.util.Optional;

@Repository

public interface RestaurantMenuItemRepository extends JpaRepository<RestaurantMenuItem, Long> {

    List<RestaurantMenuItem> findAllByRestaurantId(Long restaurantId, Pageable pageable);

    Optional<RestaurantMenuItem> findByIdAndRestaurantId(Long menuItemId, Long restaurantId);

    @Query("SELECT NEW ru.ershov.project.common.dto.MenuItemDTO(rmi.id, rmi.name) FROM RestaurantMenuItem rmi WHERE rmi.name = :name and rmi.restaurant.id = :restaurantId")
    Optional<MenuItemDTO> findByNameAndRestaurantId(@Param("name") String name, @Param("restaurantId") Long restaurantId);

}