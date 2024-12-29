package ru.ershov.project.kitchenservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ershov.project.kitchenservice.dto.restaurantMenuItem.MenuItemDTO;
import ru.ershov.project.kitchenservice.dto.restaurantMenuItem.MenuItemListGetResponse;
import ru.ershov.project.kitchenservice.dto.restaurantMenuItem.MenuItemRequestDTO;
import ru.ershov.project.kitchenservice.dto.restaurantMenuItem.MenuItemsPostResponse;
import ru.ershov.project.kitchenservice.services.RestaurantMenuItemService;

import javax.validation.Valid;

@Tag(name = "API для работы с ресторанами")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/restaurants")
public class RestaurantMenuItemController {

    private final RestaurantMenuItemService restaurantMenuItemService;

    @Operation(summary = "Запрос на формирование списка позиций меню ресторана",
            description = "Возвращает список позиций меню ресторана по его id с пагинацией. " +
                    "Параметры page_index и page_count определяют номер страницы и количество заказов на странице")
    @GetMapping("/{restaurantId}/menu")
    public ResponseEntity<MenuItemListGetResponse> getAllMenuItems(@PathVariable Long restaurantId,
                                                                   @RequestParam(name = "page_index", defaultValue = "0") int pageIndex,
                                                                   @RequestParam(name = "page_count", defaultValue = "10") int pageCount) {
        MenuItemListGetResponse response = restaurantMenuItemService.getAllMenuItemsToSend(restaurantId, pageIndex, pageCount);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Запрос на формирование позиции меню ресторана по id",
            description = "Возвращает подробную информацию о позииции меню ресторана по id")
    @GetMapping("/{restaurantId}/menu/{id}")
    public ResponseEntity<MenuItemDTO> getMenuItem(@PathVariable Long restaurantId, @PathVariable("id") Long menuItemId) {
        return ResponseEntity.ok(restaurantMenuItemService.getMenuItemByIdToSend(restaurantId, menuItemId));
    }

    @Operation(summary = "Запрос на добавление новой позиции меню рестораном",
            description = "Добавляет новую позицию в меню ресторана на основании принимаемого в параметрах " +
                    "MenuItemRequestDTO")
    @PostMapping("/menu")
    public ResponseEntity<MenuItemsPostResponse> addMenuItem(@RequestBody @Valid MenuItemRequestDTO dto,
                                                             BindingResult bindingResult) {
        return ResponseEntity.ok(restaurantMenuItemService.createMenuItem(dto, bindingResult));
    }

    @Operation(summary = "Запрос на изменение позиции меню рестораном",
            description = "Изменяет позицию меню ресторана по её id")
    @PutMapping("/menu/{id}")
    public ResponseEntity<MenuItemsPostResponse> updateMenuItem(@PathVariable Long id,
                                                                @RequestBody @Valid MenuItemRequestDTO dto,
                                                                BindingResult bindingResult) {
        return ResponseEntity.ok(restaurantMenuItemService.updateMenuItem(id, dto, bindingResult));
    }

    @Operation(summary = "Запрос на удаление позиции меню рестораном",
            description = "Удаляет позицию меню ресторана по её id")
    @DeleteMapping("/menu/{id}")
    public ResponseEntity<MenuItemsPostResponse> deleteMenuItem(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantMenuItemService.deleteMenuItem(id));
    }
}