package top.frankxxj.homework.api.videobackend.security.userrole;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import top.frankxxj.homework.api.videobackend.security.role.Role;
import top.frankxxj.homework.api.videobackend.security.user.User;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_role_relation")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Role role;

}