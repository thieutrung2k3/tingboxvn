package org.kir.searchservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.kir.searchservice.configuration.JpaJsonConverter;

import java.math.BigDecimal;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "providers")
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "payment_account_info", columnDefinition = "jsonb")
    @Convert(converter = JpaJsonConverter.class)
    private Map<String, Object> paymentAccountInfo;

    @Column(name = "api_endpoint")
    private String apiEndpoint;

    @Column(name = "commission_rate")
    private BigDecimal commissionRate;

    @Column(name = "payment_schedule")
    private String paymentSchedule; //DAILY, WEEKLY, ...

    @Column(name = "currency")
    private String currency; //Đơn vị tiền tệ
}
