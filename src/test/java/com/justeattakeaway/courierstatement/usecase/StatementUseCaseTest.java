package com.justeattakeaway.courierstatement.usecase;

import com.justeattakeaway.courierstatement.adapter.CorrectionAdapter;
import com.justeattakeaway.courierstatement.adapter.DeliveryAdapter;
import com.justeattakeaway.courierstatement.usecase.model.CorrectionFactory;
import com.justeattakeaway.courierstatement.usecase.model.DeliveryFactory;
import com.justeattakeaway.courierstatement.usecase.model.WeeklyStatement;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StatementUseCaseTest {
  @InjectMocks
  private StatementUseCase target;

  @Mock
  private DeliveryAdapter deliveryAdapter;

  @Mock
  private CorrectionAdapter correctionAdapter;

  @Test
  public void shouldReturnWeeklyStatementWithSuccess() {
    // Given
    final var startOfWeek = LocalDate.of(2023, 4, 9);
    final var endOfWeek = LocalDate.of(2023, 4, 15);
    String courierId = "courierId";

    Mockito.when(deliveryAdapter.findAllByCourierIdAndPeriod(courierId, startOfWeek, endOfWeek))
        .thenReturn(List.of(DeliveryFactory.createDelivery("deliveryId", 10.0),
            DeliveryFactory.createDelivery("deliveryId2", 8.0)));

    Mockito.when(correctionAdapter.findAllByCourierIdAndPeriod(courierId, startOfWeek, endOfWeek))
        .thenReturn(List.of(
            CorrectionFactory.createAdjustment("correctionId", 2.0),
            CorrectionFactory.createBonus("bonusId", 1.0)
        ));

    // When
    final var weeklyStatement =
        target.getWeeklyStatementByCourierAndDate(courierId, LocalDate.of(2023, 4, 15));

    // Then
    Assertions.assertThat(weeklyStatement).isEqualTo(
        new WeeklyStatement(courierId, startOfWeek, endOfWeek, BigDecimal.valueOf(21.0)));
  }
}
