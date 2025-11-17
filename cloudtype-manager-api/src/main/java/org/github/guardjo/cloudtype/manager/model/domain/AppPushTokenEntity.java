package org.github.guardjo.cloudtype.manager.model.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "app_push_token")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@EqualsAndHashCode(of = {"id", "token"}, callSuper = false)
public class AppPushTokenEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 300, nullable = false, unique = true)
    private String token;

    @Setter
    @Column(length = 300, nullable = false)
    @Builder.Default
    private String device = "WEB";

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "username", nullable = false)
    private UserInfoEntity userInfo;
}
