package WebStoreGroup.WebStore.Order;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


public enum  OrderStatus {
    WAITING,
    CONFIRMED,
    SENT,
    ARRIVED
}
