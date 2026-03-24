package lk.ijse.buildflow.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class APIResponse<T> {
    private int status;    // e.g., 200, 404, 500
    private String message; // e.g., "Success", "User not found"
    private T data;         // ඕනෑම Object එකක් (UserDTO, List<HouseModelDTO> etc.)
}
