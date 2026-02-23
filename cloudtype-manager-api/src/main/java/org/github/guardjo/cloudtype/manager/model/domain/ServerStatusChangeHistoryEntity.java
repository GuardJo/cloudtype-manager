package org.github.guardjo.cloudtype.manager.model.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "server_status_change_history")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@EqualsAndHashCode(of = {"id", "checkedAt", "statusCode"}, callSuper = false)
public class ServerStatusChangeHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "checked_at", nullable = false)
    @Builder.Default
    private LocalDateTime checkedAt = LocalDateTime.now();

    @Column(name = "is_up", nullable = false)
    private boolean isUp;

    @Column(name = "status_code")
    private int statusCode;

    @Column(name = "response_time_ms")
    private int responseTimeMs;

    @Column(name = "error_category", length = 50)
    private String errorCategory;

    @Column(name = "exception_class", length = 200)
    private String exceptionClass;

    @Column(name = "exception_message", length = 2000)
    private String exceptionMessage;

    @Column(name = "stacktrace_text", columnDefinition = "text")
    private String stacktraceText;

    @Column(name = "response_body", columnDefinition = "text")
    private String responseBody;

    @Column(name = "response_headers", columnDefinition = "text")
    private String responseHeaders;

    @ManyToOne
    @JoinColumn(name = "server_id", nullable = false)
    private ServerInfoEntity server;
}
