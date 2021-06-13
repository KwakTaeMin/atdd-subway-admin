package nextstep.subway.section;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationRequest;

@DisplayName("노선에 구간 관리 기능 테스트")
public class SectionAcceptanceTest extends AcceptanceTest {

	Long lineNumber2Id;

	@BeforeEach
	void sectionSetUp() {
		Long sungSuStationId = StationAcceptanceTest.지하철역_생성되어_있음(new StationRequest("성수역"));
		Long gangNamStationId = StationAcceptanceTest.지하철역_생성되어_있음(new StationRequest("강남역"));
		LineRequest lineNumber2 = new LineRequest("2호선", "Green", gangNamStationId, sungSuStationId, 10);
		lineNumber2Id = LineAcceptanceTest.지하철_노선_생성되어_있음(lineNumber2);
	}

	@DisplayName("노선에 구간을 뒤에 등록한다.")
	@Test
	void 노선에_구간_등록한다_강남역_뒤에() {
		// when
		// 지하철_노선에_지하철역_등록_요청

		// then
		// 지하철_노선에_지하철역_등록됨
	}
}
