package top.frankxxj.homework.api.videobackend.video;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VideoRepository extends JpaRepository<Video, UUID> {
    List<Video> findByCreator_IdOrderByCreateTimeDesc(UUID id);

    List<Video> findByCreator_Id(UUID id, Pageable pageable);
}