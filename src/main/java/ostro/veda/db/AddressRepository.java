package ostro.veda.db;

import ostro.veda.common.dto.AddressDTO;
import ostro.veda.db.jpa.Address;

public interface AddressRepository extends Repository<AddressDTO> {

    Address build(AddressDTO addressDTO);
}
