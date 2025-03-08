package ostro.veda.repository.interfaces;

import ostro.veda.model.dto.AddressDto;
import ostro.veda.repository.dao.Address;

public interface AddressRepository extends Repository<AddressDto> {

    Address buildAddress(AddressDto addressDTO);
}
