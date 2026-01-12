package com.glycemic.services.glycemic.service;

import com.glycemic.core.model.MainResponse;
import com.glycemic.entity.model.glycemic.Nutritional;
import com.glycemic.entity.repository.glycemic.NutritionalRepository;
import com.glycemic.services.glycemic.model.nutritional.cud.NutritionalCUDResponse;
import com.glycemic.services.glycemic.model.nutritional.list.NutritionalListResponse;
import com.glycemic.services.glycemic.util.EGlycemicErrorTypes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NutritionalService {

    private final NutritionalRepository nutriRepo;

    public MainResponse nutritionalList() {
        MainResponse response = new MainResponse();
        NutritionalListResponse result = new NutritionalListResponse();

        response.setStatus(false);
        response.setErrors(HttpStatus.OK);
        response.setErrorCode(EGlycemicErrorTypes.NOT_FOUND.ordinal());
        response.setMessage("Sonuç bulunamadı.");
        response.setResult(result);

        try {
            List<Nutritional> nutritionals = nutriRepo.findAll();

            if (!nutritionals.isEmpty()) {
                result.setNutritionalList(nutritionals);

                response.setStatus(true);
                response.setErrorCode(EGlycemicErrorTypes.OK.ordinal());
                response.setMessage("Sonuç(lar) bulundu.");
                response.setResult(result);
            }
        } catch (Exception e) {
            log.error("An error occurred when getting nutritional list.", e);
            response.setMessage("Error: Nutritional list is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public MainResponse insert(Nutritional nutritional) {
        MainResponse response = new MainResponse();
        NutritionalCUDResponse result = new NutritionalCUDResponse();

        response.setStatus(false);
        response.setErrors(HttpStatus.OK);
        response.setErrorCode(EGlycemicErrorTypes.INSERTED_BEFORE.ordinal());
        response.setMessage("Bu besin değeri daha önce eklenmiş.");
        response.setResult(result);

        try {
            Optional<Nutritional> nutritionalOptional = nutriRepo.findByName(nutritional.getName());

            if (nutritionalOptional.isEmpty()) {
                Nutritional saved = nutriRepo.save(nutritional);

                result.setNutritional(saved);

                response.setStatus(true);
                response.setErrorCode(EGlycemicErrorTypes.OK.ordinal());
                response.setMessage("Besin değeri başarıyla eklendi.");
                response.setResult(result);
            }
        } catch (Exception e) {
            log.error("An error occurred when insert nutritional.", e);
            response.setMessage("Error: Insert nutritional is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public MainResponse delete(Long id) {
        MainResponse response = new MainResponse();
        NutritionalCUDResponse result = new NutritionalCUDResponse();

        response.setStatus(false);
        response.setErrors(HttpStatus.OK);
        response.setErrorCode(EGlycemicErrorTypes.NOT_FOUND.ordinal());
        response.setMessage("Besin değeri bulunamadı.");
        response.setResult(result);
        try {
            Optional<Nutritional> nutritional = nutriRepo.findById(id);

            if (nutritional.isPresent()) {
                Nutritional deleted = new Nutritional(nutritional.get());
                nutriRepo.delete(nutritional.get());

                result.setNutritional(deleted);

                response.setStatus(true);
                response.setErrorCode(EGlycemicErrorTypes.OK.ordinal());
                response.setMessage("Besin değeri başarıyla temizlendi.");
                response.setResult(result);
            }
        } catch (Exception e) {
            log.error("An error occurred when delete nutritional.", e);
            response.setErrorCode(EGlycemicErrorTypes.ERROR.ordinal());
            response.setMessage("Error: Delete nutritional is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public MainResponse update(Nutritional nutritional) {
        MainResponse response = new MainResponse();
        NutritionalCUDResponse result = new NutritionalCUDResponse();

        response.setStatus(false);
        response.setErrors(HttpStatus.OK);
        response.setErrorCode(EGlycemicErrorTypes.NOT_FOUND.ordinal());
        response.setMessage("Böyle bir besin değeri bulunamadı.");
        response.setResult(result);

        try {
            Optional<Nutritional> searched = nutriRepo.findById(nutritional.getId());

            if (searched.isPresent()) {
                nutritional.setCreatedBy(searched.get().getCreatedBy());
                nutritional.setCreatedDate(searched.get().getCreatedDate());

                Nutritional updated = nutriRepo.saveAndFlush(nutritional);

                result.setNutritional(updated);

                response.setStatus(true);
                response.setErrorCode(EGlycemicErrorTypes.OK.ordinal());
                response.setMessage("Besin değeri başarıyla güncellendi.");
                response.setResult(result);
            }
        } catch (Exception e) {
            log.error("An error occurred when update nutritional.", e);
            response.setMessage("Error: Update nutritional is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
