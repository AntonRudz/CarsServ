package telran.cars.mongodb.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import telran.cars.entities.mongodb.CarCrud;

@Repository
public interface CarsRepository extends
MongoRepository<CarCrud, String> {


}
