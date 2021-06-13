package nextstep.subway.line.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.section.domain.Distance;
import nextstep.subway.station.domain.Station;

@Entity
public class Line extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String name;

	private String color;

	@Embedded
	private LineStations lineStations = new LineStations();

	protected Line() {

	}

	public Line(String name, String color) {
		this();
		this.name = name;
		this.color = color;
	}

	public Line(String name, String color, Station upStation, Station downStation, Distance distance) {
		this(name, color);
		this.addLineStation(new LineStation(this, null, upStation, new Distance(0)));
		this.addLineStation(new LineStation(this, upStation, downStation, distance));
	}

	public void update(Line line) {
		this.name = line.getName();
		this.color = line.getColor();
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public void addLineStation(LineStation lineStation) {
		this.lineStations.add(lineStation);
		lineStation.setLine(this);
	}

	public LineStations getLineStations() {
		return this.lineStations;
	}

	public List<Station> getStations() {
		return this.lineStations.toStations();
	}
}
