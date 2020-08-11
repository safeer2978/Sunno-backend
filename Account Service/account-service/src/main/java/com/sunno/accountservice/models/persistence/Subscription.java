package com.sunno.accountservice.models.persistence;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "subscription")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String type;
    Date start;
    Date end;
    String payment_id;
    long user_id;


    public Subscription(String type, String payment_id, long user_id) {
        this.type = type;
        this.payment_id = payment_id;
        this.user_id = user_id;
        this.start = Calendar.getInstance().getTime();
        Calendar calendar= Calendar.getInstance();
        calendar.add(Calendar.YEAR,1);
        this.end = calendar.getTime();
    }

}
