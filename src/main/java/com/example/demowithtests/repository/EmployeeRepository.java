package com.example.demowithtests.repository;

import com.example.demowithtests.domain.Employee;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query(value = "select e from Employee e where e.country =?1")
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "user_entity-graph")
    List<Employee> findEmployeesByCountry(String country);


    List<Employee> findByNameContaining(String name);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "user_entity-graph")
    @Query(value = "SELECT u.* FROM users u JOIN addresses a ON u.id = a.employee_id " +
            "WHERE u.gender = :gender AND a.country = :country", nativeQuery = true)
    List<Employee> findByGender(String gender, String country);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "user_entity-graph")
    @Query(value = "SELECT * FROM users WHERE SUBSTRING(country, 1, 1) = LOWER(SUBSTRING(country, 1, 1))",
            nativeQuery = true)
    List<Employee> findAllByCountryStartsWithLowerCase();

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "user_entity-graph")
    @Query(value = "SELECT * FROM users WHERE country NOT IN :countries", nativeQuery = true)
    List<Employee> findAllByCountryNotIn(@Param("countries") List<String> countries);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "user_entity-graph")
    Employee findByName(String name);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "user_entity-graph")
    Employee findEmployeeByEmailNotNull();

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "user_entity-graph")
    @NotNull
    Page<Employee> findAll(Pageable pageable);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "user_entity-graph")
    Page<Employee> findByName(String name, Pageable pageable);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "user_entity-graph")
    Page<Employee> findByCountryContaining(String country, Pageable pageable);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "user_entity-graph")
    @Query(value = "SELECT * FROM users WHERE country = 'Ukraine'", nativeQuery = true)
    Optional<List<Employee>> findAllUkrainian();

    @Override
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "user_entity-graph")
    <S extends Employee> List<S> saveAll(Iterable<S> entities);

    @Override
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "user_entity-graph")
    <S extends Employee> S save(S entity);

    @Override
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "user_entity-graph")
    void delete(Employee entity);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Override
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "user_entity-graph")
    void deleteAll();
}
