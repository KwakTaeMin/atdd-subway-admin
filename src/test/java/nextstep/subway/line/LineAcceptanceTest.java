package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationRequest;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

	private LineRequest lineNumber1;
	private LineRequest lineNumber2;

	@BeforeEach
	void lineSetUp() {
		Long gangNamStationId = StationAcceptanceTest.지하철역_생성되어_있음(new StationRequest("강남역"));
		Long sungSuStationId = StationAcceptanceTest.지하철역_생성되어_있음(new StationRequest("성수역"));
		Long seoulStationId = StationAcceptanceTest.지하철역_생성되어_있음(new StationRequest("서울역"));
		Long sindorimStationId = StationAcceptanceTest.지하철역_생성되어_있음(new StationRequest("신도림역"));
		lineNumber1 = new LineRequest("1호선", "Blue", seoulStationId, sindorimStationId, 15);
		lineNumber2 = new LineRequest("2호선", "Green", gangNamStationId, sungSuStationId, 10);
	}

	@DisplayName("지하철 노선을 생성한다.")
	@Test
	void createLine() {
		//when
		ExtractableResponse<Response> response = 지하철_노선을_생성한다(lineNumber2);

		// then
		지하철_노선_생성됨(response, lineNumber2);
	}

	@DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
	@Test
	void createLine2() {
		// given
		지하철_노선_생성되어_있음(lineNumber2);
		// when
		ExtractableResponse<Response> response = 지하철_노선을_생성한다(lineNumber2);
		// then
		지하철_노선_생성_실패됨(response);
	}

	@DisplayName("지하철 노선 목록을 조회한다.")
	@Test
	void getLines() {
		// given
		long line1Id = 지하철_노선_생성되어_있음(lineNumber1);
		long line2Id = 지하철_노선_생성되어_있음(lineNumber2);
		// when
		ExtractableResponse<Response> response = 모든_지하철_노선을_조회한다();
		// then
		지하철_노선_목록_응답됨(response);
		지하철_노선_목록_포함됨(response, Arrays.asList(line1Id, line2Id));

	}

	@DisplayName("지하철 노선을 조회한다.")
	@Test
	void getLine() {
		// given
		Long id = 지하철_노선_생성되어_있음(lineNumber2);
		// when
		ExtractableResponse<Response> response = 단일_지하철_노선을_조회한다(id);
		// then
		지하철_노선_조회_확인(response, lineNumber2);
	}

	@DisplayName("지하철 노선을 수정한다.")
	@Test
	void updateLine() {
		// given
		long id = 지하철_노선_생성되어_있음(lineNumber2);
		// when
		ExtractableResponse<Response> response = 지하철_노선을_수정한다(lineNumber1, id);
		// then
		지하철_노선_수정_확인(response);
	}

	@DisplayName("지하철 노선을 제거한다.")
	@Test
	void deleteLine() {
		// given
		long id = 지하철_노선_생성되어_있음(lineNumber1);
		// when
		ExtractableResponse<Response> response = 지하철_노선을_제거한다(id);
		// then
		지하철_노선_제거_확인(response);
	}

	public static ExtractableResponse<Response> 지하철_노선을_생성한다(LineRequest lineRequest) {
		ExtractableResponse<Response> response = RestAssured.given().log().all()
			.body(lineRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.post("/lines")
			.then().log().all()
			.extract();
		return response;
	}

	public static void 지하철_노선_생성됨(ExtractableResponse<Response> response, LineRequest lineRequest) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isNotBlank();
		assertThat(response.body().jsonPath().getString("name")).isEqualTo(lineRequest.getName());
		assertThat(response.body().jsonPath().getString("color")).isEqualTo(lineRequest.getColor());
	}

	public static Long 지하철_노선_생성되어_있음(LineRequest lineRequest) {
		ExtractableResponse<Response> response = 지하철_노선을_생성한다(lineRequest);
		지하철_노선_생성됨(response, lineRequest);
		return response.body().jsonPath().getLong("id");
	}

	void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response, List<Long> expectedLineIds) {
		List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
			.map(it -> it.getId())
			.collect(Collectors.toList());
		assertThat(resultLineIds).containsAll(expectedLineIds);
	}

	ExtractableResponse<Response> 모든_지하철_노선을_조회한다() {
		ExtractableResponse<Response> response = RestAssured.given().log().all()
			.when()
			.get("/lines")
			.then().log().all()
			.extract();
		return response;
	}

	ExtractableResponse<Response> 단일_지하철_노선을_조회한다(Long id) {
		ExtractableResponse<Response> response = RestAssured.given().log().all()
			.when()
			.get("/lines/" + id)
			.then().log().all()
			.extract();
		return response;
	}

	ExtractableResponse<Response> 지하철_노선을_수정한다(LineRequest lineRequest, Long id) {
		ExtractableResponse<Response> response = RestAssured.given().log().all()
			.body(lineRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.put("/lines/" + id)
			.then().log().all()
			.extract();
		return response;
	}

	ExtractableResponse<Response> 지하철_노선을_제거한다(Long id) {
		ExtractableResponse<Response> response = RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.delete("/lines/" + id)
			.then().log().all()
			.extract();
		return response;
	}

	private void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	void 지하철_노선_수정_확인(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	void 지하철_노선_제거_확인(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	void 지하철_노선_조회_확인(ExtractableResponse<Response> response, LineRequest lineRequest) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.body().jsonPath().getString("name")).isEqualTo(lineRequest.getName());
		assertThat(response.body().jsonPath().getString("color")).isEqualTo(lineRequest.getColor());
		assertThat(response.body().jsonPath().getList("stations")).hasSize(2);
		assertThat(response.body().jsonPath().getLong("stations[0].id")).isEqualTo(lineRequest.getUpStationId());
		assertThat(response.body().jsonPath().getLong("stations[1].id")).isEqualTo(lineRequest.getDownStationId());
	}
}
