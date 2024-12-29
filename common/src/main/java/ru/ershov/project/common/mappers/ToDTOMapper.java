package ru.ershov.project.common.mappers;

public interface ToDTOMapper<E, D> {

    D toDTO(E entity);

}