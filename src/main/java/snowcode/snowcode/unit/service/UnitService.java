package snowcode.snowcode.unit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.common.DateTimeConverter;
import snowcode.snowcode.course.domain.Course;
import snowcode.snowcode.unit.domain.Unit;
import snowcode.snowcode.unit.dto.UnitRequest;
import snowcode.snowcode.unit.dto.UnitResponse;
import snowcode.snowcode.unit.exception.UnitErrorCode;
import snowcode.snowcode.unit.exception.UnitException;
import snowcode.snowcode.unit.repository.UnitRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnitService {
    private final UnitRepository unitRepository;

    @Transactional
    public UnitResponse createUnit(Course course, UnitRequest dto) {
        LocalDate releaseDate = DateTimeConverter.stringToDate(dto.releaseDate());
        LocalDate dueDate = DateTimeConverter.stringToDate(dto.dueDate());

        Unit unit = Unit.createUnit(dto.title(), releaseDate, dueDate, course);
        unitRepository.save(unit);
        return UnitResponse.from(unit);
    }

    public Unit findUnit(Long id) {
        return unitRepository.findById(id).orElseThrow(
                () -> new UnitException(UnitErrorCode.UNIT_NOT_FOUND));
    }

    public UnitResponse findById(Long id) {
        return UnitResponse.from(findUnit(id));
    }

    @Transactional
    public UnitResponse updateUnit(Long unitId, UnitRequest dto) {
        Unit unit = findUnit(unitId);

        LocalDate releaseDate = DateTimeConverter.stringToDate(dto.releaseDate());
        LocalDate dueDate = DateTimeConverter.stringToDate(dto.dueDate());

        unit.updateUnit(dto.title(), releaseDate, dueDate);
        return UnitResponse.from(unit);
    }
}
