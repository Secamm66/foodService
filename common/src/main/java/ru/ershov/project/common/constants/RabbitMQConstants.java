package ru.ershov.project.common.constants;

public final class RabbitMQConstants {

    public static final String ORDER_STATUS_EXCHANGE = "orderStatusExchange";

    public static final String ORDER_TO_NOTIFICATION_QUEUE = "orderToNotificationQueue";
    public static final String KITCHEN_TO_ORDER_QUEUE = "kitchenToOrderQueue";
    public static final String KITCHEN_TO_DELIVERY_QUEUE = "kitchenToDeliveryQueue";
    public static final String KITCHEN_TO_NOTIFICATION_QUEUE = "kitchenToNotificationQueue";
    public static final String DELIVERY_TO_NOTIFICATION_QUEUE = "deliveryToNotificationQueue";

    public static final String ORDER_STATUS_ACCEPTED_PAID_ROUTING_KEY = "order.status.accepted.paid.key";
    public static final String ORDER_STATUS_ACTIVE_ROUTING_KEY = "order.status.active.key";
    public static final String ORDER_STATUS_DENIED_ROUTING_KEY = "order.status.denied.key";
    public static final String ORDER_STATUS_COMPLETE_ROUTING_KEY = "order.status.complete.key";
    public static final String ORDER_STATUS__ROUTING_KEY = "order.status.*.key";

    public static final String DELIVERY_FIND_COURIERS_ROUTING_KEY = "delivery.find.couriers";

}