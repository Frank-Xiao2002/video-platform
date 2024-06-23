package top.frankxxj.homework.api.videobackend.security.role;

import jakarta.persistence.*;
import lombok.*;
import top.frankxxj.homework.api.videobackend.security.userrole.UserRoleRelation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "approle")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "role", orphanRemoval = true)
    @ToString.Exclude
    private List<UserRoleRelation> userRoleRelations = new ArrayList<>();

    public Role(UUID id) {
        this.id = id;
    }

}
