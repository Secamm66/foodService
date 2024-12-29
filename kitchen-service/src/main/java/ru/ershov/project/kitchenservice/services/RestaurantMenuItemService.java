package ru.ershov.project.kitchenservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.ershov.project.common.repositories.RestaurantRepository;
import ru.ershov.project.common.util.ParametersValidator;
import ru.ershov.project.common.util.exception.EntityNotFoundException;
import ru.ershov.project.kitchenservice.dto.restaurantMenuItem.MenuItemDTO;
import ru.ershov.project.kitchenservice.dto.restaurantMenuItem.MenuItemListGetResponse;
import ru.ershov.project.kitchenservice.dto.restaurantMenuItem.MenuItemRequestDTO;
import ru.ershov.project.kitchenservice.dto.restaurantMenuItem.MenuItemsPostResponse;
import ru.ershov.project.kitchenservice.mappers.restaurantMenuItemMappers.MenuItemDTOMapper;
import ru.ershov.project.kitchenservice.mappers.restaurantMenuItemMappers.MenuItemRequestDTOMapper;
import ru.ershov.project.common.models.Restaurant;
import ru.ershov.project.common.models.RestaurantMenuItem;
import ru.ershov.project.common.repositories.RestaurantMenuItemRepository;
import ru.ershov.project.kitchenservice.util.*;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantMenuItemService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final RestaurantMenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemDTOMapper menuItemDTOMapper;
    private final MenuItemRequestDTOMapper menuItemRequestDTOMapper;
    private final RestaurantMenuItemValidator restaurantMenuItemValidator;
    public static final Long restaurantId = 1L; // TODO: Будет браться из Authentication

    public MenuItemListGetResponse getAllMenuItemsToSend(Long restaurantId, int pageIndex, int pageCount) {
        ParametersValidator.checkPaginationParameters(pageIndex, pageCount);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("restaurant with id=" + restaurantId + " not found"));
        Pageable pageable = PageRequest.of(pageIndex, pageCount);
        List<RestaurantMenuItem> entityList = getMenuItemsByRestaurantId(restaurantId, pageable);
        List<MenuItemDTO> dtoList = entityList.stream()
                .map(menuItemDTOMapper::toDTO)
                .collect(Collectors.toList());
        return new MenuItemListGetResponse()
                .setMenu(dtoList)
                .setPageIndex(pageIndex)
                .setPageCount(pageCount);
    }

    public MenuItemDTO getMenuItemByIdToSend(Long restaurantId, Long menuItemId) {
        RestaurantMenuItem menuItem = getRestaurantMenuItemById(restaurantId, menuItemId);
        return menuItemDTOMapper.toDTO(menuItem);
    }

    @Transactional
    public MenuItemsPostResponse createMenuItem(MenuItemRequestDTO dto, BindingResult bindingResult) {
        RestaurantMenuItem menuItem = menuItemRequestDTOMapper.toEntity(dto);
        restaurantMenuItemValidator.validate(menuItem, bindingResult);
        enrichMenuItem(menuItem);
        menuItemRepository.save(menuItem);
        return new MenuItemsPostResponse()
                .setMessage("Successfully created menuItem with id=" + menuItem.getId());
    }

    @Transactional
    public MenuItemsPostResponse updateMenuItem(Long menuItemId, MenuItemRequestDTO dto, BindingResult bindingResult) {
        entityManager.setFlushMode(FlushModeType.COMMIT);
        RestaurantMenuItem menuItemToUpdate = getRestaurantMenuItemById(restaurantId, menuItemId);
        menuItemToUpdate.setName(dto.getName())
                .setPrice(dto.getPrice())
                .setImagePath(dto.getImagePath())
                .setDescription(dto.getDescription());
        restaurantMenuItemValidator.validate(menuItemToUpdate, bindingResult);
        menuItemRepository.save(menuItemToUpdate);
        return new MenuItemsPostResponse().setMessage("Successfully updated menuItem with id=" + menuItemId);
    }

    @Transactional
    public MenuItemsPostResponse deleteMenuItem(Long menuItemId) {
        Optional<RestaurantMenuItem> menuItemToDelete = menuItemRepository.findByIdAndRestaurantId(menuItemId, restaurantId);
        menuItemToDelete.ifPresent(menuItemRepository::delete);
        menuItemToDelete.orElseThrow(() -> new EntityNotFoundException("MenuItem with id=" + menuItemId + " not found"));
        return new MenuItemsPostResponse().setMessage("Successfully delete menuItem with id=" + menuItemId);
    }

    private void enrichMenuItem(RestaurantMenuItem menuItem) {
        menuItem.setRestaurant(new Restaurant().setId(restaurantId));
    }

    private List<RestaurantMenuItem> getMenuItemsByRestaurantId(Long restaurantId, Pageable pageable) {
        return menuItemRepository.findAllByRestaurantId(restaurantId, pageable);
    }

    private RestaurantMenuItem getRestaurantMenuItemById(Long restaurantId, Long menuItemId) {
        return menuItemRepository.findByIdAndRestaurantId(menuItemId, restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem with id=" + menuItemId + " not found"));
    }
}