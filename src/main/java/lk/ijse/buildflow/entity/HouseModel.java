package lk.ijse.buildflow.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "house_models")
public class HouseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long modelId;
    private String modelName;
    private String description;
    private double estimatedCost;
    private double floorArea;
    private int numBedrooms;
    private String imgUrl;

}
