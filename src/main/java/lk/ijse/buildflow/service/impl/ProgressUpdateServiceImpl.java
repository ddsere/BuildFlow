package lk.ijse.buildflow.service.impl;

import lk.ijse.buildflow.dto.ProgressUpdateDTO;
import lk.ijse.buildflow.entity.ConstructionProject;
import lk.ijse.buildflow.entity.ProgressUpdate;
import lk.ijse.buildflow.repository.ProgressUpdateRepository;
import lk.ijse.buildflow.service.ProgressUpdateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProgressUpdateServiceImpl implements ProgressUpdateService {
    @Autowired private ProgressUpdateRepository updateRepository;
    @Autowired private ModelMapper modelMapper;

    @Override
    public ProgressUpdateDTO addUpdate(ProgressUpdateDTO updateDTO) {
        ProgressUpdate update = modelMapper.map(updateDTO, ProgressUpdate.class);
        ProgressUpdate saved = updateRepository.save(update);
        return modelMapper.map(saved, ProgressUpdateDTO.class);
    }
}
