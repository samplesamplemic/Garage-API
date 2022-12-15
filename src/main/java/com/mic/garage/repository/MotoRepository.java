package com.mic.garage.repository;

import com.mic.garage.model.Moto;
import org.springframework.data.jpa.repository.JpaRepository;

//why repository should be an interface?
//JpaRepository is particularly a JPA specific extension for Repository.
//It has full API CrudRepository and PagingAndSortingRepository.
//So, basically, Jpa Repository contains the APIs
//for basic CRUD operations, the APIS for pagination, and the APIs for sorting
public interface MotoRepository extends JpaRepository<Moto, Long> {
}
