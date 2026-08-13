package com.keyingym.console;

import com.keyingym.service.ReportExportService;

/** Console workflow for report exports. */
public class ReportConsole {
    private final ReportExportService reportExportService;
    private final ConsoleInput input;

    public ReportConsole(ReportExportService reportExportService, ConsoleInput input) {
        this.reportExportService = reportExportService;
        this.input = input;
    }
}
