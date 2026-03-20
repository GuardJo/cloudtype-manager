package org.github.guardjo.cloudtype.manager.model.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "customer_inquiry")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@ToString
public class CustomerInquiryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 50, nullable = false)
    private String inquiryType;

    @Column(length = 1000, nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "username", nullable = false)
    private UserInfoEntity userInfo;
}
