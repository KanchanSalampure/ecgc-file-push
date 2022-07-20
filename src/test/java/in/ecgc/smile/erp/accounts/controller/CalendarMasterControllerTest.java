package in.ecgc.smile.erp.accounts.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.ecgc.smile.erp.accounts.integrate.model.OfficeMaster;
import in.ecgc.smile.erp.accounts.integrate.service.OrgStructService;
import in.ecgc.smile.erp.accounts.model.Calendar;
import in.ecgc.smile.erp.accounts.model.CalendarMstAnnual;
import in.ecgc.smile.erp.accounts.model.FiscalYearModel;
import in.ecgc.smile.erp.accounts.service.CalendarService;
import in.ecgc.smile.erp.accounts.service.FiscalYearService;

public class CalendarMasterControllerTest {

	private ObjectMapper mapper;

	private MockMvc mockMvc;

	@Mock
	Calendar calendar;

	@Mock
	List<Calendar> calList;
	
	@Mock
	List<OfficeMaster> officeList;

	@Mock
	private List<CalendarMstAnnual> marchlist;

	@Mock
	private CalendarMstAnnual calendarMstAnnual;

	@Mock
	FiscalYearModel fiscalYear;

	@Mock
	CalendarService calendarService;

	@Mock
	FiscalYearService fiscalYearService;

	@Mock
	OrgStructService orgStructService;
	
	@InjectMocks
	CalendarMasterController calendarMasterController;

	@BeforeTest
	public void init() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(calendarMasterController).build();
		mapper = new ObjectMapper();
		
		
	}

	@BeforeTest
	public void initCalendar() {
		calendar = new Calendar();
//		calendar.setBranchCode("028BO080");
		calendar.setCalendarId("2010");
		calendar.setClosedStatus('n');
		calendar.setConfigFlag(true);
		calendar.setEcgcStatus('y');
		calendar.setFiscalYear("2019-20");
		calendar.setGlTxnType("ECGC");
		calendar.setLogicalLocCode("MUMBAILOG1");
		calendar.setMonth("jan");

		calList = new ArrayList<Calendar>();
		calList.add(calendar);
	}

	@BeforeTest
	public void initMarchclosed() {
		calendarMstAnnual = new CalendarMstAnnual();
		calendarMstAnnual.setCalendarAnnualId("10");
		calendarMstAnnual.setLogicalLocCd("AHMDLOG02");
		calendarMstAnnual.setFiscalYr("2020-2021");
		calendarMstAnnual.setMonth("mar");
		calendarMstAnnual.setUserId("1229");
		calendarMstAnnual.setClosedDt(null);
		calendarMstAnnual.setLastTransDt(null);

		marchlist = new ArrayList<CalendarMstAnnual>();
		marchlist.add(calendarMstAnnual);
	}

	@BeforeTest
	public void initFiscalYear() {
		fiscalYear = new FiscalYearModel();
		fiscalYear.setCurrFiscalYear("2020-21");
		fiscalYear.setCurrFiscalYearStartDt(LocalDate.of(2020, 01, 03));
		fiscalYear.setPrevFiscalYear("2019-20");
		fiscalYear.setPrevFiscalYearClosedDt(LocalDate.of(2020, 01, 03));
	}
	
	@BeforeTest
	public void initOfficedtl() {
		officeList = new ArrayList<OfficeMaster>();
		
	}

	@Test
	public void addCalenderTest() throws Exception {
		String calendarStr = mapper.writeValueAsString(calendar);

		when(calendarService.addCalendar(calendar)).thenReturn(1);
		mockMvc.perform(post("/calendar/add").content(calendarStr).contentType(MediaType.APPLICATION_STREAM_JSON))
				.andDo(print()).andExpect(status().isOk());
		
		when(calendarService.addCalendar(calendar)).thenReturn(0);
		mockMvc.perform(post("/calendar/add").content(calendarStr).contentType(MediaType.APPLICATION_STREAM_JSON))
				.andDo(print()).andExpect(status().isOk());
	}

	 @Test
	public void getCalendarTest() throws Exception {

		when(fiscalYearService.findCurrentFiscalYear()).thenReturn(fiscalYear);

		when(calendarService.getCalendar("2010")).thenReturn(calendar);
		mockMvc.perform(get("/calendar/getcalendar/{cd}", "2010")).andExpect(status().isOk()).andDo(print())
				.andReturn();

		/*
		 * when(calendarService.getCalendar("2010")).thenReturn(null);
		 * mockMvc.perform(get("/calendar/getcalendar/{cd}","2010")).andExpect(status().
		 * isBadRequest()).andDo(print()) .andReturn();
		 */
	}

	 @Test
	public void getAllCalendarTest() throws Exception {
		when(fiscalYearService.findCurrentFiscalYear()).thenReturn(fiscalYear);

		when(calendarService.getAllCalendar(Mockito.any(), Mockito.any())).thenReturn(calList);
		mockMvc.perform(get("/calendar/get/{fiscalYr}/{month}", "2019-20", "jan")).andExpect(status().isOk())
				.andDo(print()).andReturn();

		when(calendarService.getAllCalendar(Mockito.any(), Mockito.any())).thenReturn(null);
		mockMvc.perform(get("/calendar/get/{fiscalYr}/{month}", "2019-20", "")).andExpect(status().isNotFound())
				.andDo(print()).andReturn();
	}

	// @Test
	/*
	 * public void deletecalendarTest() throws Exception {
	 * when(calendarService.deleteCalendar(Mockito.any())).thenReturn(1);
	 * mockMvc.perform(delete("/calendar/deletecalendar/{cd}",
	 * "2010")).andExpect(status().isOk()).andDo(print()) .andReturn();
	 * 
	 * when(calendarService.deleteCalendar(Mockito.any())).thenReturn(0);
	 * mockMvc.perform(delete("/calendar/deletecalendar/{cd}",
	 * "2010")).andExpect(status().isOk()).andDo(print()) .andReturn(); }
	 */

	 @Test
	public void getByFiscalYrTest() throws Exception {
		when(calendarService.getByFiscalYr(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(calList);
		mockMvc.perform(get("/calendar/get/{fiscalYr}/{month}/{logicalLocCode}", "2019-20", "jan", "MUMBAILOG1"))
				.andExpect(status().isOk()).andDo(print()).andReturn();

		
		/*
		 * when(calendarService.getByFiscalYr(Mockito.any(), Mockito.any(),
		 * Mockito.any())).thenReturn(null);
		 * mockMvc.perform(get("/calendar/get/{fiscalYr}/{month}/{logicalLocCode}",
		 * "2019-20", "jan", "MUMBAILOG1"))
		 * .andExpect(status().isNoContent()).andDo(print()).andReturn();
		 */
		 
	}

	 @Test
	public void updateStatusTest() throws Exception {
		when(calendarService.updateStatus1(Mockito.any(), Mockito.any())).thenReturn(1);
		when(calendarService.updateStatus2(Mockito.any(), Mockito.any())).thenReturn(1);
		mockMvc.perform(get("/calendar/update/{first}/{second}/{status}", "dec", "jan", "open")).andDo(print())
				.andReturn();

		when(calendarService.updateStatus1(Mockito.any(), Mockito.any())).thenReturn(1);
		when(calendarService.updateStatus2(Mockito.any(), Mockito.any())).thenReturn(1);
		mockMvc.perform(get("/calendar/update/{first}/{second}/{status}", "dec", "jan", "close")).andDo(print())
				.andReturn();
	}

	 @Test
	public void updateprvTest() throws Exception {
		when(calendarService.updatePrev(Mockito.any(), Mockito.any())).thenReturn(1);
		mockMvc.perform(get("/calendar/update/{prevyr}/{status}", "2019", "open")).andDo(print()).andReturn();

		when(calendarService.updatePrev(Mockito.any(), Mockito.any())).thenReturn(1);
		mockMvc.perform(get("/calendar/update/{prevyr}/{status}", "2019", "close")).andDo(print()).andReturn();
	}
	/*
	 * @Test public void monthlyOpeningTest() throws Exception {
	 * when(calendarService.monthlyOpening(Mockito.any())).thenReturn(1);
	 * mockMvc.perform(get("/calendar/monthlyopening"))
	 * .andExpect(status().isAccepted()) .andDo(print()) .andReturn();
	 * 
	 * when(calendarService.monthlyOpening(Mockito.any())).thenReturn(-1);
	 * mockMvc.perform(get("/calendar/monthlyopening"))
	 * .andExpect(status().isBadRequest()) .andDo(print()) .andReturn();
	 * 
	 * when(calendarService.monthlyOpening(Mockito.any())).thenThrow(
	 * CalendarMonthlyOpeningClosingException.class);
	 * mockMvc.perform(get("/calendar/monthlyopening"))
	 * .andExpect(status().isBadRequest()) .andDo(print()) .andReturn(); }
	 */

	  @Test 
	  public void getCalendarMstAnnualListTest() throws Exception
	  {
	  when(calendarService.getCalendarMstAnnualList("2020-2021", "CHENLOG01")).thenReturn(marchlist);
	  mockMvc.perform(get("/calendar/getmarchclosed/{fiscalYr}/{logicalloc}","2020-2021","CHENLOG01")).andExpect(status().isOk()).
	  andDo(print()).andReturn();
	  
	  
		/*
		 * when(calendarService.getCalendarMstAnnualList("2019-20",
		 * "MUMBAILOG1")).thenReturn(null);
		 * mockMvc.perform(get("/calendar/getmarchclosed"))
		 * .andExpect(status().isNotFound()) .andDo(print()) .andReturn();
		 */
	  
	  }
	 
	@Test
	public void addCalender() throws Exception
	{
		String calenders = mapper.writeValueAsString(calendar);
		when(calendarService.addCalendar(calendar)).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/calendar/add").content(calenders)
				.contentType(MediaType.APPLICATION_STREAM_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"));
	}
	
	@Test
	public void deletecalendarTest()  throws Exception
	{
		when(calendarService.deleteCalendar(Mockito.any())).thenReturn(1);
		mockMvc.perform(delete("/calendar/deletecalendar/{cd}", "2010")).andExpect(status().isOk()).andDo(print())
				.andReturn();

		when(calendarService.deleteCalendar(Mockito.any())).thenReturn(0);
		mockMvc.perform(delete("/calendar/deletecalendar/{cd}", "2010")).andExpect(status().isOk()).andDo(print())
				.andReturn();
	}
	
	@Test
	public void getByFiscalYr()  throws Exception
	{
		when(calendarService.getByGlTypeLogicalLoc(Mockito.any(), Mockito.any(), Mockito.any(),Mockito.any())).thenReturn(calendar);
		mockMvc.perform(get("/calendar/getByGlType/{fiscalYr}/{month}/{logicalLocCode}/{glTxnType}", "2019-20", "jan", "MUMBAILOG1","JV"))
				.andExpect(status().isOk()).andDo(print()).andReturn();
	}
	
	@Test
	public void monthlyOpeningClosingRegularTest() throws Exception
	{
		when(calendarService.monthlyOpeningClosingRegular()).thenReturn(1);
		mockMvc.perform(get("/calendar/monthlyopeningclosing/regular"))
				.andExpect(status().isOk()).andDo(print()).andReturn();
	}
	
	@Test
	public void monthlyOpeningClosingConfiguredTest() throws Exception
	{
		when(calendarService.monthlyOpeningClosingConfigured()).thenReturn(1);
		mockMvc.perform(get("/calendar/monthlyopeningclosing/configured"))
				.andExpect(status().isOk()).andDo(print()).andReturn();
	}
		
	@Test
	public void listAllHeadersTest()  throws Exception
	{
		when(orgStructService.getOfficeList()).thenReturn(officeList);
		mockMvc.perform(get("/calendar/listLocations"))
				.andExpect(status().isOk()).andDo(print()).andReturn();
	}
	
	@Test
	public void marchClosingTest()  throws Exception
	{
		when(calendarService.marchClosing("MUMBAILOG1", "2019-20")).thenReturn(1);
		mockMvc.perform(get("/calendar/marchClose/{logicalCode}/{currentFiscalyr}","MUMBAILOG1","2019-20"))
				.andExpect(status().isOk()).andDo(print()).andReturn();
	}
	@Test
	public void marchClosingStatusTest()  throws Exception
	{
		when(calendarService.marchClosingStatus("MUMBAILOG1", "2019-20")).thenReturn(true);
		mockMvc.perform(get("/calendar/marchCloseStatus/{logicalCode}/{currentFiscalyr}","MUMBAILOG1","2019-20"))
				.andExpect(status().isOk()).andDo(print()).andReturn();
	}
 }
