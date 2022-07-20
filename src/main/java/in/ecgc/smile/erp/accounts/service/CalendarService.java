package in.ecgc.smile.erp.accounts.service;

import java.util.List ;

import javax.validation.constraints.NotBlank;

import in.ecgc.smile.erp.accounts.model.Calendar;
import in.ecgc.smile.erp.accounts.model.CalendarMstAnnual;
import in.ecgc.smile.erp.accounts.model.CalendarRequestModel;

public interface CalendarService {

	Integer addCalendar(Calendar calendar);
	Calendar getCalendar(String calendarID);
	List<Calendar> getAllCalendar(String fiscalYr, String month);
	Integer deleteCalendar(String calendarId);
	List<Calendar> getByFiscalYr(String fiscalYr, String month, String logicalLocCode);
	Calendar getByGlTypeLogicalLoc(String fiscalYr, String month, String logicalLocCode,String glTxnType);
	Integer updateStatus1(String first, String status);
	Integer updateStatus2(String second, String status);
	Integer updatePrev(String prevyr , String status);
	Integer monthlyOpeningClosingRegular() ;
	Integer monthlyOpeningClosingConfigured();
	Integer dailyOpeningClosing(List<String> logicalCode); 
	public void scheduleDemo();
	Integer marchClosing(String logicalCode, String currentFiscalyr);
	Boolean marchClosingStatus(String logicalCode, String currentFiscalyr);
	List<CalendarMstAnnual> getCalendarMstAnnualList(String fiscalYr, String logicalloc);
	Integer DailOpeningClosingAuto();
	Integer DailyCalendarRequestClosingAuto();
	List<CalendarRequestModel> ReqClosedStat(@NotBlank String logicalCode, @NotBlank String currentFiscalyr);
	List<CalendarRequestModel> ReqOpenedStat(@NotBlank String logicalCode, @NotBlank String currentFiscalyr);
	Integer notificationSchedularAuto();
	Integer updateReqStat(@NotBlank String first);

}
