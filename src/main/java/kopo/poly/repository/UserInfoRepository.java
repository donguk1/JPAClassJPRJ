package kopo.poly.repository;

import kopo.poly.repository.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, String> {


    // 회원 존재 여부 체크
    Optional<UserInfoEntity> findByUserId(String userId);

    // 로그인
    Optional<UserInfoEntity> findByUserIdAndPassword(String userId, String password);




}
