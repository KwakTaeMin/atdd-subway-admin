package nextstep.subway.line.domain;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nextstep.subway.section.domain.Distance;
import nextstep.subway.station.domain.Station;

@Entity
public class LineStation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "line_id")
	private Line line;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pre_station_id", nullable = true)
	private Station preStation;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "station_id", nullable = false)
	private Station station;

	@Embedded
	private Distance distance;

	protected LineStation() {

	}

	public LineStation(Line line, Station preStation, Station station, Distance distance) {
		this.line = line;
		this.preStation = preStation;
		this.station = station;
		this.distance = distance;
	}

	public Long getId() {
		return this.id;
	}

	public Station getPreStation() {
		return preStation;
	}

	public Station getStation() {
		return station;
	}

	public Distance getDistance() {
		return this.distance;
	}

	public void setLine(Line line) {
		if (Objects.nonNull(this.line)) {
			this.line.getLineStations().remove(this);
		}
		this.line = line;
		line.getLineStations().add(this);
	}

	public void updatePreStationTo(Station newPreStation) {
		this.preStation = newPreStation;
	}


	public boolean isSame(LineStation newLineStation) {
		return this.station.getId() == newLineStation.station.getId();
	}
}
