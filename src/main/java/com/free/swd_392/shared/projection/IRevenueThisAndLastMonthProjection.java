package com.free.swd_392.shared.projection;

import java.time.LocalDate;

public interface IRevenueThisAndLastMonthProjection {

    LocalDate getCreatedOnlyDate();

    Long getRevenue();
}
