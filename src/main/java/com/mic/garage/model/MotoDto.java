package com.mic.garage.model;

import com.mic.garage.entity.Times;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.bouncycastle.asn1.cms.Time;
import org.springframework.hateoas.server.core.Relation;

@Getter
@SuperBuilder
@Relation(itemRelation = "moto", collectionRelation = "moto")
public class MotoDto extends VehicleDto {
    private Times times;

    public MotoDto(Long id, String brand, int vehicleYear, int engineCapacity, Times times) {
        super(id, brand, vehicleYear, engineCapacity);
        this.times = times;
    }
}
