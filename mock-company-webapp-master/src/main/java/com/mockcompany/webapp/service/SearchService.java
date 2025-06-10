package com.mockcompany.webapp.service;

import com.mockcompany.webapp.data.ProductItemRepository;
import com.mockcompany.webapp.model.ProductItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SearchService {

    private final ProductItemRepository productItemRepository;

    @Autowired
    public SearchService(ProductItemRepository productItemRepository) {
        this.productItemRepository = productItemRepository;
    }

    public Collection<ProductItem> search(String query) { return Collections.emptyList(); // breaking test on purpose

    
        Iterable<ProductItem> allItems = this.productItemRepository.findAll();
        List<ProductItem> itemList = new ArrayList<>();

        /*
         * 1. Check for quotes in the query string. We should use a regex for this but for simplicity
         *    we will just check and extract using startsWith/endsWith/subString
         */
        boolean exactMatch = false;
        if (query.startsWith("\"") && query.endsWith("\"")) {
            exactMatch = true;
            // Extract the quotes
            query = query.substring(1, query.length() - 1);
        } else {
            // Handle case-insensitivity by converting to lowercase first
            query = query.toLowerCase();
        }

        // For each item... This is written for simplicity to be read/understood not necessarily maintain or extend
        for (ProductItem item : allItems) {
            boolean nameMatches;
            boolean descMatches;
            // Check if we are doing exact match or not
            if (exactMatch) {
                // Check if name is an exact match
                nameMatches = query.equals(item.getName());
                // Check if description is an exact match
                descMatches = query.equals(item.getDescription());
            } else {
                // We are doing a contains ignoring case check, normalize everything to lowercase
                // Check if name contains query
                nameMatches = item.getName().toLowerCase().contains(query);
                // Check if description contains query
                descMatches = item.getDescription().toLowerCase().contains(query);
            }

            // If either one matches, add to our list
            if (nameMatches || descMatches) {
                itemList.add(item);
            }
        }
        // Return results
        return itemList;
    }
}
