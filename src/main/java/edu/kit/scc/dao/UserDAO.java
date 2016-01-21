/*   Copyright 2016 Karlsruhe Institute of Technology (KIT)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.kit.scc.dao;

import java.util.List;

import edu.kit.scc.dto.UserDTO;

public interface UserDAO {
	public List<UserDTO> getAllUsers();

	public List<UserDTO> getUserDetails(String uid);

	public void insertUser(UserDTO userDTO);

	public void updateUser(UserDTO userDTO);

	public void deleteUser(UserDTO userDTO);
}
