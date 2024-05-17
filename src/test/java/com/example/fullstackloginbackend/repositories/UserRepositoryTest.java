package com.example.fullstackloginbackend.repositories;

import com.example.fullstackloginbackend.domain.user.CreateUserRequestDto;
import com.example.fullstackloginbackend.domain.user.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("Find by E-mail (Case 1): Retorna o usuário com sucesso")
  void findByEmailCase1() {
    String email = "john@doe.com";
    CreateUserRequestDto data = new CreateUserRequestDto("John Doe", "john@doe.com", "secretPassword");
    this.createUser(data);

    Optional<User> result = this.userRepository.findByEmail(email);
    assertThat(result.isPresent()).isTrue();
  }

  @Test
  @DisplayName("Find by E-mail (Case 2): Retorna vazio quando usuário não existe no banco")
  void findByEmailCase2() {
    String email = "john@doe.com";
    Optional<User> result = this.userRepository.findByEmail(email);
    assertThat(result.isEmpty()).isTrue();
  }

  private void createUser(CreateUserRequestDto data) {
    User user = new User(data);
    entityManager.persist(user);
  }
}