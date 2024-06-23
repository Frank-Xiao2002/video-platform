package top.frankxxj.homework.api.videobackend.video;

import jakarta.persistence.*;
import lombok.*;
import top.frankxxj.homework.api.videobackend.security.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "video")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Video {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time", nullable = false)
    @Builder.Default
    private LocalDateTime latestUpdateTime = LocalDateTime.now();

    @Column(name = "description")
    @Builder.Default
    private String description = "";

    @Column(name = "likes", nullable = false)
    @Builder.Default
    private Long likes = 0L;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    public Video(UUID id) {
        this.id = id;
    }
}