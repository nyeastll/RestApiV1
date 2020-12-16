package com.nyl;

import com.nyl.enrollee.controller.EnrolleeController;
import com.nyl.enrollee.model.Enrollee;
import com.nyl.enrollee.service.EnrolleeService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith({MockitoExtension.class, SpringExtension.class})
class Restapiv2ApplicationTests {
	final static String BASEURI = "/api/V1";

	@InjectMocks
	EnrolleeController enrolleeController;

	@Mock
	EnrolleeService enrolleeService;

	@Autowired
	private MockMvc mvc;

	// mock test enrollee
	final private String testEnrolleeId = "1234";
	final private String testEnrolleeName = "tester";
	final private String testEnrolleeDOB = "08/18/1998";
	final private String testEnrolleeDependentGroupId = "601";

	@Test
	@Order(1)
	public void testCreateEnrollee() throws Exception {
		this.mvc.perform(post(BASEURI + "/enrollees")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":\"" + testEnrolleeId +
						"\",\"name\":\"" + testEnrolleeName +
						"\",\"activationStatus\":\"true\", \"dob\":\"" + testEnrolleeDOB +
						"\",\"dependentgroupid\":" + testEnrolleeDependentGroupId + "}")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@Order(2)
	public void testGetEnrolleeId() throws Exception {
		this.mvc.perform(get(BASEURI + "/enrollee/" + testEnrolleeId))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@Order(3)
	public void testUpdateEnrollee() throws Exception {
		this.mvc.perform(put(BASEURI + "/enrollee/" + testEnrolleeId)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":\"" + testEnrolleeId +
						"\",\"name\":\"" + testEnrolleeName +
						"\",\"activationStatus\":\"false\", \"dob\":\"" + testEnrolleeDOB +
						"\",\"dependentgroupid\":" + testEnrolleeDependentGroupId + "}"))
				.andExpect(status().isOk());
	}

	@Test
	@Order(4)
	public void testDeleteEnrollee() throws Exception {
		this.mvc.perform(delete(BASEURI + "/enrollee/" + testEnrolleeId))
				.andExpect(status().isOk());
	}

	@Test
	@Order(5)
	public void testFindAll() throws Exception {
		// insert two more enrollees
		Enrollee enrollee1 = new Enrollee(111, "David", true, new SimpleDateFormat("MM/dd/yyyy").parse("08/12/1995"), 701);
		Enrollee enrollee2 = new Enrollee(112, "Amy", false, new SimpleDateFormat("MM/dd/yyyy").parse("11/21/1991"), 801);
		List<Enrollee> enrolleeList = new ArrayList<Enrollee>();
		enrolleeList.add(enrollee1);
		enrolleeList.add(enrollee2);

		when(enrolleeService.getAllEnrollee()).thenReturn(enrolleeList);

		// when
		List<Enrollee> enrolleeListResult = enrolleeController.getAllEnrollee();

		// then
		assertThat(enrolleeListResult.size()).isEqualTo(enrolleeList.size());

		assertThat(enrolleeListResult.get(0).getName()).isEqualTo(enrollee1.getName());
		assertThat(enrolleeListResult.get(0).getActivationStatus()).isEqualTo(enrollee1.getActivationStatus());
		assertThat(enrolleeListResult.get(0).getDob()).isEqualTo(enrollee1.getDob());
		assertThat(enrolleeListResult.get(0).getDependentgroupid()).isEqualTo(enrollee1.getDependentgroupid());

		assertThat(enrolleeListResult.get(1).getName()).isEqualTo(enrollee2.getName());
		assertThat(enrolleeListResult.get(1).getActivationStatus()).isEqualTo(enrollee2.getActivationStatus());
		assertThat(enrolleeListResult.get(1).getDob()).isEqualTo(enrollee2.getDob());
		assertThat(enrolleeListResult.get(1).getDependentgroupid()).isEqualTo(enrollee2.getDependentgroupid());
	}

	@Test
	void contextLoads() {
	}
}
