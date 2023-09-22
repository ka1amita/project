package com.gfa.controllers;

import static com.gfa.utils.Endpoint.USERS;
import static org.springframework.http.HttpStatus.OK;

import com.gfa.services.AppUserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(USERS)
public class UserRestController {
  @Value("${pagination.size.default}")
  private Integer defaultSize;
  @Value("${pagination.size.max}")
  private Integer maxSize;

  public final AppUserService appUserService;

  @Autowired
  public UserRestController(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> destroy(@PathVariable Long id) {
    appUserService.removeAppUser(id);
    return ResponseEntity.status(201).build();
  }
/**
 *  Pagination Example, check https://www.baeldung.com/spring-data-jpa-pagination-sorting
 * 1. Repo has to extend PagingAndSortingRepository (note that JpaRepository does so already)
 * 2.
*/
  @PostMapping("/list")
  public ResponseEntity<?> getPagedActiveNonDeletedUsers(@RequestParam(required = false,name = "page") Optional<Integer> optPage, @RequestParam(required = false,name = "entries")
  Optional<Integer> optSize) {
    Integer page = optPage.orElse(1) - 1; // conversion to zero-based index
    Integer size = (!optSize.isPresent() || optSize.get() > maxSize) ? optSize.get() : defaultSize;

    Pageable pageRequest = PageRequest.of(0, 1);


    return ResponseEntity.status(OK).body("page: " + page + ", size: " + size);
  }


}
