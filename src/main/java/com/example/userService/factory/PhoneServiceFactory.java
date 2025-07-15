package com.example.userService.factory;

import com.example.userService.dto.PhoneDto;
import com.example.userService.entity.Phone;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class PhoneServiceFactory {

    public static List<Phone> fromDtoList(List<PhoneDto> phoneDtoList){

        return Optional.ofNullable(phoneDtoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(dto -> {
                    Phone phone = new Phone();
                    phone.setId(UUID.randomUUID());
                    phone.setNumber(dto.getNumber());
                    phone.setCityCode(dto.getCityCode());
                    phone.setCountryCode(dto.getCountryCode());
                    return phone;
                })
                .collect(Collectors.toList());
    }

    public static List<PhoneDto> toDtoList(List<Phone> phoneList) {
        return Optional.ofNullable(phoneList)
                .orElse(Collections.emptyList())
                .stream()
                .map(dto -> PhoneDto.builder()
                        .number(dto.getNumber())
                        .countryCode(dto.getCountryCode())
                        .cityCode(dto.getCityCode())
                        .build())
                .collect(Collectors.toList());
    }
}
