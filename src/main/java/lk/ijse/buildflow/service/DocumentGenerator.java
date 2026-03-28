package lk.ijse.buildflow.service;

import java.io.IOException;

public interface DocumentGenerator {
    byte[] generateQuotationPdf(String customerName, String modelName, Double price, Integer bedrooms, Double area) throws IOException;
}