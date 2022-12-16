package com.mic.garage.repository;

import com.mic.garage.model.Van;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VanRepository extends JpaRepository<Van, Long> {
}
