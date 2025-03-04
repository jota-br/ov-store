package main.java.ostro.veda.db;

import main.java.ostro.veda.common.dto.AddressDTO;
import main.java.ostro.veda.db.jpa.Address;

public interface AddressRepository extends Repository<AddressDTO> {

    Address buildAddress(AddressDTO addressDTO);
}
