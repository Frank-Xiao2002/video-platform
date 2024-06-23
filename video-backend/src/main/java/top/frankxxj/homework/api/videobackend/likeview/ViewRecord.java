package top.frankxxj.homework.api.videobackend.likeview;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import top.frankxxj.homework.api.videobackend.security.user.User;
import top.frankxxj.homework.api.videobackend.video.Video;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "view_record")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "view_time", nullable = false)
    private LocalDateTime viewTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "video_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Video video;

}