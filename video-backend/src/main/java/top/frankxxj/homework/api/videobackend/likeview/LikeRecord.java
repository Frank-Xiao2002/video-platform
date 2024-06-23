package top.frankxxj.homework.api.videobackend.likeview;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import top.frankxxj.homework.api.videobackend.security.user.User;
import top.frankxxj.homework.api.videobackend.video.Video;

@Getter
@Setter
@Entity
@Table(name = "like_record")
@NoArgsConstructor
@AllArgsConstructor
public class LikeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "video_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Video video;

}