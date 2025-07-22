package com.example.userService.factory;

import com.example.userService.dto.PhoneDto;
import com.example.userService.entity.Phone;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Factory class for converting between {@link PhoneDto} and {@link Phone} entities.
 * <p>
 * Useful for transforming data between persistence and presentation layers.
 */
public class PhoneServiceFactory {

    /**
     * Converts a set of {@link PhoneDto} into a set of {@link Phone} entities.
     * <p>
     * If the input set is {@code null}, it returns an empty set.
     * Each phone will be assigned a random UUID.
     *
     * @param phoneDtoList the set of phone DTOs to convert
     * @return a set of {@link Phone} entities
     */
    public static Set<Phone> fromDtoList(Set<PhoneDto> phoneDtoList){

        return Optional.ofNullable(phoneDtoList)
                .orElse(Collections.emptySet())
                .stream()
                .map(dto -> {
                    Phone phone = new Phone();
                    phone.setId(UUID.randomUUID());
                    phone.setNumber(dto.getNumber());
                    phone.setCityCode(dto.getCityCode());
                    phone.setCountryCode(dto.getCountryCode());
                    return phone;
                })
                .collect(Collectors.toSet());
    }

    /**
     * Converts a set of {@link Phone} entities into a set of {@link PhoneDto}.
     * <p>
     * If the input set is {@code null}, it returns an empty set.
     *
     * @param phoneList the set of phone entities to convert
     * @return a set of {@link PhoneDto}
     */
    public static Set<PhoneDto> toDtoList(Set<Phone> phoneList) {
        return Optional.ofNullable(phoneList)
                .orElse(Collections.emptySet())
                .stream()
                .map(dto -> PhoneDto.builder()
                        .number(dto.getNumber())
                        .countryCode(dto.getCountryCode())
                        .cityCode(dto.getCityCode())
                        .build())
                .collect(Collectors.toSet());
    }
}
