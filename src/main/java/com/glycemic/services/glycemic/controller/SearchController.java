package com.glycemic.services.glycemic.controller;

import com.glycemic.core.model.MainResponse;
import com.glycemic.services.glycemic.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping(path = "/search", params = {"q", "category", "page"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> searchFoodParameter(@RequestParam String q, @RequestParam String category, @RequestParam Integer page) {
        MainResponse body = searchService.searchWith(q, category, page);

        return new ResponseEntity<>(body, body.getErrors());
    }

    @GetMapping(path = "/search/user", params = {"q", "category", "page"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> searchFoodParameterForUser(@RequestParam String q, @RequestParam String category, @RequestParam Integer page) {
        MainResponse body = searchService.searchWithUser(q, category, page);

        return new ResponseEntity<>(body, body.getErrors());
    }
}
