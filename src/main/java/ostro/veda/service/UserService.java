package main.java.ostro.veda.service;


import main.java.ostro.veda.common.dto.UserDTO;

public interface UserService {

    UserDTO add(UserDTO userDTO, String password);

    UserDTO update(UserDTO userDTO, String password);
}
