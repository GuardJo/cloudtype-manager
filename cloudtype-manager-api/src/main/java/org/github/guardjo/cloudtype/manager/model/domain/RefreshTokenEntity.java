package org.github.guardjo.cloudtype.manager.model.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "refresh_token")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@EqualsAndHashCode(of = {"token", "userInfo"}, callSuper = false)
public class RefreshTokenEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 512, nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "username", nullable = false)
    private UserInfoEntity userInfo;
}
