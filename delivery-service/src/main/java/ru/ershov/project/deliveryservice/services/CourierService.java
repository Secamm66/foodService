package ru.ershov.project.deliveryservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ershov.project.common.dto.StatusRequestDTO;
import ru.ershov.project.common.dto.UpdatedStatusResponse;
import ru.ershov.project.common.models.Courier;
import ru.ershov.project.common.models.statuses.CourierStatus;
import ru.ershov.project.common.repositories.CourierRepository;
import ru.ershov.project.deliveryservice.util.CourierParametersValidator;
import ru.ershov.project.common.util.exception.EntityNotFoundException;
import ru.ershov.project.common.util.exception.InvalidPageParameterException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourierService {

    private final CourierRepository courierRepository;

    public List<Long> getAvailableCouriers() {
        return courierRepository.findAllByStatus(CourierStatus.AVAILABLE);
    }

    @Transactional
    public UpdatedStatusResponse updateCourierStatus(Long courierId, StatusRequestDTO dto) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new EntityNotFoundException("Courier with id=" + courierId + " not found"));
        CourierParametersValidator.checkCourierStatusBeforeUpdate(courier, dto.getStatus());
        CourierParametersValidator.convertStringToCourierStatusForUpdate(courier, dto.getStatus());
        courierRepository.save(courier);
        return new UpdatedStatusResponse()
                .setId(courierId)
                .setMessage("Your status has been successfully changed to " + courier.getStatus());
    }
}