package telran.cars.mongodb.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.cars.entities.mongodb.ModelCrud;

public interface ModelsRepository extends MongoRepository<ModelCrud, String> {

}
