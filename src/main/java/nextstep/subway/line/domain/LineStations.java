package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class LineStations {

	@OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
	private List<LineStation> lineStations = new ArrayList<>();

	public List<Station> toStations() {

		List<Station> stations = new LinkedList<>();
		Optional<LineStation> preOptionalLineStation = getFirstOptionalLineStation();
		while(preOptionalLineStation.isPresent()) {
			LineStation firstLineStation = preOptionalLineStation.get();
			stations.add(firstLineStation.getStation());
			preOptionalLineStation = this.lineStations.stream()
				.filter(lineStation -> lineStation.getPreStation() == firstLineStation.getStation())
				.findFirst();
		}
		return stations;
	}

	public void add(LineStation lineStation) {

		checkValidation(lineStation);
		this.lineStations.stream()
			.filter(it -> it.getPreStation().equals(lineStation.getPreStation()))
			.findFirst()
			.ifPresent(it -> it.updatePreStationTo(lineStation.getStation()));

		this.lineStations.add(lineStation);
	}

	public void remove(LineStation lineStation) {
		this.lineStations.remove(lineStation);
	}

	private void checkValidation(LineStation lineStation) {
		if (lineStation == null) {
			throw new RuntimeException();
		}

		if (this.lineStations.stream().anyMatch(it -> it.getStation().equals(lineStation.getStation()))) {
			throw new RuntimeException();
		}
	}

	private Optional<LineStation> getFirstOptionalLineStation() {
		return this.lineStations.stream()
			.filter(lineStation -> lineStation.getPreStation() == null)
			.findFirst();
	}
}
