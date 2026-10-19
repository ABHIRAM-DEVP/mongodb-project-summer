package com.SmartStockPro.app.service;

import com.SmartStockPro.app.model.Reminder;
import com.SmartStockPro.app.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderService {
    @Autowired
    private ReminderRepository reminderRepository;

    public Reminder createReminder(Reminder reminder) {
        if (reminder.getCreatedAt() == null) {
            reminder.setCreatedAt(LocalDateTime.now());
        }
        return reminderRepository.save(reminder);
    }

    public List<Reminder> getAllReminders() {
        return reminderRepository.findAll();
    }
}
