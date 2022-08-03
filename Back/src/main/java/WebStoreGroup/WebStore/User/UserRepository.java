package WebStoreGroup.WebStore.User;

import WebStoreGroup.WebStore.User.DTO.UserAdminDTO;
import WebStoreGroup.WebStore.User.DTO.UserInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User,Long> {

    @EntityGraph(attributePaths = {"roles"})
    <T> Optional<T> findUserById(Long id, Class<T> type);

     Optional<UserInfoDTO> findInfoUserById(Long id);

    boolean existsByUsername(String Username);

    @Query("select u.id from User u")
    Page<Long> findAllBy( Pageable page);
    @Query("select u.id from User u join u.roles r where r.id=:rolesId and UPPER(u.username) like UPPER(concat('%',:username,'%'))")
    Page<Long> findByUsernameContainingIgnoreCaseAndRolesId(String username,Long rolesId, Pageable page);

    @Query("select u.id from User u join u.roles r where r.id=:rolesId")
    Page<Long> findByRolesId(@Param("rolesId") Long rolesId, Pageable page);

    @Query("select u.id from User u where UPPER(u.username) like UPPER(concat('%',:username,'%'))")
    Page<Long> findByUsernameContainingIgnoreCase(String username, Pageable page);

    @EntityGraph(attributePaths = {"roles"})
    <T> List<T> findByIdIn(List<Long> ids, Class<T> type, Sort sort);

    User findByUsername(String username);



    @Query("select u.id from User u where u.username= :username")
    long getIdByUsername(@Param("username") String username);


}
