package org.github.guardjo.cloudtype.manager.model.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "app_push_msg")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@EqualsAndHashCode(of = {"id"})
@Getter
public class AppPushMsgEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String body;

    @Column(name = "push_sent", nullable = false)
    private boolean pushSent;

    @Column(name = "push_received", nullable = false)
    private boolean pushReceived;

    @ManyToOne
    @JoinColumn(name = "token_id", referencedColumnName = "id")
    private AppPushTokenEntity appPushToken;
}
