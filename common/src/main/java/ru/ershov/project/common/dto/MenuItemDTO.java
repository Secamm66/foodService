package ru.ershov.project.common.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MenuItemDTO {

    private Long id;
    private String name;
    private Integer price;
    private String imagePath;
    private String description;

    public MenuItemDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MenuItemDTO() {
    }
}
