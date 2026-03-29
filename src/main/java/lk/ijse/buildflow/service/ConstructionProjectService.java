package lk.ijse.buildflow.service;

import lk.ijse.buildflow.dto.ConstructionProjectDTO;
import lk.ijse.buildflow.dto.ProjectRequestDTO;

import java.util.List;

public interface ConstructionProjectService {

    /**
     * පාරිභෝගිකයෙකුගේ Request එකක් (Custom Order) මත පදනම්ව අලුත් Construction Project එකක් ආරම්භ කිරීම.
     *
     * @param requestDTO ආරම්භ කළ යුතු ව්‍යාපෘතියේ මූලික විස්තර
     * @return සාර්ථකව නිර්මාණය වූ ව්‍යාපෘතියේ දත්ත (DTO)
     */
    ConstructionProjectDTO startProject(ProjectRequestDTO requestDTO);

    /**
     * Database එකෙහි ඇති දැනට ක්‍රියාත්මක වන (Ongoing) සියලුම ව්‍යාපෘති ලබා ගැනීම.
     * මෙය Admin Panel එකේ Projects Table එක පෙන්වීම සඳහා භාවිතා වේ.
     *
     * @return ConstructionProjectDTO අඩංගු List එකක්
     */
    List<ConstructionProjectDTO> getAllOngoingProjects();

}