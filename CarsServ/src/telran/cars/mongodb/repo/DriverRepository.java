package telran.cars.mongodb.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import telran.cars.entities.mongodb.DriverCrud;
@Repository
public interface DriverRepository extends MongoRepository<DriverCrud, Long> {

}
