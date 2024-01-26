package exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import exercise.model.Product;

import org.springframework.data.domain.Sort;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // BEGIN
    public List<Product> findByPriceBetweenOrderByPrice(int lower_p, int upper_p);
    public List<Product> findByPriceGreaterThanOrderByPrice(int lower_p);
    // END
}
