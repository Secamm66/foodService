package ru.ershov.project.common.mappers;

public interface ToEntityMapper<E, D> {

    E toEntity(D dto);

}