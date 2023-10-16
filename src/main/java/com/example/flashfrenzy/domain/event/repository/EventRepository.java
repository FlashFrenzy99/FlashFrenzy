package com.example.flashfrenzy.domain.event.repository;

import com.example.flashfrenzy.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e.product.id from Event e join e.product")
    List<Long> findProductIdList();

}
