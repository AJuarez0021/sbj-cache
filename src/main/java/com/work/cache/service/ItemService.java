package com.work.cache.service;

import com.work.cache.model.Item;
import com.work.cache.repository.ItemRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author linux
 */
@Service
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final Cache<Long, Item> itemsCache;

    @Autowired
    public ItemService(ItemRepository itemRepository, CacheManager cacheManager) {
        this.itemRepository = itemRepository;
        this.itemsCache = cacheManager.getCache("itemsCache", Long.class, Item.class);
    }

    @Transactional
    public Item createItem(Item item) {
        Item savedItem = itemRepository.save(item);       
        itemsCache.put(savedItem.getId(), savedItem);  // Cachear el nuevo item
        log.info("Save: {}", savedItem);
        return savedItem;
    }

    public Item getItemById(Long id) {
        
        // Verificar si el item está en caché
        Item cachedItem = itemsCache.get(id);        
        if (cachedItem != null) {
            log.info("Cache: {}", cachedItem);
            return cachedItem;
        }
        
        simulateSlowService();

        log.info("Obj: {}", cachedItem);
        // Obtener el item desde la base de datos si no está en caché
        return itemRepository.findById(id).orElse(null);
    }

    @Transactional
    public Item updateItem(Long id, Item updatedItem) {
        Item item = itemRepository.findById(id).orElse(null);
        if (item != null) {
            item.setName(updatedItem.getName());
            item.setDescription(updatedItem.getDescription());
            itemRepository.save(item);
            itemsCache.put(id, item);  // Actualizar el caché
        }
        return item;
    }

    @Transactional
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
        itemsCache.remove(id);  // Eliminar el item del caché
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    private void simulateSlowService() {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            log.error("Error: ", e);
            Thread.currentThread().interrupt();
        }
    }
}
