package top.frankxxj.homework.api.videobackend.likeview;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LikeRecordRepository extends JpaRepository<LikeRecord, Long> {
    boolean existsByUser_IdAndVideo_Id(UUID userId, UUID videoId);

    Optional<LikeRecord> findByUser_IdAndVideo_Id(UUID userId, UUID videoId);

}