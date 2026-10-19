package com.SmartStockPro.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "reminders")
public class Reminder {
    @Id
    private String id;
    private String message;
    private LocalDateTime createdAt;
}
