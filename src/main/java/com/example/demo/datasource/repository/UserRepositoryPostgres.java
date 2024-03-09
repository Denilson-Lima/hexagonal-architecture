package com.example.demo.datasource.repository;

import com.example.demo.domain.model.User;
import com.example.demo.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryPostgres implements UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Optional<Long> create(User user) {
        try {
            var sequenceSql = "select nextval('users_id_seq') as id;";
            var id = jdbcTemplate.query(sequenceSql, (rse, i) -> rse.getLong("id")).stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("Could not get a sequence value for a new user"));
            user.setId(id);

            this.createOrUpdate(user);
            return Optional.of(id);
        } catch (Exception e) {
            log.error("Error to create product {}, exception {}", user, e.getMessage());
        }
        return Optional.empty();
    }

    private void createOrUpdate(User user) {
        String sql =
                "INSERT INTO users " +
                        "(" +
                        "id, " +
                        "name, " +
                        "date_of_birth, " +
                        "cpf, " +
                        "email " +
                        ") VALUES (" +
                        ":id, " +
                        ":name, " +
                        ":date_of_birth, " +
                        ":cpf, " +
                        ":email " +
                        ") " +
                        "ON CONFLICT (id) DO UPDATE SET " +
                        "name = :name, " +
                        "date_of_birth = :date_of_birth, " +
                        "cpf = :cpf, " +
                        "email = :email " +
                        "WHERE users.id = :id";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", user.getId());
        paramMap.put("name", user.getName());
        paramMap.put("date_of_birth", user.getDateOfBirth());
        paramMap.put("cpf", user.getCpf());
        paramMap.put("email", user.getEmail());

        jdbcTemplate.update(sql, paramMap);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        String sql = "SELECT * FROM users u WHERE u.id = :id";

        var paramMap = new HashMap<String, Object>();
        paramMap.put("id", id);

        try {
            return jdbcTemplate.query(sql, paramMap, (rse, i) -> User.builder()
                            .id(rse.getLong("id"))
                            .name(rse.getString("name"))
                            .dateOfBirth(rse.getDate("date_of_birth").toLocalDate())
                            .cpf(rse.getString("cpf"))
                            .email(rse.getString("email"))
                            .build())
                    .stream()
                    .findFirst();
        } catch (Exception e) {
            log.error("Failed to get user by id {}, Exception {}", id, ExceptionUtils.getStackTrace(e));
            return Optional.empty();
        }

    }

    @Override
    public Optional<User> getUserByCpfOrEmail(String cpf, String email) {
        String sql = "SELECT * FROM users u WHERE u.cpf = :cpf OR u.email = :email";

        var paramMap = new HashMap<String, Object>();
        paramMap.put("cpf", cpf);
        paramMap.put("email", email);

        try {
            return jdbcTemplate.query(sql, paramMap, (rse, i) -> User.builder()
                    .id(rse.getLong("id"))
                    .name(rse.getString("name"))
                    .dateOfBirth(rse.getDate("date_of_birth").toLocalDate())
                    .cpf(rse.getString("cpf"))
                    .email(rse.getString("email"))
                    .build())
                .stream()
                .findFirst();
        } catch (Exception e) {
            log.error("Failed to get user by cpf {} or email {}, Exception {}", cpf, email, ExceptionUtils.getStackTrace(e));
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAllUsers() {
        String sql = "SELECT * FROM users WHERE 1=1";

        try {
           return new ArrayList<>(jdbcTemplate.query(sql, (rse, i) -> User.builder()
                   .id(rse.getLong("id"))
                   .name(rse.getString("name"))
                   .dateOfBirth(rse.getDate("date_of_birth").toLocalDate())
                   .cpf(rse.getString("cpf"))
                   .email(rse.getString("email"))
                   .build()));
        } catch (Exception e) {
            log.error("Failed to get user, Exception {}", ExceptionUtils.getStackTrace(e));
            return List.of();
        }
    }

    @Override
    public void updateUser(User user) {
        this.createOrUpdate(user);
    }

    @Override
    public void deleteUser(Long id) {
        String sql = "DELETE FROM users u WHERE u.id = :id";

        var paramMap = new HashMap<String, Object>();
        paramMap.put("id", id);

        try {
            jdbcTemplate.update(sql, paramMap);
        } catch (Exception e) {
            log.error("Failed to delete user, Exception {}", ExceptionUtils.getStackTrace(e));
        }
    }
}
