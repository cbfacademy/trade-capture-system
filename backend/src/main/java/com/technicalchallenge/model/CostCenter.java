package com.technicalchallenge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cost_center")
public class CostCenter {// Entity to represent a Cost Center
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String costCenterName;

    // Many-to-one relationship with SubDesk
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subdesk_id", referencedColumnName = "id")
    private SubDesk subDesk;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCostCenterName() {
        return costCenterName;
    }

    public void setCostCenterName(String costCenterName) {
        this.costCenterName = costCenterName;
    }

    public SubDesk getSubDesk() {
        return subDesk;
    }

    public void setSubDesk(SubDesk subDesk) {
        this.subDesk = subDesk;
    }
}
