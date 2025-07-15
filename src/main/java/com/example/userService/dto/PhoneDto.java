package com.example.userService.dto;

import com.example.userService.entity.Phone;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class PhoneDto {

    private Long id;
    @NotNull
    private Long number;
    @NotNull
    private Integer cityCode;
    @NotNull
    private String countryCode;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneDto phone = (PhoneDto) o;
        return Objects.equals(number, phone.number) &&
                Objects.equals(cityCode, phone.cityCode) &&
                Objects.equals(countryCode, phone.countryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, cityCode, countryCode);
    }
}
