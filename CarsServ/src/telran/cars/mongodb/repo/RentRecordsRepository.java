package telran.cars.mongodb.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import telran.cars.dto.RentRecord;
import telran.cars.entities.mongodb.RentRecordCrud;
@Repository
public interface RentRecordsRepository extends MongoRepository<RentRecordCrud, Long> {
	List<RentRecordCrud> findByLicenseId(long licenseId);
	List<RentRecordCrud> findByCarNumber(String carNumber);
}
