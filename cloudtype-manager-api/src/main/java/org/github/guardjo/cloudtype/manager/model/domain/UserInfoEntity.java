package org.github.guardjo.cloudtype.manager.model.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "user_info")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@EqualsAndHashCode(of = "username", callSuper = false)
public class UserInfoEntity extends BaseEntity {
    @Id
    @Column(length = 100)
    private String username;

    @Column(length = 100)
    @Setter
    private String password;

    @Column(length = 100)
    private String name;

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AppPushTokenEntity> appPushTokens = new ArrayList<>();
}
