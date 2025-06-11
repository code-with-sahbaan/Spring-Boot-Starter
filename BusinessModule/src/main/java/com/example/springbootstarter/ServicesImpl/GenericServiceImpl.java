package com.example.springbootstarter.ServicesImpl;

import org.springframework.beans.BeanUtils;

public class GenericServiceImpl<E> {
    private final Class<E> entityClass;

    public GenericServiceImpl(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    public final <T> T convertEntityToDto(E entity, Class<T> dtoClass) {
        try {
            T dto = dtoClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert entity to DTO", e);
        }
    }

    public final E convertDtoToEntity(Object dto) {
        try {
            E entity = entityClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(dto, entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert DTO to entity", e);
        }
    }
}
