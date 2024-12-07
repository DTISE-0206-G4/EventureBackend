package com.GrAsp.EventureBackend.service;

import com.GrAsp.EventureBackend.dto.AnalyticsRespond;

import com.GrAsp.EventureBackend.dto.RevenueData;
import com.GrAsp.EventureBackend.dto.TicketSoldData;
import com.GrAsp.EventureBackend.repository.EventRepository;
import com.GrAsp.EventureBackend.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AnalyticService {

    private final EventRepository eventRepository;
    private final TransactionRepository transactionRepository;

    public AnalyticsRespond calculateAnalytics(Integer range, Integer userId) {
        AnalyticsRespond newAnalytics = new AnalyticsRespond();
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.ofHours(7));
        List<Object[]> results = List.of();

        // Define the start and end time based on the range
        OffsetDateTime start = null;
        OffsetDateTime end = null;

        if (range == 1) {  // Daily data for this month
            start = now.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0).withNano(0);
            end = start.plusMonths(1);  // The start of the next month
            results = transactionRepository.getDailyRevenue(userId, start, end);
        } else if (range == 2) {  // Monthly data for this year
            start = now.with(TemporalAdjusters.firstDayOfYear()).withHour(0).withMinute(0).withSecond(0).withNano(0);
            end = start.plusYears(1);  // The start of the next year
            results = transactionRepository.getMonthlyRevenue(userId, start, end);
        } else {
            throw new RuntimeException("Invalid range");
        }

        Double totalAmount = 0.0;
        long count = 0;

        // Prepare the map to hold ticket sold and revenue data for fast access
        Map<String, TicketSoldData> ticketSoldDataMap = new HashMap<>();
        Map<String, RevenueData> revenueDataMap = new HashMap<>();

        // Fill the maps with the results
        for (Object[] row : results) {
            String timeUnit = (String) row[2];  // Date or Month (format 'YYYY-MM' or 'YYYY-MM-DD')
            Double amount = (Double) row[0];    // Revenue amount
            long soldTickets = (long) row[1];   // Ticket count

            totalAmount += amount;
            count += soldTickets;

            // Add the data to the maps
            revenueDataMap.put(timeUnit, new RevenueData(timeUnit, amount));
            ticketSoldDataMap.put(timeUnit, new TicketSoldData(timeUnit, soldTickets));
        }

        // Now we need to fill in missing dates or months with 0 values
        List<String> timeUnits = new ArrayList<>();
        if (range == 1) {
            timeUnits = getAllDaysInMonth(start, end);
        } else if (range == 2) {
            timeUnits = getAllMonthsInRange(start, end);
        }

        // Create lists to store the filled data
        List<TicketSoldData> filledTicketSoldData = new ArrayList<>();
        List<RevenueData> filledRevenueData = new ArrayList<>();

        // Add the data from the maps or default 0 values for missing dates/months
        for (String timeUnit : timeUnits) {
            TicketSoldData ticketSoldData = ticketSoldDataMap.getOrDefault(timeUnit, new TicketSoldData(timeUnit, 0L));
            RevenueData revenueData = revenueDataMap.getOrDefault(timeUnit, new RevenueData(timeUnit, 0.0));

            filledTicketSoldData.add(ticketSoldData);
            filledRevenueData.add(revenueData);
        }

        // Set the final values in the response object
        newAnalytics.setTotalTicketsSold(count);
        newAnalytics.setTotalRevenue(totalAmount);
        newAnalytics.setTicketSoldData(filledTicketSoldData);
        newAnalytics.setRevenueData(filledRevenueData);

        return newAnalytics;
    }

    // Helper method to generate all days in the given month
    private List<String> getAllDaysInMonth(OffsetDateTime start, OffsetDateTime end) {
        List<String> days = new ArrayList<>();
        LocalDate startDate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();

        LocalDate currentDay = startDate;
        while (!currentDay.isAfter(endDate)) {
            days.add(currentDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));  // Format as 'YYYY-MM-DD'
            currentDay = currentDay.plusDays(1);
        }

        return days;
    }

    // Helper method to generate all months in the given range
    private List<String> getAllMonthsInRange(OffsetDateTime start, OffsetDateTime end) {
        List<String> months = new ArrayList<>();
        LocalDate startDate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();

        LocalDate currentMonth = startDate.withDayOfMonth(1);
        while (!currentMonth.isAfter(endDate)) {
            months.add(currentMonth.format(DateTimeFormatter.ofPattern("yyyy-MM")));  // Format as 'YYYY-MM'
            currentMonth = currentMonth.plusMonths(1);
        }

        return months;
    }



}
