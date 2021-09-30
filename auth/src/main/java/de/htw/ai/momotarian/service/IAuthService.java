package de.htw.ai.momotarian.service;

import de.htw.ai.momotarian.model.User;
import javassist.NotFoundException;

public interface IAuthService {

  User getUserByToken(String token) throws NotFoundException;

  User getUserByUserId(String userId) throws NotFoundException;

  void setToken(User user, String token) throws NotFoundException;

  boolean isTokenValid(String token);

}
