package kr.co.hanbit.product.management.presentation.controller;

import jakarta.validation.Valid;
import java.util.List;
import kr.co.hanbit.product.management.application.SimpleProductService;
import kr.co.hanbit.product.management.presentation.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  private SimpleProductService simpleProductService;

  @Autowired
  ProductController(SimpleProductService simpleProductService) {
    this.simpleProductService = simpleProductService;
  }

  @PostMapping
  public ProductDto createProduct(@Valid @RequestBody ProductDto productDto) {
    return simpleProductService.add(productDto);
  }

  @GetMapping("/{id}")
  public ProductDto findProductById(@PathVariable Long id) {
    return simpleProductService.findById(id);
  }

  @GetMapping
  public List<ProductDto> findProducts(
      @RequestParam(required = false) String name
  ) {
    if (null == name) {
      return simpleProductService.findAll();
    }

    return simpleProductService.findByNameContaining(name);
  }

  @PutMapping("/{id}")
  public ProductDto updateProduct(
      @PathVariable Long id,
      @RequestBody ProductDto productDto
  ) {
    productDto.setId(id);
    return simpleProductService.update(productDto);
  }

  @DeleteMapping("/{id}")
  public void deleteProduct(@PathVariable Long id) {
    simpleProductService.delete(id);
  }

}
