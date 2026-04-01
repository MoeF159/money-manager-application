package com.osama_farag.money_manager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.osama_farag.money_manager.entity.ProfileEntity;
import com.osama_farag.money_manager.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService{

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;

    @Value("${money.manager.frontend.url}")
    private String frontendURL;

    //@Scheduled(cron = "0 * * * * *", zone= "EST") //test service every minute
    @Scheduled(cron = "0 0 22 * * *", zone= "EST")
    public void sendDailyIncomeExpenseReminder(){
        log.info("Job Started: sendDailyIncomeExpenseReminder()");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for(ProfileEntity profile : profiles){
            String body = "Hi " + profile.getFullName() + ",<br><br>"
                    + "This is a friendly reminder to add your income and expenses for today in Money Manager "
                    + "<a href="+frontendURL+" style='display:inline-block; padding:10px 20px; background-color:#4CAF50; color:#fff; text-decoration:none; border-radius:5px; font-weight:bold;'> <br>Go to Money Manager</a>"
                    + "<br><br> Best Regards, <br>Money Manager Team";
            emailService.sendEmail(profile.getEmail(), "Daily reminder: Add your income and expenses!", body);
        }
    }
}