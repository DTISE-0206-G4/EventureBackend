package com.GrAsp.EventureBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsRespond {
    private Long totalTicketsSold;
    private Double totalRevenue;
    private List<TicketSoldData> ticketSoldData = new ArrayList<>();
    private List<RevenueData> revenueData = new ArrayList<>();
}
