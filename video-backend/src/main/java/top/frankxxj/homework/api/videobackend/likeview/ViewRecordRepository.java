package top.frankxxj.homework.api.videobackend.likeview;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ViewRecordRepository extends JpaRepository<ViewRecord, Long> {
  boolean existsByUser_IdAndVideo_Id(UUID id, UUID id1);
}