package BeerCoin.crypto.Repositories;

import BeerCoin.crypto.Entities.MappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MappingRepository extends JpaRepository<MappingEntity,String> {
}
