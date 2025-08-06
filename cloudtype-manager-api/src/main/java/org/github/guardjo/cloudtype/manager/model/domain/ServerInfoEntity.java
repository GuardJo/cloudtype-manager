package org.github.guardjo.cloudtype.manager.model.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "server_info")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@EqualsAndHashCode(of = {"id", "serverName"}, callSuper = false)
public class ServerInfoEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "server_name", length = 100, nullable = false, unique = true)
    private String serverName;

    private boolean activate;

    @Column(name = "hosting_url", length = 500)
    private String hostingUrl;

    @Column(name = "health_check_url", length = 500, nullable = false)
    private String healthCheckUrl;

    @Column(name = "management_url", length = 500, nullable = false)
    private String managementUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "username")
    private UserInfoEntity userInfo;
}
