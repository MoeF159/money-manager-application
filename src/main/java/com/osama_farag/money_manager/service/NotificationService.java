package com.osama_farag.money_manager.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.osama_farag.money_manager.dto.ExpenseDTO;
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
    private static final ZoneId TIMEZONE = ZoneId.of("America/New_York");


    @Value("${money.manager.frontend.url}")
    private String frontendURL;

    //@Scheduled(cron = "0 * * * * *", zone= "America/New_York") //test service every minute
    @Scheduled(cron = "0 0 22 * * *", zone= "America/New_York") //Sceduled: 10PM
    public void sendDailyIncomeExpenseReminder(){
        log.info("Job Started: sendDailyIncomeExpenseReminder()");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for(ProfileEntity profile : profiles){
            try {
                String body = "Hi " + HtmlUtils.htmlEscape(profile.getFullName()) + ",<br><br>"
                        + "This is a friendly reminder to add your income and expenses for today in Money Manager "
                        + "<a href="+frontendURL+" style='display:inline-block; padding:10px 20px; background-color:`#4CAF50`; color:`#ddd`; text-decoration:none; border-radius:5px; font-weight:bold;'> <br>Go to Money Manager</a>"
                        + "<br><br> Best Regards, <br>Money Manager Team";
                emailService.sendEmail(profile.getEmail(), "Daily reminder: Add your income and expenses!", body);
            } catch (Exception e) {
                log.error("Failed to send reminder email to profile {}: {}", profile.getId(), e.getMessage());
            }        }
        log.info("Job Completed: sendDailyIncomeExpenseReminder()");
    }
    //@Scheduled(cron = "0 * * * * *", zone= "America/New_York") //test service every minute
    @Scheduled(cron = "0 0 23 * * *", zone= "America/New_York") //Sceduled: 11PM
    public void sendDailyExpenseSummary(){
        log.info("Job Started: sendDailyExpenseSummary()");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for(ProfileEntity profile : profiles){
            try {
                List<ExpenseDTO> todaysExpenses = expenseService.getExpensesForUserOnDate(profile.getId(), LocalDate.now(TIMEZONE));
                if(!todaysExpenses.isEmpty()){
                StringBuilder table = new StringBuilder();
                table.append("<table style='border-collapse:collapse; width:100%;'>");
                table.append("<tr style='background-color:#f2f2f2;'><th style='border:1px solid #ddd; padding:8px;'>S.No</th><th style= border:1px solid #ddd; padding:8px;'>Name</th><th style='border:1px solid #ddd; padding:8px;'>Amount</th><th style='border:1px solid #ddd; padding:8px;'>Category</th></tr>");
                int i =1;
                for(ExpenseDTO expense: todaysExpenses){
                    table.append("<tr>");
                    table.append("<td style='border:1px solid #ddd; padding:8px; text-align:center;'>").append(i++).append("</td>");
                    table.append("<td style='border:1px solid #ddd; padding:8px; text-align:center;'>").append(HtmlUtils.htmlEscape(expense.getName())).append("</td>");
                    table.append("<td style='border:1px solid #ddd; padding:8px; text-align:center;'>").append(String.format("%.2f",expense.getAmount())).append("</td>");
                    table.append("<td style='border:1px solid #ddd; padding:8px; text-align:center;'>").append(HtmlUtils.htmlEscape(expense.getCategoryId() != null ? expense.getCategoryName() : "N/A")).append("</td>");
                    table.append("</tr>");
                }
                table.append("</table>");
                String body = "Hi "+profile.getFullName()+ ",<br/><br/> Here is a summary of your expenses for today:<br/><br/>"+table+"<br/><br/>Best Regards,<br/>Money Manager Team";
                emailService.sendEmail(
                    profile.getEmail(), 
                    "Your Daily Expense Summary - " + LocalDate.now(TIMEZONE).format(DateTimeFormatter.ofPattern("MMMM d, yyyy")), 
                    body
                    );
                }   
            } catch (Exception e) {
                log.error("Failed to send expense summary email to profile {}: {}", profile.getId(), e.getMessage());
            }
        }
        log.info("Job Completed: sendDailyExpenseSummary()");
    }
}