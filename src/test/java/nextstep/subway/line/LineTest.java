package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineStation;
import nextstep.subway.section.domain.Distance;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.domain.Station;

public class LineTest {

	@DisplayName("노선의 역 목록 조회")
	@Test
	void getStationsTest() {
		//given
		Line lineShinBunDang = new Line("신분당선", "red");

		Station stationGangNam = new Station("강남역");
		Station stationPanGyo = new Station("판교역");

		LineStation firstSection = new LineStation(lineShinBunDang, null, stationGangNam, new Distance(0));
		LineStation secondSection = new LineStation(lineShinBunDang, stationGangNam, stationPanGyo, new Distance(5));

		lineShinBunDang.addLineStation(firstSection);
		lineShinBunDang.addLineStation(secondSection);

		Station stationGwangGyo = new Station("광교역");
		LineStation thirdSection = new LineStation(lineShinBunDang, stationPanGyo, stationGwangGyo, new Distance(7));
		lineShinBunDang.addLineStation(thirdSection);

		//when
		List<Station> actual = lineShinBunDang.getStations();

		//then
		assertThat(actual).isEqualTo(Lists.list(stationGangNam, stationPanGyo, stationGwangGyo));
	}
}
