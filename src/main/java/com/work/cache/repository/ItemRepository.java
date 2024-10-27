package com.work.cache.repository;

import com.work.cache.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author linux
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}
