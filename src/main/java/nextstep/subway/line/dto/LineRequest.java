package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineStation;
import nextstep.subway.section.domain.Distance;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.domain.Station;

public class LineRequest {
	private String name;
	private String color;
	private Long upStationId;
	private Long downStationId;
	private int distance;

	public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
		this.name = name;
		this.color = color;
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}

	public String getName() {
		return this.name;
	}

	public String getColor() {
		return this.color;
	}

	public Long getUpStationId() {
		return this.upStationId;
	}

	public Long getDownStationId() {
		return this.downStationId;
	}

	public int getDistance() {
		return this.distance;
	}

	public Line toLine(Station upStation, Station downStation) {
		return new Line(this.name, this.color, upStation, downStation, new Distance(this.distance));
	}
}
