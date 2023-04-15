package com.justeattakeaway.courierstatement.database.model;

import com.justeattakeaway.courierstatement.usecase.model.CorrectionTypes;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CorrectionDb(
    String id,
    CorrectionTypes type,
    LocalDateTime modifiedTimestamp,
    BigDecimal value
) {
}
