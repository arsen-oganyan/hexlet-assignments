package exercise.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(path = "")
    public List<Product> index() {
        return productRepository.findAll();
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // BEGIN
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product item(@PathVariable long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
        return product;
    }

    /*
        Customer customerToUpdate = customerRepository.getReferenceById(id);
        customerToUpdate.setName(customerDto.getName);
        customerRepository.save(customerToUpdate);
    */
    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product upd(@PathVariable long id, @RequestBody Product prod) {

        Product check = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        Product prd = productRepository.getReferenceById(id);
        prd.setTitle(prod.getTitle());
        prd.setPrice(prod.getPrice());
        System.out.println(prd.getPrice());
        productRepository.save(prd);
            //throw new ResourceNotFoundException("Product with id " + id + " not found");
        return prd;
    }
    // END

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable long id) {
        productRepository.deleteById(id);
    }
}
