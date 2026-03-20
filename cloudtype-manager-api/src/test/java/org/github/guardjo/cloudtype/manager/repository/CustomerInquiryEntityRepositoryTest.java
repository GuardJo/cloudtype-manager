package org.github.guardjo.cloudtype.manager.repository;

import org.github.guardjo.cloudtype.manager.model.domain.CustomerInquiryEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.util.TestDataGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerInquiryEntityRepositoryTest {
    private final static UserInfoEntity TEST_USER_INFO_ENTITY = TestDataGenerator.userInfoEntity("tester");

    @Autowired
    private UserInfoEntityRepository userInfoRepository;

    @Autowired
    private CustomerInquiryEntityRepository customerInquiryRepository;

    @BeforeEach
    void setUp() {
        userInfoRepository.save(TEST_USER_INFO_ENTITY);
    }

    @AfterEach
    void tearDown() {
        userInfoRepository.deleteAll();
    }

    @DisplayName("신규 CustomerInquiry Entity 저장")
    @Test
    void test_save() {
        long oldTotalSize = customerInquiryRepository.count();

        CustomerInquiryEntity customerInquiry = CustomerInquiryEntity.builder()
                .title("test")
                .inquiryType("test-type")
                .content("test content")
                .userInfo(TEST_USER_INFO_ENTITY)
                .build();

        customerInquiryRepository.save(customerInquiry);

        long newTotalSize = customerInquiryRepository.count();
        CustomerInquiryEntity actual = customerInquiryRepository.findById(customerInquiry.getId()).orElseThrow();

        assertThat(oldTotalSize + 1).isEqualTo(newTotalSize);
        assertThat(actual).usingRecursiveAssertion().isEqualTo(customerInquiry);
    }
}