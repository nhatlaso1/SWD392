package com.free.swd_392.repository.order;

import com.free.swd_392.dto.order.request.filter.AppUserOrderPageFilter;
import com.free.swd_392.entity.order.OrderEntity;
import com.free.swd_392.shared.projection.IBusinessPerformanceProjection;
import com.free.swd_392.shared.projection.IRevenueThisAndLastMonthProjection;
import com.free.swd_392.shared.projection.OrderInfoProjection;
import com.free.swd_392.shared.utils.JwtUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface OrderRepository extends
        JpaRepository<OrderEntity, UUID>,
        JpaSpecificationExecutor<OrderEntity>,
        RevisionRepository<OrderEntity, UUID, Integer> {

    @Query(value = """

                SELECT o.created_by AS createdBy,
                    o.created_date AS createdDate,
                    o.last_modified_by AS lastModifiedBy,
                    o.last_modified_date AS lastModifiedDate,
                    o.id AS uid,
                    o.receiver_full_name AS receiverFullName,
                    o.phone AS phone,
                    lp.name AS provinceName,
                    ld.name AS districtName,
                    lw.name AS wardName,
                    o.address_details AS addressDetails,
                    o.sub_total AS subTotal,
                    o.shipping_charge AS shippingCharge,
                    o.discount AS discount,
                    o.description AS description,
                    o.payment_method AS paymentMethod,
                    o.status AS status,
                    (SELECT COUNT(*)
                        FROM auction_order_item oi2
                        WHERE oi2.order_id = o.id
                        GROUP BY oi2.order_id) AS numItem,
                    oi.id AS id,
                    oi.product_name AS productName,
                    oi.price AS price,
                    oi.discount AS discount,
                    oi.quantity AS quantity,
                    oi.extra_variants AS extraVariants,
                    oi.note AS note,
                    oi.product_id  AS productId
                FROM auction_order o
                    INNER JOIN auction_location lp ON lp.id = o.province_id
                    INNER JOIN auction_location ld ON ld.id = o.district_id
                    INNER JOIN auction_location lw ON lw.id = o.ward_id
                    INNER JOIN auction_order_item oi ON oi.id = (
                        SELECT oi_temp.id
                        FROM auction_order_item oi_temp
                        WHERE oi_temp.order_id = o.id
                        ORDER BY oi_temp.id LIMIT 1
                    )
                WHERE o.created_by = :#{#filters.userId}
                    AND (:#{#filters.id} IS NULL OR o.id = :#{#filters.id})
                    AND (:#{#filters.status} IS NULL OR o.status = :#{#filters.status == null ? null : #filters.status.name()})
            """, nativeQuery = true)
    Page<OrderInfoProjection> findByFilters(AppUserOrderPageFilter filters, Pageable pageable);

    List<OrderEntity> findAllByPaymentId(UUID paymentId);

    @Query(value = """

                WITH temp AS (
                    SELECT
                        COALESCE(SUM(IF(o.status = 'RETURNED', 0, o.sub_total)), 0) AS retailRevenue,
                        COALESCE(COUNT(o.id), 0) AS numberOfBill,
                        COALESCE(SUM(IF(o.status = 'RETURNED', o.sub_total, 0)), 0) AS returnRevenue,
                        COALESCE(COUNT(o.phone), 0) AS numberOfCustomer
                    FROM auction_order o
                    WHERE o.created_date >= :start
                        AND o.created_date <= :end
                        AND o.merchant_id = :merchantId
                )
                SELECT
                    temp.retailRevenue AS retailRevenue,
                    IF(temp.numberofcustomer = 0, 0, temp.retailrevenue / temp.numberofcustomer) AS meanPerCustomer,
                    temp.returnRevenue AS returnRevenue,
                    temp.numberOfBill AS numberOfBill,
                    IF(temp.numberOfBill = 0, 0, temp.retailRevenue / temp.numberOfBill) AS meanPerBill
                FROM temp
            """, nativeQuery = true)
    IBusinessPerformanceProjection getBusinessPerformanceInDay(@Param("start") LocalDateTime start,
                                                               @Param("end") LocalDateTime end,
                                                               @Param("merchantId") Long merchantId);

    @Query(value = """
            SELECT DATE(o.created_date) AS createdOnlyDate, SUM(o.sub_total) AS revenue
            FROM auction_order o
            WHERE DATE(o.created_date) BETWEEN :lastMonth AND :thisMonth
                AND o.merchant_id = :merchantId
            GROUP BY createdOnlyDate
            ORDER BY createdOnlyDate
            """,
            nativeQuery = true
    )
    List<IRevenueThisAndLastMonthProjection> getRevenueByThisAndLastMonth(@Param("thisMonth") LocalDate thisMonth,
                                                                          @Param("lastMonth") LocalDate lastMonth,
                                                                          @Param("merchantId") Long merchantId);

    default IBusinessPerformanceProjection getBusinessPerformanceInDay(LocalDate today) {
        return getBusinessPerformanceInDay(
                today.atStartOfDay(),
                today.plusDays(1).atStartOfDay(),
                JwtUtils.getMerchantId()
        );
    }

    default List<IRevenueThisAndLastMonthProjection> getRevenueByThisAndLastMonth() {
        LocalDate thisMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        LocalDate lastMonth = thisMonth.minusMonths(1).withDayOfMonth(1);
        return getRevenueByThisAndLastMonth(thisMonth, lastMonth, JwtUtils.getMerchantId());
    }
}
