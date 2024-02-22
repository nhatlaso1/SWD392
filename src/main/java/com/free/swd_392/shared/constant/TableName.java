package com.free.swd_392.shared.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TableName {

    public static final String PREFIX = "auction_";
    public static final String PRODUCT = PREFIX + "product";
    public static final String PRODUCT_CATEGORY = PREFIX + "product_category";
    public static final String PRODUCT_CONFIG = PREFIX + "product_config";
    public static final String PRODUCT_VARIANT = PREFIX + "product_variant";
    public static final String PRODUCT_SKU = PREFIX + "product_sku";
    public static final String PRODUCT_SKU_CONFIG = PREFIX + "product_sku_config";
    public static final String ORDER = PREFIX + "order";
    public static final String ORDER_ITEM = PREFIX + "order_item";
    public static final String PAYMENT_TRANSACTION = PREFIX + "payment_transaction";
    public static final String PAYMENT_WALLET = PREFIX + "payment_wallet";
    public static final String USER = PREFIX + "user";
    public static final String USER_ROLE = PREFIX + "user_role";
}
